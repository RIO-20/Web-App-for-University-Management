/** Clasa service pentru logica de business legata de actiuni
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/

package com.raul.univ_management.service;

import com.raul.univ_management.dao.*;
import com.raul.univ_management.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ActionService {

    private final FacultateDao facultateDao;
    private final DotareDao dotareDao;
    private final TranzactieDao tranzactieDao;
    private final Dotare_TranzactieDao dotareTranzactieDao;

    public ActionService(FacultateDao facultateDao, DotareDao dotareDao, 
                         TranzactieDao tranzactieDao, Dotare_TranzactieDao dotareTranzactieDao) {
        this.facultateDao = facultateDao;
        this.dotareDao = dotareDao;
        this.tranzactieDao = tranzactieDao;
        this.dotareTranzactieDao = dotareTranzactieDao;
    }


    public record BuyRequest(Long facultyId, Long roomId, String name, BigDecimal cost, String description, int quantity) {}
    public record RepairRequest(Long facultyId, Long dotareId, BigDecimal cost, String description, int quantity) {}
    public record SellRequest(Long facultyId, Long dotareId, String description, int quantity) {}
    public record ReportRequest(Long facultyId, Long dotareId, String newState, String description, int quantity) {}


    @Transactional
    public void executeBuy(BuyRequest req) {
        BigDecimal totalCost = req.cost.multiply(BigDecimal.valueOf(req.quantity)); 

        
        var budgetStats = facultateDao.getBudgetStats(req.facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));
        
        if (budgetStats.remainingBudget().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient funds! Total Cost: " + totalCost + " > Budget: " + budgetStats.remainingBudget());
        }

        
        Dotare dotare = new Dotare();
        dotare.setIdSala(req.roomId);
        dotare.setNume(req.name);
        dotare.setValoare(req.cost); 
        dotare.setStare("functional");
        dotare.setCantitate(req.quantity > 0 ? req.quantity : 1); 
        dotareDao.insert(dotare); 

        
        Tranzactie tranzactie = new Tranzactie();
        tranzactie.setFacultateId(req.facultyId);
        tranzactie.setTip("Achizitie");
        tranzactie.setDescriere(req.description == null || req.description.isEmpty() ? "Bought " + req.name + " x" + dotare.getCantitate() : req.description);
        tranzactieDao.insert(tranzactie); 

       
        Dotare_Tranzactie link = new Dotare_Tranzactie();
        link.setDotareId(dotare.getId());
        link.setTranzactieId(tranzactie.getId());
        link.setValoare(totalCost); 
        link.setCantitate(dotare.getCantitate());
        link.setDescriere("Initial Purchase");
        dotareTranzactieDao.insert(link);

        
        facultateDao.updateBudget(req.facultyId, totalCost.negate());
    }

  
    @Transactional
    public void executeRepair(RepairRequest req) {
        System.out.println("DEBUG REPAIR: Method called - dotareId=" + req.dotareId + ", quantity=" + req.quantity);
        
        
        int quantityToRepair = req.quantity > 0 ? req.quantity : 1; 

        BigDecimal totalCost = req.cost.multiply(BigDecimal.valueOf(quantityToRepair)); 

       
        var budgetStats = facultateDao.getBudgetStats(req.facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        if (budgetStats.remainingBudget().compareTo(totalCost) < 0) {
            throw new RuntimeException("Insufficient funds for repair! Total: " + totalCost);
        }

       
        Dotare originalItem = dotareDao.findById(req.dotareId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        System.out.println("DEBUG REPAIR: Original item - Name=" + originalItem.getNume() + ", Qty=" + originalItem.getCantitate() + ", State=" + originalItem.getStare());
        
        if (quantityToRepair > originalItem.getCantitate()) {
             throw new RuntimeException("Cannot repair " + quantityToRepair + " items. Only " + originalItem.getCantitate() + " available in this batch.");
        }

        Long targetDotareId = req.dotareId; 

        if (originalItem.getCantitate() > quantityToRepair) {
            System.out.println("DEBUG REPAIR: Partial repair - repairing " + quantityToRepair + " out of " + originalItem.getCantitate());
            
            dotareDao.updateQuantity(originalItem.getId(), originalItem.getCantitate() - quantityToRepair);

           
            System.out.println("DEBUG: Looking for existing functional batch - Room: " + originalItem.getIdSala() + ", Name: " + originalItem.getNume());
            var existingFunctional = dotareDao.findByRoomAndNameAndState(
                originalItem.getIdSala(), 
                originalItem.getNume(), 
                "functional"
            );

            if (existingFunctional.isPresent()) {
               
                Dotare functionalBatch = existingFunctional.get();
                System.out.println("DEBUG: Found existing functional batch ID: " + functionalBatch.getId() + " with quantity: " + functionalBatch.getCantitate());
                System.out.println("DEBUG: Merging " + quantityToRepair + " repaired items. New total: " + (functionalBatch.getCantitate() + quantityToRepair));
                dotareDao.updateQuantity(functionalBatch.getId(), functionalBatch.getCantitate() + quantityToRepair);
                targetDotareId = functionalBatch.getId();
            } else {
                
                System.out.println("DEBUG: No existing functional batch found. Creating new batch.");
                Dotare newItem = new Dotare();
                newItem.setIdSala(originalItem.getIdSala());
                newItem.setNume(originalItem.getNume());
                newItem.setValoare(originalItem.getValoare()); 
                newItem.setStare("functional"); 
                newItem.setCantitate(quantityToRepair);
                
                dotareDao.insert(newItem); 
                targetDotareId = newItem.getId();
            }
        } else {
            
            System.out.println("DEBUG REPAIR: Full batch repair - updating state of all " + originalItem.getCantitate() + " items to functional");
            
            
            System.out.println("DEBUG: Looking for existing functional batch to merge with - Room: " + originalItem.getIdSala() + ", Name: " + originalItem.getNume());
            var existingFunctional = dotareDao.findByRoomAndNameAndState(
                originalItem.getIdSala(), 
                originalItem.getNume(), 
                "functional"
            );
            
            if (existingFunctional.isPresent()) {
               
                Dotare functionalBatch = existingFunctional.get();
                System.out.println("DEBUG: Found existing functional batch ID: " + functionalBatch.getId() + " with quantity: " + functionalBatch.getCantitate());
                System.out.println("DEBUG: Merging all " + originalItem.getCantitate() + " repaired items. New total: " + (functionalBatch.getCantitate() + originalItem.getCantitate()));
                
                dotareDao.updateQuantity(functionalBatch.getId(), functionalBatch.getCantitate() + originalItem.getCantitate());
                dotareDao.delete(originalItem.getId()); 
                targetDotareId = functionalBatch.getId();
            } else {
              
                System.out.println("DEBUG: No existing functional batch found. Just updating state of current batch.");
                dotareDao.updateState(targetDotareId, "functional");
            }
        }

        
        Tranzactie tranzactie = new Tranzactie();
        tranzactie.setFacultateId(req.facultyId);
        tranzactie.setTip("Reparatie");
        tranzactie.setDescriere(req.description);
        tranzactieDao.insert(tranzactie);

        
        Dotare_Tranzactie link = new Dotare_Tranzactie();
        link.setDotareId(targetDotareId);
        link.setTranzactieId(tranzactie.getId());
        link.setValoare(totalCost);
        link.setCantitate(quantityToRepair); 
        link.setDescriere("Repair Cost");
        dotareTranzactieDao.insert(link);

        
        facultateDao.updateBudget(req.facultyId, totalCost.negate());
    }

    
    @Transactional
    public void executeSell(SellRequest req) {
        
        Dotare item = dotareDao.findById(req.dotareId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        int quantityToSell = req.quantity > 0 ? req.quantity : 1; 

        
        BigDecimal multiplier = switch (item.getStare().toLowerCase()) {
            case "functional" -> BigDecimal.ONE;
            case "vechi" -> new BigDecimal("0.50");
            case "stricat" -> new BigDecimal("0.25");
            default -> new BigDecimal("0.25"); 
        };

        BigDecimal unitPrice = item.getValoare().multiply(multiplier);
        BigDecimal totalRevenue = unitPrice.multiply(BigDecimal.valueOf(quantityToSell));

        
        int currentStock = dotareTranzactieDao.getCurrentStock(req.dotareId);

        if (quantityToSell > currentStock) {
            throw new RuntimeException("Cannot sell " + quantityToSell + " items. Only " + currentStock + " available (based on transaction history).");
        }

     
        Tranzactie tranzactie = new Tranzactie();
        tranzactie.setFacultateId(req.facultyId);
        tranzactie.setTip("Vanzare");
        tranzactie.setDescriere(req.description + " (Item: " + item.getNume() + ", Qty: " + quantityToSell + ", Rate: " + (multiplier.multiply(new BigDecimal("100"))) + "%)");
        tranzactieDao.insert(tranzactie);

        
        if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
            facultateDao.updateBudget(req.facultyId, totalRevenue);
        }
        
        
        Dotare_Tranzactie link = new Dotare_Tranzactie();
        link.setDotareId(req.dotareId);
        link.setTranzactieId(tranzactie.getId());
        link.setValoare(totalRevenue);
        link.setCantitate(quantityToSell); 
        link.setDescriere("Sale Revenue (Auto-calc)");
        dotareTranzactieDao.insert(link);

       
        int remainingStock = currentStock - quantityToSell;
        if (remainingStock <= 0) {
            dotareDao.delete(req.dotareId);
        } else {
            dotareDao.updateQuantity(req.dotareId, remainingStock);
        }
    }

   
    @Transactional
    public void executeReportIssue(ReportRequest req) {
        
        Dotare item = dotareDao.findById(req.dotareId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        
        int quantityToReport = req.quantity > 0 ? req.quantity : 1; 

      
        if (quantityToReport > item.getCantitate()) {
            throw new RuntimeException("Cannot report " + quantityToReport + " items. Only " + item.getCantitate() + " available in this batch.");
        }

        Long targetDotareId = req.dotareId;

        
        if (item.getCantitate() > quantityToReport) {
            
            dotareDao.updateQuantity(item.getId(), item.getCantitate() - quantityToReport);

           
            Dotare newItem = new Dotare();
            newItem.setIdSala(item.getIdSala());
            newItem.setNume(item.getNume());
            newItem.setValoare(item.getValoare());
            newItem.setStare(req.newState); 
            newItem.setCantitate(quantityToReport);
            
            dotareDao.insert(newItem);
            targetDotareId = newItem.getId();
        } else {
            
            dotareDao.updateState(targetDotareId, req.newState);
        }

        Tranzactie tranzactie = new Tranzactie();
        tranzactie.setFacultateId(req.facultyId);
        tranzactie.setTip("reparatie"); 
        tranzactie.setDescriere(req.description + " (Reported " + quantityToReport + " items as " + req.newState + ")");
        tranzactieDao.insert(tranzactie);

      
        Dotare_Tranzactie link = new Dotare_Tranzactie();
        link.setDotareId(targetDotareId);
        link.setTranzactieId(tranzactie.getId());
        link.setValoare(BigDecimal.ZERO); 
        link.setCantitate(quantityToReport);
        link.setDescriere("Reported Issue");
        dotareTranzactieDao.insert(link);
    }
}

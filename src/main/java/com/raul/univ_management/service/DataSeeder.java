/** Clasa utilitara pentru popularea initiala a bazei de date
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.service;

import com.raul.univ_management.dao.*;
import com.raul.univ_management.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;

@Service
public class DataSeeder {

    private final FacultateDao facultateDao;
    private final SalaDao salaDao;
    private final DotareDao dotareDao;
    private final TranzactieDao tranzactieDao;
    private final Dotare_TranzactieDao dotareTranzactieDao;
    private final SponsorDao sponsorDao;
    private final Sponsor_FacultateDao sponsorFacultateDao;
    private final ActionService actionService;

    private final Random random = new Random();

    public DataSeeder(FacultateDao facultateDao, SalaDao salaDao, DotareDao dotareDao,
                      TranzactieDao tranzactieDao, Dotare_TranzactieDao dotareTranzactieDao,
                      SponsorDao sponsorDao, Sponsor_FacultateDao sponsorFacultateDao,
                      ActionService actionService) {
        this.facultateDao = facultateDao;
        this.salaDao = salaDao;
        this.dotareDao = dotareDao;
        this.tranzactieDao = tranzactieDao;
        this.dotareTranzactieDao = dotareTranzactieDao;
        this.sponsorDao = sponsorDao;
        this.sponsorFacultateDao = sponsorFacultateDao;
        this.actionService = actionService;
    }

    @Transactional
    public void seedDatabase() {
        
        List<Long> facultyIds = new ArrayList<>();
        facultyIds.add(createFaculty("Computer Science & AI", "Leading tech research", new BigDecimal("500000.00")));
        facultyIds.add(createFaculty("Law & Political Science", "Justice and policy", new BigDecimal("300000.00")));
        facultyIds.add(createFaculty("Medicine & BioTech", "Saving lives", new BigDecimal("800000.00")));
        
        
        List<Facultate> existing = facultateDao.findAll();
        for(Facultate f : existing) {
            if(!facultyIds.contains(f.getId())) facultyIds.add(f.getId());
        }

     
        List<Long> sponsorIds = new ArrayList<>();
        sponsorIds.add(createSponsor("Tech Giant Corp", "Global tech leader", "contact@techgiant.com"));
        sponsorIds.add(createSponsor("Future Foundation", "Supporting education", "grants@future.org"));
        sponsorIds.add(createSponsor("Green Energy Systems", "Sustainable solutions", "csr@greenenergy.com"));

       
        for (Long facultyId : facultyIds) {
           
            int roomCount = 10 + random.nextInt(10); // 10-20 rooms
            for (int i = 1; i <= roomCount; i++) {
                String type = random.nextBoolean() ? "Curs" : "Laborator";
                int capacity = 20 + random.nextInt(180);
                Long roomId = createRoom(facultyId, "Room " + (100 + i) + " (" + type.substring(0,3) + ")", type, capacity);

              
                if (random.nextBoolean()) {
                    int chairCount = Math.min(capacity, 100); 
                    insertEquipment(facultyId, roomId, "Ergonomic Chair", new BigDecimal("250.00"), "Bulk purchase of chairs", chairCount);
                }

                if (type.equals("Laborator")) {
                     insertEquipment(facultyId, roomId, "High-End Workstation", new BigDecimal("4500.00"), "Lab setup", 5 + random.nextInt(5));
                } else {
                     insertEquipment(facultyId, roomId, "4K Projector", new BigDecimal("3500.00"), "Lecture hall upgrade", 1);
                }
            }

          
            for (Long sponsorId : sponsorIds) {
                if (random.nextBoolean()) {
                    Sponsor_Facultate sf = new Sponsor_Facultate();
                    sf.setSponsorId(sponsorId);
                    sf.setFacultateId(facultyId);
                    sf.setTip("bani");
                    sf.setValoare(new BigDecimal(5000 + random.nextInt(20000)));
                    sponsorFacultateDao.insert(sf);
                    facultateDao.updateBudget(facultyId, sf.getValoare());
                } else {
                    Sponsor_Facultate sf = new Sponsor_Facultate();
                    sf.setSponsorId(sponsorId);
                    sf.setFacultateId(facultyId);
                    sf.setTip("obiecte");
                    sf.setValoare(new BigDecimal(2000 + random.nextInt(5000)));
                    sponsorFacultateDao.insert(sf);
                }
            }
        }
    }

    private Long createFaculty(String name, String desc, BigDecimal budget) {
        Facultate f = new Facultate();
        f.setAdminId(1L); 
        f.setNume(name);
        f.setDescriere(desc);
        f.setBuget(budget);
        facultateDao.insert(f); 
        return f.getId();
    }

    private Long createSponsor(String name, String desc, String email) {
        Sponsor s = new Sponsor();
        s.setNume(name);
        s.setDescriere(desc);
        s.setEmail(email);
        sponsorDao.insert(s);
        return s.getId();
    }

    private Long createRoom(Long facultyId, String name, String type, int cap) {
        Sala s = new Sala();
        s.setFacultateId(facultyId);
        s.setNume(name);
        s.setTip(type);
        s.setCapacitate(cap);
        salaDao.insert(s);
        return s.getId();
    }
    private void insertEquipment(Long facultyId, Long roomId, String name, BigDecimal unitCost, String description, int quantity) {
        Dotare dotare = new Dotare();
        dotare.setIdSala(roomId);
        dotare.setNume(name);
        dotare.setValoare(unitCost); 
        dotare.setStare("functional");
        dotare.setCantitate(quantity); 
        dotareDao.insert(dotare); 

        Tranzactie tranzactie = new Tranzactie();
        tranzactie.setFacultateId(facultyId);
        tranzactie.setTip("Achizitie");
        tranzactie.setDescriere(description + " - " + name + " x" + quantity + " (Seeded)");
        tranzactieDao.insert(tranzactie); 

        Dotare_Tranzactie link = new Dotare_Tranzactie();
        link.setDotareId(dotare.getId());
        link.setTranzactieId(tranzactie.getId());
        link.setValoare(unitCost.multiply(BigDecimal.valueOf(quantity))); 
        link.setCantitate(quantity);
        link.setDescriere("Seeded Data");
        dotareTranzactieDao.insert(link);
        
    }
}

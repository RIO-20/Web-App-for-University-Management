/** Clasa controller pentru gestionarea actiunilor (cumparare, reparare, vanzare)
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/

package com.raul.univ_management.controller;

import com.raul.univ_management.model.Administrator;
import com.raul.univ_management.service.ActionService;
import com.raul.univ_management.service.FacultateService;
import com.raul.univ_management.service.SalaService;
import com.raul.univ_management.service.DotareService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@Controller
@RequestMapping("/actions")
public class ActionController {

    private final ActionService actionService;
    private final FacultateService facultateService;
    private final SalaService salaService;
    private final DotareService dotareService;

    public ActionController(ActionService actionService, FacultateService facultateService, 
                            SalaService salaService, DotareService dotareService) {
        this.actionService = actionService;
        this.facultateService = facultateService;
        this.salaService = salaService;
        this.dotareService = dotareService;
    }

    @GetMapping
    public String showActionsPage(jakarta.servlet.http.HttpSession session, Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) return "redirect:/login";
        model.addAttribute("admin", admin);

     
        if ("super".equals(admin.getUsername())) {
             model.addAttribute("error", "Actions are not available for Super Admin (No Faculty Assigned). Please use the Admin Console.");
             return "actions"; 
        }

        var faculty = facultateService.findByAdminId(admin.getId());
        if (faculty == null) {
            model.addAttribute("error", "No faculty assigned to this admin. Cannot perform actions.");
            return "actions";
        }
        model.addAttribute("faculty", faculty);

    
        model.addAttribute("rooms", salaService.findByFaculty(faculty.id()));
        
        
        model.addAttribute("repairableItems", dotareService.findRepairableByFaculty(faculty.id())); 
        model.addAttribute("sellableItems", dotareService.findAllByFaculty(faculty.id())); 

        return "actions";
    }

    @PostMapping("/buy")
    public String handleBuy(@RequestParam Long roomId,
                            @RequestParam String name,
                            @RequestParam BigDecimal cost,
                            @RequestParam String description,
                            @RequestParam(defaultValue = "1") int quantity,
                            jakarta.servlet.http.HttpSession session,
                            Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) return "redirect:/login";
        
        var faculty = facultateService.findByAdminId(admin.getId());
        
        try {
            actionService.executeBuy(new ActionService.BuyRequest(faculty.id(), roomId, name, cost, description, quantity));
            model.addAttribute("success", "Item(s) bought successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Buy Failed: " + e.getMessage());
        }
        return showActionsPage(session, model); 
    }

    @PostMapping("/repair")
    public String handleRepair(@RequestParam Long dotareId,
                               @RequestParam BigDecimal cost,
                               @RequestParam String description,
                               @RequestParam(defaultValue = "1") int quantity,
                               jakarta.servlet.http.HttpSession session,
                               Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) return "redirect:/login";
        
        var faculty = facultateService.findByAdminId(admin.getId());

        try {
            actionService.executeRepair(new ActionService.RepairRequest(faculty.id(), dotareId, cost, description, quantity));
            model.addAttribute("success", "Repair logged successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Repair Failed: " + e.getMessage());
        }
        return showActionsPage(session, model);
    }

    @PostMapping("/sell")
    public String handleSell(@RequestParam Long dotareId,
                             @RequestParam String description,
                             @RequestParam(defaultValue = "1") int quantity,
                             jakarta.servlet.http.HttpSession session,
                             Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) return "redirect:/login";
        
        var faculty = facultateService.findByAdminId(admin.getId());

        try {
            actionService.executeSell(new ActionService.SellRequest(faculty.id(), dotareId, description, quantity));
            model.addAttribute("success", "Action complete!");
        } catch (Exception e) {
            model.addAttribute("error", "Sell/Delete Failed: " + e.getMessage());
        }
        return showActionsPage(session, model);
    }

    @PostMapping("/report")
    public String handleReport(@RequestParam Long dotareId,
                               @RequestParam String newState,
                               @RequestParam String description,
                               @RequestParam(defaultValue = "1") int quantity,
                               jakarta.servlet.http.HttpSession session,
                               Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) return "redirect:/login";

        var faculty = facultateService.findByAdminId(admin.getId());

        try {
            actionService.executeReportIssue(new ActionService.ReportRequest(faculty.id(), dotareId, newState, description, quantity));
            model.addAttribute("success", "Issue reported successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Report Failed: " + e.getMessage());
        }
        return showActionsPage(session, model);
    }
}

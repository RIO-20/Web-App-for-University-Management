/** Clasa controller pentru gestionarea echipamentelor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/

package com.raul.univ_management.controller;

import com.raul.univ_management.model.Administrator;
import com.raul.univ_management.service.DotareService;
import com.raul.univ_management.service.FacultateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EquipmentController {

    private final DotareService dotareService;
    private final FacultateService facultateService;

    public EquipmentController(DotareService dotareService, FacultateService facultateService) {
        this.dotareService = dotareService;
        this.facultateService = facultateService;
    }

    @GetMapping("/equipment")
    public String showEquipmentAnalysis(jakarta.servlet.http.HttpSession session, Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) {
            return "redirect:/login";
        }
        
        

        model.addAttribute("admin", admin);


      if ("super".equals(admin.getUsername())) {
             model.addAttribute("error", "Equipment analysis is not available for super admin (No Faculty Assigned).");
             return "equipment"; 
        }
        
        var faculty = facultateService.findByAdminId(admin.getId());
        if (faculty != null) {
            model.addAttribute("faculty", faculty);
            model.addAttribute("maintenanceAnalysis", dotareService.getMaintenanceAnalysis(faculty.id()));
        } else {
             model.addAttribute("error", "No faculty assigned to this admin.");
        }

        return "equipment";
    }

    @GetMapping("/equipment/room/{id}")
    public String showRoomDetails(@org.springframework.web.bind.annotation.PathVariable Long id, jakarta.servlet.http.HttpSession session, Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("admin", admin);
        model.addAttribute("roomName", dotareService.getRoomName(id));
        var equipmentList = dotareService.getEquipmentWithHistory(id);
        model.addAttribute("equipmentList", equipmentList);
        

        java.util.Map<String, Long> equipmentCounts = dotareService.getEquipmentCountByRoom(id);
        model.addAttribute("equipmentCounts", equipmentCounts);
        
        return "equipment_details";
    }
}

/** Clasa controller pentru gestionarea sponsorilor si donatiilor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.controller;

import com.raul.univ_management.model.Administrator;
import com.raul.univ_management.model.Sponsor;
import com.raul.univ_management.service.FacultateService;
import com.raul.univ_management.service.SponsorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class SponsorController {

    private final SponsorService sponsorService;
    private final FacultateService facultateService;
    private final com.raul.univ_management.service.SalaService salaService;

    public SponsorController(SponsorService sponsorService, FacultateService facultateService, com.raul.univ_management.service.SalaService salaService) {
        this.sponsorService = sponsorService;
        this.facultateService = facultateService;
        this.salaService = salaService;
    }

    @GetMapping("/sponsors")
    public String showSponsors(jakarta.servlet.http.HttpSession session, Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) return "redirect:/login";

        model.addAttribute("admin", admin);
        model.addAttribute("allSponsors", sponsorService.findAll()); // For dropdowns

        if ("super".equals(admin.getUsername())) {
            
             model.addAttribute("stats", sponsorService.getGlobalSponsorAnalysis());
             model.addAttribute("allFaculties", facultateService.getAllFaculties()); 
             return "sponsors";
        }

        var faculty = facultateService.findByAdminId(admin.getId());
        if (faculty == null) {
            model.addAttribute("error", "No faculty assigned.");
            model.addAttribute("stats", java.util.Collections.emptyList());
            return "sponsors";
        }

        model.addAttribute("faculty", faculty);
        model.addAttribute("stats", sponsorService.getSponsorAnalysis(faculty.id()));
        model.addAttribute("rooms", salaService.findByFaculty(faculty.id())); 
        
        return "sponsors";
    }

    @PostMapping("/sponsors/add")
    public String addSponsor(@RequestParam String name, 
                             @RequestParam String email, 
                             @RequestParam String description) {
        Sponsor sponsor = new Sponsor();
        sponsor.setNume(name);
        sponsor.setEmail(email);
        sponsor.setDescriere(description);
        sponsorService.registerSponsor(sponsor);
        return "redirect:/sponsors";
    }

    @PostMapping("/sponsors/donate")
    public String addDonation(@RequestParam Long sponsorId,
                              @RequestParam Long facultyId,
                              @RequestParam String type,
                              @RequestParam BigDecimal amount,
                              @RequestParam String description,
                              @RequestParam(required = false) Long roomId,
                              @RequestParam(required = false) Integer quantity,
                              Model model) {
        System.out.println("DEBUG: Controller addDonation called. SponsorId: " + sponsorId + ", FacultyId: " + facultyId + ", Type: " + type + ", Amount: " + amount);
        try {
            sponsorService.registerDonation(sponsorId, facultyId, type, amount, description, roomId, quantity);
        } catch (IllegalArgumentException e) {
            return "redirect:/sponsors?error=" + e.getMessage();
        } catch (Exception e) {
            
            return "redirect:/sponsors?error=Database Error: " + e.getMessage();
        }
        return "redirect:/sponsors";
    }
}

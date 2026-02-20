/** Clasa controller pentru gestionarea paginilor principale si autentificare
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.raul.univ_management.dao.AdminDao;
import com.raul.univ_management.model.Administrator;
import com.raul.univ_management.service.FacultateService;
import com.raul.univ_management.service.SalaService;
import com.raul.univ_management.service.SponsorService;
import com.raul.univ_management.service.DotareService;
import com.raul.univ_management.service.TranzactieService;
import com.raul.univ_management.service.Sponsor_FacultateService;
import com.raul.univ_management.service.Dotare_TranzactieService;

import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.raul.univ_management.dto.FacultateDto;
import com.raul.univ_management.dto.AdministratorDto;
import com.raul.univ_management.dto.SponsorDto;
import com.raul.univ_management.dto.SalaDto;
import com.raul.univ_management.dto.DotareDto;
import com.raul.univ_management.dto.TranzactieDto;
import com.raul.univ_management.dto.Dotare_TranzactieDto;
import com.raul.univ_management.service.AdministratorService;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PageController {

    private final AdminDao adminDAO;
    private final FacultateService facultateService;
    private final SalaService salaService;
    private final SponsorService sponsorService;
    private final DotareService dotareService;
    private final TranzactieService tranzactieService;
    private final AdministratorService administratorService;
    private final Sponsor_FacultateService sponsorFacultateService;
    private final Dotare_TranzactieService dotareTranzactieService;

    public PageController(AdminDao adminDAO, FacultateService facultateService, 
                          SalaService salaService, SponsorService sponsorService,
                          DotareService dotareService, TranzactieService tranzactieService,
                          AdministratorService administratorService,
                          Sponsor_FacultateService sponsorFacultateService,
                          Dotare_TranzactieService dotareTranzactieService) {
        this.adminDAO = adminDAO;
        this.facultateService = facultateService;
        this.salaService = salaService;
        this.sponsorService = sponsorService;
        this.dotareService = dotareService;
        this.tranzactieService = tranzactieService;
        this.administratorService = administratorService;
        this.sponsorFacultateService = sponsorFacultateService;
        this.dotareTranzactieService = dotareTranzactieService;
    }

    @GetMapping("/")
    public String showLandingPage(){
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model,
                               jakarta.servlet.http.HttpSession session) {

        Optional<Administrator> optionalAdmin = adminDAO.findByUsernameOrEmail(username);

        if(optionalAdmin.isPresent()){
            Administrator adm = optionalAdmin.get();
            if (adm.getParola().equals(password)) {
                System.out.println("Login Successful for: " + username);
                session.setAttribute("loggedInAdmin", adm);
                return "redirect:/dashboard";
            }
        }

        
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/dashboard")
    public String showDashboard(jakarta.servlet.http.HttpSession session, Model model) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) {
            return "redirect:/login";
        }
        model.addAttribute("admin", admin);
        
        
        com.raul.univ_management.dto.FacultateDto faculty = facultateService.findByAdminId(admin.getId());
        if(faculty != null) {
            model.addAttribute("budgetStats", facultateService.getBudgetStats(faculty.id()));
        }
        
        return "dashboard";
    }
    
    @GetMapping("/logout")
    public String logout(jakarta.servlet.http.HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/console")
    public String AdminConsole(jakarta.servlet.http.HttpSession session, org.springframework.ui.Model model){
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null || !admin.getUsername().equals("super")) {
            return "redirect:/login";
        }
        model.addAttribute("admin", admin);
        return "admin_console";
    }
    @GetMapping("/console/insert")
    public String AdminInsert(jakarta.servlet.http.HttpSession session, Model model){
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null || !admin.getUsername().equals("super")) { return "redirect:/login"; }
        
    
       
        List<Administrator> allAdmins = adminDAO.findAll();
        List<FacultateDto> existingFaculties = facultateService.getAllFaculties();
        
        Set<Long> assignedAdminIds = existingFaculties.stream()
                .map(FacultateDto::adminId)
                .collect(Collectors.toSet());

        List<Administrator> availableAdmins = allAdmins.stream()
                .filter(a -> !assignedAdminIds.contains(a.getId()))
                .collect(Collectors.toList());

        model.addAttribute("admins", availableAdmins);
        model.addAttribute("facultati", existingFaculties);
        model.addAttribute("sali", salaService.findAll());
        model.addAttribute("sponsori", sponsorService.findAll());
        model.addAttribute("dotari", dotareService.findAll());

        List<TranzactieDto> allTranzactii = tranzactieService.findAll();
        List<Dotare_TranzactieDto> linkDt = dotareTranzactieService.findAll();
        Set<Long> usedTranzactieIds = linkDt.stream()
                .map(Dotare_TranzactieDto::tranzactieId)
                .collect(Collectors.toSet());
        List<TranzactieDto> availableTranzactii = allTranzactii.stream()
                .filter(t -> !usedTranzactieIds.contains(t.id()))
                .collect(Collectors.toList());

        model.addAttribute("tranzactii", availableTranzactii);

        return "admin_insert";
    }

    @GetMapping("/console/update")
    public String AdminUpdate(jakarta.servlet.http.HttpSession session, Model model){
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null || !admin.getUsername().equals("super")) { return "redirect:/login"; }
        
        model.addAttribute("admins", adminDAO.findAll());
        model.addAttribute("facultati", facultateService.getAllFaculties());
        model.addAttribute("sali", salaService.findAll());
        model.addAttribute("sponsori", sponsorService.findAll());
        model.addAttribute("dotari", dotareService.findAll());
        model.addAttribute("tranzactii", tranzactieService.findAll());
        
        return "admin_update";
    }

    @GetMapping("/console/delete")
    public String AdminDelete(jakarta.servlet.http.HttpSession session, Model model){
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null || !admin.getUsername().equals("super")) { return "redirect:/login"; }
        
        model.addAttribute("admins", adminDAO.findAll());
        model.addAttribute("facultati", facultateService.getAllFaculties());
        model.addAttribute("sali", salaService.findAll());
        model.addAttribute("sponsori", sponsorService.findAll());
        model.addAttribute("dotari", dotareService.findAll());
        model.addAttribute("tranzactii", tranzactieService.findAll());
        
        return "admin_delete";
    }

    @GetMapping("/console/view")
    public String AdminView(jakarta.servlet.http.HttpSession session, Model model){
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null || !admin.getUsername().equals("super")) { return "redirect:/login"; }
        
        model.addAttribute("admins", adminDAO.findAll());
        model.addAttribute("facultati", facultateService.getAllFaculties());
        model.addAttribute("sali", salaService.findAll());
        model.addAttribute("sponsori", sponsorService.findAll());
        model.addAttribute("dotari", dotareService.findAll());
        model.addAttribute("tranzactii", tranzactieService.findAll());
        model.addAttribute("sponsor_facultate", sponsorFacultateService.getAll());
        model.addAttribute("dotare_tranzactie", dotareTranzactieService.findAll());
        
        return "admin_view";
    }

    @GetMapping("/analysis")
    public String AdminAnalysis(jakarta.servlet.http.HttpSession session, Model model){
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        if (admin == null) { return "redirect:/login"; }
        
        com.raul.univ_management.dto.FacultateDto faculty = facultateService.findByAdminId(admin.getId());
        if(faculty != null) {
            model.addAttribute("faculty", faculty);
            model.addAttribute("transactions", tranzactieService.findRecentByFaculty(faculty.id(), 10)); // Top 10
            model.addAttribute("roomStats", salaService.getRoomStatsForFaculty(faculty.id()));
            model.addAttribute("financialImpact", tranzactieService.getFinancialImpact(faculty.id(), 5000.0, 1000.0));
            model.addAttribute("roomEfficiency", salaService.getRoomEfficiencyAnalysis(faculty.id()));
            
          
            model.addAttribute("highValueTransactions", tranzactieService.findHighValueTransactions());
            model.addAttribute("wellEquippedFaculties", facultateService.findWellEquippedFaculties());
        } else {
            model.addAttribute("error", "No faculty assigned to this admin.");
        }
        
        return "analysis";
    }

    @GetMapping("/back")
    public String BackToDashboard(jakarta.servlet.http.HttpSession session){
        session.invalidate();
        return "redirect:/dashboard";
    }

    @PostMapping("/console/insert/facultate")
    public String processInsertFacultate(@org.springframework.web.bind.annotation.ModelAttribute com.raul.univ_management.dto.FacultateDto facultateDto,
                                       Model model,
                                       jakarta.servlet.http.HttpSession session) {
         Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
         if (admin == null || !admin.getUsername().equals("super")) { return "redirect:/login"; }
         
         List<Administrator> allAdmins = adminDAO.findAll();
         List<FacultateDto> existingFaculties = facultateService.getAllFaculties();

         Set<Long> assignedAdminIds = existingFaculties.stream()
                 .map(FacultateDto::adminId)
                 .collect(Collectors.toSet());

         List<Administrator> availableAdmins = allAdmins.stream()
                 .filter(a -> !assignedAdminIds.contains(a.getId()))
                 .collect(Collectors.toList());

         model.addAttribute("admins", availableAdmins);

         try {
             facultateService.insertFacultate(facultateDto);
             model.addAttribute("success", "Faculty created successfully!");
         } catch (Exception e) {
             model.addAttribute("error", "Error creating faculty: " + e.getMessage());
         }
         
         return "admin_insert";
    }

    @PostMapping("/console/insert/admin")
    public String processInsertAdmin(@ModelAttribute AdministratorDto adminDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            administratorService.insert(adminDto);
            model.addAttribute("success", "Administrator created successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error creating administrator: " + e.getMessage());
        }
        return reloadInsertPage(model);
    }

    @PostMapping("/console/insert/sponsor")
    public String processInsertSponsor(@ModelAttribute SponsorDto sponsorDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            sponsorService.insert(sponsorDto);
            model.addAttribute("success", "Sponsor created successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error creating sponsor: " + e.getMessage());
        }
        return reloadInsertPage(model);
    }

    @PostMapping("/console/insert/sala")
    public String processInsertSala(@ModelAttribute SalaDto salaDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            salaService.insert(salaDto);
            model.addAttribute("success", "Sala created successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error creating sala: " + e.getMessage());
        }
        return reloadInsertPage(model);
    }

    @PostMapping("/console/insert/dotare")
    public String processInsertDotare(@ModelAttribute DotareDto dotareDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            dotareService.insert(dotareDto);
            model.addAttribute("success", "Dotare created successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error creating dotare: " + e.getMessage());
        }
        return reloadInsertPage(model);
    }

    @PostMapping("/console/insert/tranzactie")
    public String processInsertTranzactie(@ModelAttribute TranzactieDto tranzactieDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            tranzactieService.insert(tranzactieDto);
            model.addAttribute("success", "Tranzactie created successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error creating tranzactie: " + e.getMessage());
        }
        return reloadInsertPage(model);
    }

    private boolean isAdmin(jakarta.servlet.http.HttpSession session) {
        Administrator admin = (Administrator) session.getAttribute("loggedInAdmin");
        return admin != null && admin.getUsername().equals("super");
    }

    private String reloadInsertPage(Model model) {
        
         List<Administrator> allAdmins = adminDAO.findAll();
         List<FacultateDto> existingFaculties = facultateService.getAllFaculties();

         Set<Long> assignedAdminIds = existingFaculties.stream()
                 .map(FacultateDto::adminId)
                 .collect(Collectors.toSet());

         List<Administrator> availableAdmins = allAdmins.stream()
                 .filter(a -> !assignedAdminIds.contains(a.getId()))
                 .collect(Collectors.toList());
        model.addAttribute("admins", availableAdmins);

        model.addAttribute("facultati", facultateService.getAllFaculties());
        model.addAttribute("sali", salaService.findAll());
        model.addAttribute("sponsori", sponsorService.findAll());
        model.addAttribute("dotari", dotareService.findAll());
        
        List<TranzactieDto> allTranzactii = tranzactieService.findAll();
        List<Dotare_TranzactieDto> linkDt = dotareTranzactieService.findAll();
        Set<Long> usedTranzactieIds = linkDt.stream()
                .map(Dotare_TranzactieDto::tranzactieId)
                .collect(Collectors.toSet());
        List<TranzactieDto> availableTranzactii = allTranzactii.stream()
                .filter(t -> !usedTranzactieIds.contains(t.id()))
                .collect(Collectors.toList());

        model.addAttribute("tranzactii", availableTranzactii);
        
        return "admin_insert";
    }
    @PostMapping("/console/update/facultate")
    public String processUpdateFacultate(@ModelAttribute FacultateDto facultateDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            facultateService.updateFacultate(facultateDto);
            model.addAttribute("success", "Faculty updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating faculty: " + e.getMessage());
        }
        return reloadUpdatePage(model);
    }

    @PostMapping("/console/update/admin")
    public String processUpdateAdmin(@ModelAttribute AdministratorDto adminDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            administratorService.update(adminDto);
            model.addAttribute("success", "Administrator updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating administrator: " + e.getMessage());
        }
        return reloadUpdatePage(model);
    }

    @PostMapping("/console/update/sponsor")
    public String processUpdateSponsor(@ModelAttribute SponsorDto sponsorDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            sponsorService.update(sponsorDto);
            model.addAttribute("success", "Sponsor updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating sponsor: " + e.getMessage());
        }
        return reloadUpdatePage(model);
    }

    @PostMapping("/console/update/sala")
    public String processUpdateSala(@ModelAttribute SalaDto salaDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            salaService.update(salaDto);
            model.addAttribute("success", "Sala updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating sala: " + e.getMessage());
        }
        return reloadUpdatePage(model);
    }

    @PostMapping("/console/update/dotare")
    public String processUpdateDotare(@ModelAttribute DotareDto dotareDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            dotareService.update(dotareDto);
            model.addAttribute("success", "Dotare updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating dotare: " + e.getMessage());
        }
        return reloadUpdatePage(model);
    }

    @PostMapping("/console/update/tranzactie")
    public String processUpdateTranzactie(@ModelAttribute TranzactieDto tranzactieDto, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            tranzactieService.update(tranzactieDto);
            model.addAttribute("success", "Tranzactie updated successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error updating tranzactie: " + e.getMessage());
        }
        return reloadUpdatePage(model);
    }

    private String reloadUpdatePage(Model model) {
        model.addAttribute("admins", adminDAO.findAll());
        model.addAttribute("facultati", facultateService.getAllFaculties());
        model.addAttribute("sali", salaService.findAll());
        model.addAttribute("sponsori", sponsorService.findAll());
        model.addAttribute("dotari", dotareService.findAll());
        model.addAttribute("tranzactii", tranzactieService.findAll());
        return "admin_update";
    }
    @PostMapping("/console/delete/facultate")
    public String processDeleteFacultate(@RequestParam Long id, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            facultateService.deleteFacultate(id);
            model.addAttribute("success", "Faculty deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting faculty: " + e.getMessage());
        }
        return reloadDeletePage(model);
    }

    @PostMapping("/console/delete/admin")
    public String processDeleteAdmin(@RequestParam Long id, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            administratorService.delete(id);
            model.addAttribute("success", "Administrator deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting administrator: " + e.getMessage());
        }
        return reloadDeletePage(model);
    }

    @PostMapping("/console/delete/sponsor")
    public String processDeleteSponsor(@RequestParam Long id, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            sponsorService.delete(id);
            model.addAttribute("success", "Sponsor deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting sponsor: " + e.getMessage());
        }
        return reloadDeletePage(model);
    }

    @PostMapping("/console/delete/sala")
    public String processDeleteSala(@RequestParam Long id, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            salaService.delete(id);
            model.addAttribute("success", "Sala deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting sala: " + e.getMessage());
        }
        return reloadDeletePage(model);
    }

    @PostMapping("/console/delete/dotare")
    public String processDeleteDotare(@RequestParam Long id, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            dotareService.delete(id);
            model.addAttribute("success", "Dotare deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting dotare: " + e.getMessage());
        }
        return reloadDeletePage(model);
    }

    @PostMapping("/console/delete/tranzactie")
    public String processDeleteTranzactie(@RequestParam Long id, Model model, jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        try {
            tranzactieService.delete(id);
            model.addAttribute("success", "Tranzactie deleted successfully!");
        } catch (Exception e) {
            model.addAttribute("error", "Error deleting tranzactie: " + e.getMessage());
        }
        return reloadDeletePage(model);
    }

    private String reloadDeletePage(Model model) {
        model.addAttribute("admins", adminDAO.findAll());
        model.addAttribute("facultati", facultateService.getAllFaculties());
        model.addAttribute("sali", salaService.findAll());
        model.addAttribute("sponsori", sponsorService.findAll());
        model.addAttribute("dotari", dotareService.findAll());
        model.addAttribute("tranzactii", tranzactieService.findAll());
        return "admin_delete";
    }
}

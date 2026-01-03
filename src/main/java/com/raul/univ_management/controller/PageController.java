package com.raul.univ_management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.raul.univ_management.dao.AdminDao;
import com.raul.univ_management.model.Administrator;

import java.util.Optional;

@Controller
public class PageController {

    private final AdminDao adminDAO;

    public PageController(AdminDao adminDAO) {
        this.adminDAO = adminDAO;
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
                               Model model) {

        Optional<Administrator> optionalAdmin = adminDAO.findByUsername(username);

        if(optionalAdmin.isPresent()){
            Administrator adm = optionalAdmin.get();
            if (adm.getParola().equals(password)) {
                System.out.println("Login Successful for: " + username);
                return "redirect:/dashboard";
            }
        }

        System.out.println("Login Failed for: " + username);
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard";
    }
}

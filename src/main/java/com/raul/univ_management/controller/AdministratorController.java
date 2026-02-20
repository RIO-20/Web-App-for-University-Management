/** Clasa controller pentru gestionarea administratorilor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
package com.raul.univ_management.controller;

import com.raul.univ_management.dto.AdministratorDto;
import com.raul.univ_management.service.AdministratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")

public class AdministratorController {

    private final AdministratorService administratorService;

    public AdministratorController(AdministratorService administratorService) {
        this.administratorService = administratorService;
    }

    @GetMapping("/{identifier}")
    public ResponseEntity<AdministratorDto> getAdmin(@PathVariable String identifier) {
        AdministratorDto admin = administratorService.findByUsernameOrEmail(identifier);
        if (admin != null) {
            return ResponseEntity.ok(admin);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

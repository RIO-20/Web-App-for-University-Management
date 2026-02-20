/** Clasa controller pentru gestionarea facultatilor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/

package com.raul.univ_management.controller;

import com.raul.univ_management.dto.FacultateDto;
import com.raul.univ_management.service.FacultateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facultati")
public class FacultateController {
    private final FacultateService facultateService;

    public FacultateController(FacultateService facultateService) {
        this.facultateService = facultateService;
    }

    @GetMapping
    public ResponseEntity<List<FacultateDto>> getAllFaculties() {
        return ResponseEntity.ok(facultateService.getAllFaculties());
    }
}

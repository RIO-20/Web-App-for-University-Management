package com.raul.univ_management.controller;

import com.raul.univ_management.dao.FacultateDao;
import com.raul.univ_management.model.Facultate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/facultati")
public class FacultateController {
    private final FacultateDao facDao;
    public FacultateController(FacultateDao facultateDAO) {
        this.facDao = facultateDAO;
    }

    @GetMapping
    public List<Facultate> getAllFaculties() {
        return facDao.findAll();
    }
}

package com.raul.univ_management.dto;

import java.math.BigDecimal;
/** Clasa DTO pentru transferul datelor Sponsor_Facultate
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record Sponsor_FacultateDto (
        Long id,
        Long facultateId,
        Long sponsorId,
        String tip,
        BigDecimal valoare
){}



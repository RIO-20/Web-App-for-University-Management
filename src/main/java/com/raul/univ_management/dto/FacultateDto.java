package com.raul.univ_management.dto;

import java.math.BigDecimal;


/** Clasa DTO pentru transferul datelor Facultate
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record FacultateDto(
        Long id,
        Long adminId,
        String nume,
        String descriere,
        BigDecimal buget
) {}

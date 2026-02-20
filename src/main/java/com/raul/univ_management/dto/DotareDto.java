package com.raul.univ_management.dto;
import java.math.BigDecimal;

/** Clasa DTO pentru transferul datelor Dotare
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record DotareDto (
        long id,
        long idSala,
        String nume,
        BigDecimal valoare,
        String stare,
        int cantitate
){}

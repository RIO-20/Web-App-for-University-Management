package com.raul.univ_management.dto;
import java.math.BigDecimal;

/** Clasa DTO pentru transferul datelor Dotare_Tranzactie
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record Dotare_TranzactieDto (
    Long id,
    Long tranzactieId,
    Long dotareId,
    int cantitate,
    BigDecimal valoare,
    String descriere
){}

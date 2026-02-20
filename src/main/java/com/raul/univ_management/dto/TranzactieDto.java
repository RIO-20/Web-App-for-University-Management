package com.raul.univ_management.dto;

/** Clasa DTO pentru transferul datelor Tranzactie
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record TranzactieDto (
    Long id,
    Long facultateId,
    String tip_tranzactie,
    String descriere
){}

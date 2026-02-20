package com.raul.univ_management.dto;


/** Clasa DTO pentru transferul datelor Sala
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record SalaDto (
    Long id,
    Long facultateId,
    String nume,
    String tip,
    int capacitate
){}

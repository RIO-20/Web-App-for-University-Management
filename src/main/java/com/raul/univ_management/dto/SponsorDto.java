package com.raul.univ_management.dto;

/** Clasa DTO pentru transferul datelor Sponsor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record SponsorDto
   (
    Long id,
    String nume_sponsor,
    String descriere,
    String email
   ){}


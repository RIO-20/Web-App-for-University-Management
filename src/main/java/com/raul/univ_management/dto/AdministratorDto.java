package com.raul.univ_management.dto;


/** Clasa DTO pentru transferul datelor Administrator
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record AdministratorDto(
  Long id,
  String nume,
  String prenume,
  String email,
  String telefon,
  String username,
  String parola
) {}

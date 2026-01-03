/** Clasa pentru tabelul de Administratori
 * @author Ionescu Raul-Andrei
 * @version 17 Decembrie 2025
 */


package com.raul.univ_management.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Administrator {
    private long id;
    private String nume;
    private String prenume;
    private String email;
    private String username;
    private String parola;
    private String telefon;
}

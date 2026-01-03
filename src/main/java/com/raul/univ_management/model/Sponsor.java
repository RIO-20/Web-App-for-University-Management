/** Clasa pentru tabelul de Sponsori
 * @author Ionescu Raul-Andrei
 * @version 17 Decembrie 2025
 */

package com.raul.univ_management.model;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sponsor {
    private long id;
    private String nume;
    private String  descriere;
    private String  email;
}

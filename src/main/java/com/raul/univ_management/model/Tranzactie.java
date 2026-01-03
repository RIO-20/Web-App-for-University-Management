/** Clasa pentru tabelul de Tranzactii
 * @author Ionescu Raul-Andrei
 * @version 17 Decembrie 2025
 */

package com.raul.univ_management.model;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tranzactie {
    private long id;
    private long facultateId;
    private String tip;
    private String descriere;
}

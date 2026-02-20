/** Clasa pentru tabelul de legatura Dotare_Tranzactie
 * @author Ionescu Raul-Andrei
 * @version 17 Decembrie 2025
 */
package com.raul.univ_management.model;

import lombok.*;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

/** Clasa model pentru entitatea Dotare_Tranzactie
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Dotare_Tranzactie {
    private long id;
    private long dotareId;
    private long tranzactieId;
    private int cantitate;
    private BigDecimal valoare;
    private String descriere;

}

/** Clasa pentru tabelul de Dotari
 * @author Ionescu Raul-Andrei
 * @version 17 Decembrie 2025
 */

package com.raul.univ_management.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

/** Clasa model pentru entitatea Dotare
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Dotare {
    private long id;
    private long idSala;
    private String nume;
    private BigDecimal valoare;
    private String stare;
    private int cantitate;
}

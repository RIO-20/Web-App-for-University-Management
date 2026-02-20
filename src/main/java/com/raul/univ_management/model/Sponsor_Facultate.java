/** Clasa pentru tabelul de legatura Sponsor_Facultate
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

/** Clasa model pentru entitatea Sponsor_Facultate
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Sponsor_Facultate {
    private long id;
    private long facultateId;
    private long sponsorId;
    private String tip;
    private BigDecimal valoare;
}

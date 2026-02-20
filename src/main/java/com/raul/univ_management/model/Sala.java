/** Clasa pentru tabelul de Sali
 * @author Ionescu Raul-Andrei
 * @version 17 Decembrie 2025
 */

package com.raul.univ_management.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

/** Clasa model pentru entitatea Sala
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Sala {
    private long id;
    private long facultateId;
    private String nume;
    private String tip;
    private int capacitate;
}

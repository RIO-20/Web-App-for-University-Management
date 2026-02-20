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
/** Clasa model pentru entitatea Sponsor
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Sponsor {
    private long id;
    private String nume;
    private String  descriere;
    private String  email;
}

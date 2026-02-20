/** Clasa pentru tabelul de Facultati
 * @author Ionescu Raul-Andrei
 * @version 17 Decembrie 2025
 */

package com.raul.univ_management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data   //Folosit pentru a genera automat getters, setters, toString,equals si hashcode
@NoArgsConstructor //Creeaza un constructor default
@AllArgsConstructor //Creeza un constructor care seteaza toti parametrii
@Builder
/** Clasa model pentru entitatea Facultate
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public class Facultate {
    private long id;
    private long adminId;
    private String nume;
    private String descriere;
    private BigDecimal buget;
}

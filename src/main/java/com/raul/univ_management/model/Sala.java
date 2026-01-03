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

public class Sala {
    private long id;
    private long facultateId;
    private String nume;
    private String tip;
    private int capacitate;
}

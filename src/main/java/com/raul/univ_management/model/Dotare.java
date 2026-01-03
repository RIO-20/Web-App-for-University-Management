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

public class Dotare {
    private long id;
    private long salaId;
    private String nume;
    private BigDecimal valoare;
    private String stare;
}

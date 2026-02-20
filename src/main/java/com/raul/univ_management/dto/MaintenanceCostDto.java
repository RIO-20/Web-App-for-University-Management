package com.raul.univ_management.dto;

import java.math.BigDecimal;

/** Clasa DTO pentru transferul datelor MaintenanceCost
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record MaintenanceCostDto(
    Long roomId,                   
    String roomName,
    BigDecimal replacementCost,    
    BigDecimal repairCost,         
    String recommendation          
) {}

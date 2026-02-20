package com.raul.univ_management.dto;

import java.math.BigDecimal;
import java.util.List;

/** Clasa DTO pentru transferul datelor EquipmentWithHistory
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record EquipmentWithHistoryDto(
    Long id,
    String name,
    String state,
    BigDecimal value,
    int quantity, 
    List<TransactionHistoryDto> history
) {
    public record TransactionHistoryDto(
        Long id,
        String type,           
        BigDecimal amount,
        String description
    ) {}
}

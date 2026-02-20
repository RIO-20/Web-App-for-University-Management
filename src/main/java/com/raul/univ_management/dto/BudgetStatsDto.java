package com.raul.univ_management.dto;

import java.math.BigDecimal;

/** Clasa DTO pentru transferul datelor Buget
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record BudgetStatsDto(
    BigDecimal initialBudget,
    BigDecimal totalSpent,
    BigDecimal remainingBudget
) {}

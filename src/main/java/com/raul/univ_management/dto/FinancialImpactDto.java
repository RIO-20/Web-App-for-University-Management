package com.raul.univ_management.dto;

import java.math.BigDecimal;

/** Clasa DTO pentru transferul datelor FinancialImpact
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record FinancialImpactDto(
    String transactionType,
    BigDecimal totalValue,
    String financialImpact
) {}

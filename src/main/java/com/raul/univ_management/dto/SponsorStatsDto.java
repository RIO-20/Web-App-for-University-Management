package com.raul.univ_management.dto;

import java.math.BigDecimal;

/** Clasa DTO pentru transferul datelor SponsorStats
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record SponsorStatsDto(
    Long id,
    String name,
    String email,
    BigDecimal totalDonated,
    int donationCount
) {}

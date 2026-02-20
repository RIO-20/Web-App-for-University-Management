package com.raul.univ_management.dto;

import java.math.BigDecimal;

/** Clasa DTO pentru transferul datelor RoomEfficiency
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record RoomEfficiencyDto(
    String roomName,
    int capacity,
    BigDecimal totalValue,
    BigDecimal valuePerSeat,
    String status
) {}

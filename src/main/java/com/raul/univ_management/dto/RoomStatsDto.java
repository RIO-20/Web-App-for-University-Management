package com.raul.univ_management.dto;

/** Clasa DTO pentru transferul datelor RoomStats
* @author Ionescu Raul-Andrei
* @version 12 Ianuarie 2026
*/
public record RoomStatsDto(
    String roomName,
    int totalEquipment,
    int functionalCount,
    int brokenCount,
    int oldCount
) {}

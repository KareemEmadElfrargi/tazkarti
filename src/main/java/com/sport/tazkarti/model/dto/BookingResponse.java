package com.sport.tazkarti.model.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        String ticketCode,
        String fanId,   //So he knows it's in his name
        String matchInfo,
        String stadium,
        Integer seatNumber,
        LocalDateTime bookingDate
) {}
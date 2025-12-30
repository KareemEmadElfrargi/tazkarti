package com.sport.tazkarti.model.dto;

import java.time.LocalDateTime;

public record BookingResponse(
        String ticketCode,
        String fanId,
        MatchResponse match,
        Integer seatNumber,
        LocalDateTime bookingDate
) {}
package com.sport.tazkarti.mapper;

import org.springframework.stereotype.Component;

import com.sport.tazkarti.model.Ticket;
import com.sport.tazkarti.model.dto.BookingResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TicketMapper {
    
    private final MatchMapper matchMapper;

    public BookingResponse toResponse(Ticket ticket) {
        return new BookingResponse(
                ticket.getTicketCode(),
                ticket.getUser().getFanId(),
                matchMapper.toResponse(ticket.getMatch()),
                ticket.getSeatNumber(),
                ticket.getBookingDate()
        );
    }
}
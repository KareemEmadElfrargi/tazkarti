package com.sport.tazkarti.mapper;

import com.sport.tazkarti.model.Ticket;
import com.sport.tazkarti.model.dto.BookingResponse;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public BookingResponse toResponse(Ticket ticket) {
        return new BookingResponse(
                ticket.getTicketCode(),
                ticket.getUser().getFanId(),
                ticket.getMatch().getHomeTeam().getName() + " vs " + ticket.getMatch().getAwayTeam().getName(),
                ticket.getMatch().getStadium().getName(),
                ticket.getSeatNumber(),
                ticket.getBookingDate()
        );
    }
}
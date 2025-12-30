package com.sport.tazkarti.service;


import com.sport.tazkarti.exception.DuplicateRecordException;
import com.sport.tazkarti.exception.ResourceNotFoundException;
import com.sport.tazkarti.mapper.TicketMapper;
import com.sport.tazkarti.model.AppUser;
import com.sport.tazkarti.model.Match;
import com.sport.tazkarti.model.Ticket;
import com.sport.tazkarti.model.dto.BookingRequest;
import com.sport.tazkarti.model.dto.BookingResponse;
import com.sport.tazkarti.repository.AppUserRepository;
import com.sport.tazkarti.repository.MatchRepository;
import com.sport.tazkarti.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final AppUserRepository appUserRepository;
    private final MatchRepository matchRepository;

    private final TicketMapper ticketMapper;

    @Transactional
    public BookingResponse bookTicket(BookingRequest request, String userEmail){
        AppUser user = appUserRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Match match = matchRepository.findById(request.matchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));

        if (ticketRepository.existsByUserIdAndMatchId(user.getId(), match.getId())) {
            throw new DuplicateRecordException("You have already booked a ticket for this match!");
        }

        Long soldTickets = ticketRepository.countByMatchId(match.getId());
        if (soldTickets >= match.getStadium().getCapacity()){
            throw new IllegalStateException("Sorry, this match is sold out!");
        }
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setMatch(match);
        ticket.setSeatNumber((int)(soldTickets+1));
        Ticket savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toResponse(savedTicket);
    }
    public List<BookingResponse> getTickets(String email){
        return ticketRepository.findAllByUserEmail(email)
                .stream().map(ticketMapper::toResponse).toList();
    }

}

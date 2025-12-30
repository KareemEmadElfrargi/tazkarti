package com.sport.tazkarti.controller;

import com.sport.tazkarti.model.dto.BookingRequest;
import com.sport.tazkarti.model.dto.BookingResponse;
import com.sport.tazkarti.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<BookingResponse> bookTicket(@Valid @RequestBody BookingRequest bookingRequest, Principal principal){
        return ResponseEntity.ok(ticketService.bookTicket(bookingRequest,principal.getName()));
    }
    @GetMapping
    public ResponseEntity<?> getTickets(Principal principal){
        return ResponseEntity.ok(ticketService.getTickets(principal.getName()));
    }

}

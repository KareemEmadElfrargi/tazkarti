package com.sport.tazkarti.repository;

import com.sport.tazkarti.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Count the number of tickets booked for a given match
    Long countByMatchId(Long matchId);

    // Has this user booked this match before?
    boolean existsByUserIdAndMatchId(Long userId, Long matchId);

    // Bring all the tickets for this user
    List<Ticket> findAllByUserEmail(String email);

}

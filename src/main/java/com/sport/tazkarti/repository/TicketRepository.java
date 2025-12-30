package com.sport.tazkarti.repository;

import com.sport.tazkarti.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // Count the number of tickets booked for a given match
    Long countByMatchId(Long matchId);

    // Has this user booked this match before?
    boolean existsByUserIdAndMatchId(Long userId, Long matchId);

}

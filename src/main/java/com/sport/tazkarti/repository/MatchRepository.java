package com.sport.tazkarti.repository;

import com.sport.tazkarti.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
    //we may then need to search for the matches that are still coming
    // List<Match> findByMatchTimeAfter(LocalDateTime now);
}

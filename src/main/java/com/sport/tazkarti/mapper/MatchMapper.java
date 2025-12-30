package com.sport.tazkarti.mapper;

import com.sport.tazkarti.model.Match;
import com.sport.tazkarti.model.dto.MatchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MatchMapper {
    private final TeamMapper teamMapper;
    private final StadiumMapper stadiumMapper;

    public MatchResponse toResponse(Match match) {
        return new MatchResponse(
                match.getId(),
                teamMapper.toResponse(match.getHomeTeam()),
                teamMapper.toResponse(match.getAwayTeam()),
                stadiumMapper.toResponse(match.getStadium()),
                match.getMatchTime(),
                match.getTicketPrice()
        );
    }
}

package com.sport.tazkarti.service;

import com.sport.tazkarti.mapper.MatchMapper;
import com.sport.tazkarti.model.Match;
import com.sport.tazkarti.model.Stadium;
import com.sport.tazkarti.model.Team;
import com.sport.tazkarti.model.dto.MatchRequest;
import com.sport.tazkarti.model.dto.MatchResponse;
import com.sport.tazkarti.repository.MatchRepository;
import com.sport.tazkarti.repository.StadiumRepository;
import com.sport.tazkarti.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final TeamRepository teamRepository;
    private final StadiumRepository stadiumRepository;

    public List<MatchResponse> getMatches(){
        return matchRepository.findAll().stream().map(matchMapper::toResponse).toList();
    }

    public MatchResponse addMatch(MatchRequest request){
        if (request.homeTeamId().equals(request.awayTeamId())){
            throw new IllegalArgumentException("Home team and Away team cannot be the same!");
        }
        Team homeTeam = teamRepository.findById(request.homeTeamId())
                .orElseThrow(() -> new RuntimeException("Home team not found!"));
        Team awayTeam = teamRepository.findById(request.awayTeamId())
                .orElseThrow(() -> new RuntimeException("Away team not found!"));

        Stadium stadium = stadiumRepository.findById(request.stadiumId())
                .orElseThrow(() -> new RuntimeException("Stadium not found!"));

        Match match = new Match();
        match.setHomeTeam(homeTeam);
        match.setAwayTeam(awayTeam);
        match.setStadium(stadium);
        match.setMatchTime(request.matchTime());
        match.setTicketPrice(request.ticketPrice());

        Match savedMatch = matchRepository.save(match);
        return matchMapper.toResponse(savedMatch);
    }


}

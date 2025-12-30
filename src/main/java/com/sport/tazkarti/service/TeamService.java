package com.sport.tazkarti.service;

import com.sport.tazkarti.exception.DuplicateRecordException;
import com.sport.tazkarti.model.Team;
import com.sport.tazkarti.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team addTeam(Team team) {
        if (teamRepository.existsByName(team.getName())) {
            throw new DuplicateRecordException("Team already exists: " + team.getName());
        }
        return teamRepository.save(team);
    }

}

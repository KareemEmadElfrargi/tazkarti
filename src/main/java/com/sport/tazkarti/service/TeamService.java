package com.sport.tazkarti.service;

import com.sport.tazkarti.exception.DuplicateRecordException;
import com.sport.tazkarti.mapper.TeamMapper;
import com.sport.tazkarti.model.Team;
import com.sport.tazkarti.model.dto.TeamRequest;
import com.sport.tazkarti.model.dto.TeamResponse;
import com.sport.tazkarti.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;

    public List<TeamResponse> getAllTeams() {
        return teamRepository.findAll().stream().map(teamMapper::toResponse
        ).toList();
    }

    public TeamResponse addTeam(TeamRequest request) {
        if (teamRepository.existsByName(request.name())) {
            throw new DuplicateRecordException("Team already exists: " + request.name());
        }
        Team team = teamMapper.toEntity(request);
        Team savedTeam = teamRepository.save(team);
        return teamMapper.toResponse(savedTeam);
    }

}

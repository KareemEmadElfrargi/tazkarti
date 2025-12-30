package com.sport.tazkarti.mapper;

import com.sport.tazkarti.model.Team;
import com.sport.tazkarti.model.dto.TeamRequest;
import com.sport.tazkarti.model.dto.TeamResponse;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public Team toEntity(TeamRequest request) {
        Team team = new Team();
        team.setName(request.name());
        team.setLogoUrl(request.logoUrl());
        team.setDescription(request.description());
        return team;
    }

    public TeamResponse toResponse(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getLogoUrl(),
                team.getDescription()
        );
    }
}

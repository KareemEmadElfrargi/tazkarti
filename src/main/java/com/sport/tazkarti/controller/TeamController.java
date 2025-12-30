package com.sport.tazkarti.controller;

import com.sport.tazkarti.model.Team;
import com.sport.tazkarti.model.dto.TeamRequest;
import com.sport.tazkarti.model.dto.TeamResponse;
import com.sport.tazkarti.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams() {
        return ResponseEntity.ok(teamService.getAllTeams());
    }

    @PostMapping
    public ResponseEntity<TeamResponse> addTeam(@Valid @RequestBody TeamRequest request) {
        return ResponseEntity.ok(teamService.addTeam(request));
    }

}

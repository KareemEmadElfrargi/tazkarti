package com.sport.tazkarti.controller;

import com.sport.tazkarti.model.dto.MatchRequest;
import com.sport.tazkarti.model.dto.MatchResponse;
import com.sport.tazkarti.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<MatchResponse>> getMatches() {
        return ResponseEntity.ok(matchService.getMatches());
    }

    @PostMapping
    public ResponseEntity<MatchResponse> addMatch(@Valid @RequestBody MatchRequest request) {
        return ResponseEntity.ok(matchService.addMatch(request));
    }
}

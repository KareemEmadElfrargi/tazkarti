package com.sport.tazkarti.controller;

import com.sport.tazkarti.model.Stadium;
import com.sport.tazkarti.model.dto.StadiumRequest;
import com.sport.tazkarti.model.dto.StadiumResponse;
import com.sport.tazkarti.service.StadiumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stadiums")
@RequiredArgsConstructor
public class StadiumController {
    private final StadiumService stadiumService;

    @GetMapping
    public ResponseEntity<List<StadiumResponse>> getAllStadiums() {
        return ResponseEntity.ok(stadiumService.getStadiums());
    }

    @PostMapping
    public ResponseEntity<StadiumResponse> addStadium(@Valid @RequestBody StadiumRequest request) {
        return ResponseEntity.ok(stadiumService.addStadium(request));
    }

}

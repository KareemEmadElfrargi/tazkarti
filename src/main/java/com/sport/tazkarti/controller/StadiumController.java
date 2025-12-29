package com.sport.tazkarti.controller;

import com.sport.tazkarti.model.Stadium;
import com.sport.tazkarti.service.StadiumService;
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
    public ResponseEntity<List<Stadium>> getAllStadiums() {
        return ResponseEntity.ok(stadiumService.getStadiums());
    }

    @PostMapping
    public ResponseEntity<Stadium> addStadium(@RequestBody Stadium stadium) {
        return ResponseEntity.ok(stadiumService.addStadium(stadium));
    }

}

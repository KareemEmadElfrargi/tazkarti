package com.sport.tazkarti.controller;

import com.sport.tazkarti.model.AppUser;
import com.sport.tazkarti.model.dto.RegisterRequest;
import com.sport.tazkarti.model.dto.UserResponse;
import com.sport.tazkarti.service.AppUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
            UserResponse savedUser = appUserService.registerUser(request);
            return ResponseEntity.ok(savedUser);
    }
}

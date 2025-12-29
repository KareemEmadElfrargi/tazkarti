package com.sport.tazkarti.controller;

import com.sport.tazkarti.model.dto.LoginRequest;
import com.sport.tazkarti.model.dto.RegisterRequest;
import com.sport.tazkarti.model.dto.UserResponse;
import com.sport.tazkarti.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequest request) {
            UserResponse savedUser = userService.registerUser(request);
            return ResponseEntity.ok(savedUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(Principal principal) {
        UserResponse user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(user);
    }
}

package com.sport.tazkarti.service;

import com.sport.tazkarti.model.enums.Role;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sport.tazkarti.config.JwtService;
import com.sport.tazkarti.exception.DuplicateRecordException;
import com.sport.tazkarti.mapper.UserMapper;
import com.sport.tazkarti.model.AppUser;
import com.sport.tazkarti.model.dto.LoginRequest;
import com.sport.tazkarti.model.dto.LoginResponse;
import com.sport.tazkarti.model.dto.RegisterRequest;
import com.sport.tazkarti.model.dto.UserResponse;
import com.sport.tazkarti.repository.AppUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AppUserRepository appUserRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserResponse registerUser(RegisterRequest request) {

        if (appUserRepository.existsByEmail(request.email())) {
            throw new DuplicateRecordException("Email already taken: " + request.email());
        }
        if (appUserRepository.existsByFanId(request.fanId())) {
            throw new DuplicateRecordException("Fan ID already exists: " + request.fanId());
        }

        AppUser appUser = new AppUser();
        appUser.setEmail(request.email());
        appUser.setPassword(passwordEncoder.encode(request.password()));
        appUser.setFanId(request.fanId());
        appUser.setRole(Role.USER);
        appUser.setUsername(request.username());
        
        appUserRepository.save(appUser);

        return userMapper.toDtoResponse(appUser);
    }

    public LoginResponse login(LoginRequest request) {
        AppUser user = appUserRepository.findByEmail(request.email())
                .orElseThrow(()-> new BadCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(),user.getPassword())) {
            throw new BadCredentialsException("Invalid Email or Password");
        }

        String token =  jwtService.generateToken(user.getEmail());

        return new LoginResponse(token,user.getEmail(),user.getFanId());
    }

    public UserResponse getUserByEmail(String email) {
        AppUser user = appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return userMapper.toDtoResponse(user);
    }
}

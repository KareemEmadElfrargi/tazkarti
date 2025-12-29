package com.sport.tazkarti.service;

import com.sport.tazkarti.exception.DuplicateRecordException;
import com.sport.tazkarti.mapper.UserMapper;
import com.sport.tazkarti.model.AppUser;
import com.sport.tazkarti.model.dto.RegisterRequest;
import com.sport.tazkarti.model.dto.UserResponse;
import com.sport.tazkarti.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

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
        appUserRepository.save(appUser);

        return userMapper.toDtoResponse(appUser);
    }
}

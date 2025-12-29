package com.sport.tazkarti.service;

import com.sport.tazkarti.model.AppUser;
import com.sport.tazkarti.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUser registerUser(AppUser user) {

        if (appUserRepository.existsByEmail(user.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        if (appUserRepository.existsByFanId(user.getFanId())) {
            throw new RuntimeException("Fan ID already exists");
        }
        return appUserRepository.save(user);
    }
}

package com.sport.tazkarti.service;

import com.sport.tazkarti.exception.DuplicateRecordException;
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
            throw new DuplicateRecordException("Email already taken: " + user.getEmail());
        }
        if (appUserRepository.existsByFanId(user.getFanId())) {
            throw new DuplicateRecordException("Fan ID already exists: " + user.getFanId());
        }
        return appUserRepository.save(user);
    }
}

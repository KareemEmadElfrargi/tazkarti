package com.sport.tazkarti.service;

import com.sport.tazkarti.exception.DuplicateRecordException;
import com.sport.tazkarti.mapper.UserMapper;
import com.sport.tazkarti.model.AppUser;
import com.sport.tazkarti.model.dto.RegisterRequest;
import com.sport.tazkarti.model.dto.UserResponse;
import com.sport.tazkarti.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;
    private final UserMapper userMapper;

    public UserResponse registerUser(RegisterRequest request) {

        if (appUserRepository.existsByEmail(request.email())){
            throw new DuplicateRecordException("Email already taken: " + request.email());
        }
        if (appUserRepository.existsByFanId(request.fanId())) {
            throw new DuplicateRecordException("Fan ID already exists: " + request.fanId());
        }
        AppUser user = userMapper.toEntity(request);
        appUserRepository.save(user);
        return userMapper.toDtoResponse(user);
    }
}

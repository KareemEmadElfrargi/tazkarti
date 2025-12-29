package com.sport.tazkarti.mapper;

import com.sport.tazkarti.model.AppUser;
import com.sport.tazkarti.model.dto.RegisterRequest;
import com.sport.tazkarti.model.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public AppUser toEntity(RegisterRequest request){
        AppUser user = new AppUser();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFanId(request.fanId());
        user.setPassword(request.password());
        return user;
    }

    public UserResponse toDtoResponse(AppUser user){
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFanId()
        );
    }

}

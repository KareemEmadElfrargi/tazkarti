package com.sport.tazkarti.repository;

import com.sport.tazkarti.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findByFanId(String fanId);

    boolean existsByEmail(String email);
    boolean existsByFanId(String fanId);
}

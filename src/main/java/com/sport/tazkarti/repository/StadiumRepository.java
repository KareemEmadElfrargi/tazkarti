package com.sport.tazkarti.repository;

import com.sport.tazkarti.model.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    //We will need this to make sure that we do not add the same stadium twice
    boolean existsByName(String name);
}

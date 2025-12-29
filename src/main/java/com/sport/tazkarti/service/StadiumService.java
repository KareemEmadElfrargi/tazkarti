package com.sport.tazkarti.service;

import com.sport.tazkarti.exception.DuplicateRecordException;
import com.sport.tazkarti.model.Stadium;
import com.sport.tazkarti.repository.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StadiumService {
    private final StadiumRepository stadiumRepository;

    public List<Stadium> getStadiums() {
        return stadiumRepository.findAll();
    }

    public Stadium addStadium(Stadium stadium) {
        if (stadiumRepository.existsByName(stadium.getName())) {
            throw new DuplicateRecordException("Stadium with this name already exists!");
        }
        return stadiumRepository.save(stadium);
    }
}

package com.sport.tazkarti.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sport.tazkarti.exception.DuplicateRecordException;
import com.sport.tazkarti.mapper.StadiumMapper;
import com.sport.tazkarti.model.Stadium;
import com.sport.tazkarti.model.dto.StadiumRequest;
import com.sport.tazkarti.model.dto.StadiumResponse;
import com.sport.tazkarti.repository.StadiumRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StadiumService {
    private final StadiumRepository stadiumRepository;
    private final StadiumMapper stadiumMapper;

    public List<StadiumResponse> getStadiums() {
        return stadiumRepository.findAll().stream().map(stadiumMapper::toResponse).toList();
    }

    public StadiumResponse addStadium(StadiumRequest request) {
        if (stadiumRepository.existsByName(request.name())) {
            throw new DuplicateRecordException("Stadium with this name already exists!");
        }
        Stadium stadium = stadiumMapper.toEntity(request);
        Stadium savedStadium = stadiumRepository.save(stadium);
        return stadiumMapper.toResponse(savedStadium);
    }
}

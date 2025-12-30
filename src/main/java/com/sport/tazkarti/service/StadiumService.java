package com.sport.tazkarti.service;

import com.sport.tazkarti.exception.DuplicateRecordException;
import com.sport.tazkarti.mapper.StadiumMapper;
import com.sport.tazkarti.model.Stadium;
import com.sport.tazkarti.model.dto.StadiumRequest;
import com.sport.tazkarti.model.dto.StadiumResponse;
import com.sport.tazkarti.repository.StadiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        Stadium stadium = new Stadium();
        Stadium savedStadium = stadiumRepository.save(stadium);
        return stadiumMapper.toResponse(savedStadium);
    }
}

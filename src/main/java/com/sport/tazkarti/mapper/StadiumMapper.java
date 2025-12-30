package com.sport.tazkarti.mapper;

import com.sport.tazkarti.model.Stadium;
import com.sport.tazkarti.model.dto.StadiumRequest;
import com.sport.tazkarti.model.dto.StadiumResponse;
import org.springframework.stereotype.Component;

@Component
public class StadiumMapper {

    public Stadium toEntity(StadiumRequest request) {
        Stadium stadium = new Stadium();
        stadium.setName(request.name());
        stadium.setCity(request.city());
        stadium.setCapacity(request.capacity());
        stadium.setActive(true);
        return stadium;
    }

    public StadiumResponse toResponse(Stadium stadium) {
        return new StadiumResponse(
                stadium.getId(),
                stadium.getName(),
                stadium.getCity(),
                stadium.getCapacity(),
                stadium.isActive()
        );
    }
}

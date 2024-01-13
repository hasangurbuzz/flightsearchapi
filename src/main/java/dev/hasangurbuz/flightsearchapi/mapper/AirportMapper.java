package dev.hasangurbuz.flightsearchapi.mapper;

import dev.hasangurbuz.flightsearchapi.domain.Airport;
import org.openapitools.model.AirportDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AirportMapper {

    public AirportDTO toDto(Airport airport) {
        AirportDTO dto = new AirportDTO();
        dto.setId(airport.getId());
        dto.setCity(airport.getCity());
        return dto;
    }

    public List<AirportDTO> toDtoList(List<Airport> airports) {
        List<AirportDTO> dtoList = new ArrayList<>(airports.size());
        if (airports.isEmpty()) {
            return dtoList;
        }

        for (Airport airport : airports) {
            AirportDTO dto = toDto(airport);
            dtoList.add(dto);
        }

        return dtoList;
    }
}

package dev.hasangurbuz.flightsearchapi.mapper;

import dev.hasangurbuz.flightsearchapi.domain.Flight;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.AirportDTO;
import org.openapitools.model.FlightDTO;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FlightMapper {

    private final AirportMapper airportMapper;

    public FlightDTO toDto(Flight flight) {
        FlightDTO dto = new FlightDTO();
        AirportDTO departureAirport = airportMapper.toDto(flight.getDepartureAirport());
        AirportDTO arrivalAirport = airportMapper.toDto(flight.getArrivalAirport());
        dto.setId(flight.getId());
        dto.setDepartureAirport(departureAirport);
        dto.setArrivalAirport(arrivalAirport);
        dto.setPrice(flight.getPrice());
        dto.setDepartureDate(flight.getDepartureDate());
        return dto;

    }

    public List<FlightDTO> toDtoList(List<Flight> flightList) {
        List<FlightDTO> dtoList = new ArrayList<>(flightList.size());
        if (flightList.isEmpty()) {
            return dtoList;
        }

        for (Flight flight : flightList) {
            FlightDTO dto = toDto(flight);
            dtoList.add(dto);
        }

        return dtoList;
    }
}

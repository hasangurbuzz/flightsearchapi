package dev.hasangurbuz.flightsearchapi.mapper;

import dev.hasangurbuz.flightsearchapi.domain.Flight;
import dev.hasangurbuz.flightsearchapi.helper.DateHelper;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FlightMapper {

    private final AirportMapper airportMapper;
    private final PriceMapper priceMapper;


    public FlightDTO toDto(Flight flight) {
        FlightDTO dto = new FlightDTO();
        AirportDTO departureAirport = airportMapper.toDto(flight.getOrigin());
        AirportDTO arrivalAirport = airportMapper.toDto(flight.getDestination());
        PriceDTO price = priceMapper.toDto(flight.getPrice());
        dto.setOrigin(departureAirport);
        dto.setDestination(arrivalAirport);
        dto.setDepartureDate(flight.getDepartureDate());
        dto.setPrice(price);
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

package dev.hasangurbuz.flightsearchapi.mapper;

import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.domain.Flight;
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

    public JourneyDTO toJourney(Flight flight) {
        boolean isOneway = flight.getReturnDate() == null;
        JourneyDTO journey = new JourneyDTO();
        PriceDTO price = new PriceDTO();
        JourneyTypeDTO journeyType = isOneway ? JourneyTypeDTO.OUTBOUND : JourneyTypeDTO.ROUND_TRIP;

        price.setCurrency(flight.getPrice().getCurrency().name());
        price.setAmount(flight.getPrice().getAmount());

        journey.setOutbound(toDto(flight));
        journey.setId(flight.getId());
        journey.setPrice(price);
        journey.setType(journeyType);

        if (!isOneway) {
            Airport destination = flight.getDestination();
            flight.setDepartureDate(flight.getReturnDate());
            flight.setDestination(flight.getOrigin());
            flight.setOrigin(destination);
            journey.setInbound(toDto(flight));
            journey.getPrice().setAmount(price.getAmount().add(flight.getPrice().getAmount()));
        }
        return journey;
    }

    public List<JourneyDTO> toJourneyList(List<Flight> flightList) {
        List<JourneyDTO> dtoList = new ArrayList<>(flightList.size());
        if (flightList.isEmpty()) {
            return dtoList;
        }

        for (Flight flight : flightList) {
            JourneyDTO dto = toJourney(flight);
            dtoList.add(dto);
        }

        return dtoList;
    }

}

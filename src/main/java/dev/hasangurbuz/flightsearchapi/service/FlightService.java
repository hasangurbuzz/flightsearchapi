package dev.hasangurbuz.flightsearchapi.service;

import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.domain.Flight;
import org.openapitools.model.PageRequestDTO;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Date;

public interface FlightService {
    Flight create(Flight flight);

    PagedResult<Flight> search(Airport departureAirport,
                               Airport arrivalAirport,
                               LocalDate departureDate,
                               PageRequestDTO pageRequest);

}

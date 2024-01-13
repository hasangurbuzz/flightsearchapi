package dev.hasangurbuz.flightsearchapi.service;

import dev.hasangurbuz.flightsearchapi.domain.Flight;
import org.openapitools.model.FlightRequestDTO;
import org.openapitools.model.FlightSearchRequestDTO;

public interface FlightService {
    Flight create(FlightRequestDTO flightRequest);

    PagedResult<Flight> search(FlightSearchRequestDTO searchRequest);

    Flight findById(Long id);

    Flight update(Long id, FlightRequestDTO flightRequest);

    boolean existingFlightByAirportId(Long originId);

    void delete(Flight flight);

}

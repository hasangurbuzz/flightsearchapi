package dev.hasangurbuz.flightsearchapi.service;

import dev.hasangurbuz.flightsearchapi.domain.Airport;
import org.openapitools.model.AirportRequestDTO;
import org.openapitools.model.PageRequestDTO;

public interface AirportService {
    Airport create(AirportRequestDTO createRequest);

    Airport findById(Long id);

    PagedResult<Airport> search(String term, PageRequestDTO pageRequest);

    Airport update(Long id, AirportRequestDTO updateRequest);

    void delete(Airport airport);


}

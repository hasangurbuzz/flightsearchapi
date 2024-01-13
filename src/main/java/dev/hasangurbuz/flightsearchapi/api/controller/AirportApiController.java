package dev.hasangurbuz.flightsearchapi.api.controller;

import dev.hasangurbuz.flightsearchapi.api.ApiUtil;
import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.exception.ExistsException;
import dev.hasangurbuz.flightsearchapi.mapper.AirportMapper;
import dev.hasangurbuz.flightsearchapi.service.AirportService;
import dev.hasangurbuz.flightsearchapi.service.FlightService;
import dev.hasangurbuz.flightsearchapi.service.PagedResult;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.AirportApi;
import org.openapitools.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AirportApiController implements AirportApi {

    private final AirportService airportService;
    private final AirportMapper airportMapper;
    private final FlightService flightService;

    @Override
    public ResponseEntity<AirportSearchResponseDTO> search(AirportSearchRequestDTO airportSearchRequestDTO) {
        PageRequestDTO pageRequest = ApiUtil.normalizePageRequest(airportSearchRequestDTO.getPageRequest());
        String term = airportSearchRequestDTO.getTerm();
        if (term == null) {
            term = "";
        }
        term = term.trim();
        PagedResult<Airport> searchResult = airportService.search(term, pageRequest);
        List<AirportDTO> airportDTOS = airportMapper.toDtoList(searchResult.getItems());

        AirportSearchResponseDTO response = new AirportSearchResponseDTO();
        response.setItems(airportDTOS);
        response.setTotal(searchResult.getTotal());

        return ResponseEntity.ok(response);
    }

    @Transactional
    @Override
    public ResponseEntity<AirportDTO> create(AirportRequestDTO airportCreateRequestDTO) {
        Airport airport = airportService.create(airportCreateRequestDTO);
        return ResponseEntity.ok(airportMapper.toDto(airport));
    }

    @Override
    public ResponseEntity<AirportDTO> getById(Long airportId) {
        Airport airport = airportService.findById(airportId);
        return ResponseEntity.ok(airportMapper.toDto(airport));
    }

    @Override
    public ResponseEntity<AirportDTO> update(Long airportId, AirportRequestDTO airportRequestDTO) {
        Airport airport = airportService.update(airportId, airportRequestDTO);
        return ResponseEntity.ok(airportMapper.toDto(airport));
    }

    @Override
    public ResponseEntity<Void> delete(Long airportId) {
        Airport airport = airportService.findById(airportId);
        boolean existingAirport = flightService.existingFlightByAirportId(airport.getId());
        if (existingAirport) {
            throw new ExistsException("Could not perform delete because there are pending flights on this airport : "
                    + airport.getId());
        }
        airportService.delete(airport);
        return ResponseEntity.ok().build();
    }
}

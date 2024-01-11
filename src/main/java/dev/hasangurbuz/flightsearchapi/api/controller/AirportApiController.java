package dev.hasangurbuz.flightsearchapi.api.controller;

import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.mapper.AirportMapper;
import dev.hasangurbuz.flightsearchapi.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.AirportApi;
import org.openapitools.model.AirportCreateRequestDTO;
import org.openapitools.model.AirportDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AirportApiController implements AirportApi {

    private final AirportService airportService;
    private final AirportMapper airportMapper;

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<List<AirportDTO>> get() {
        List<Airport> airports = airportService.getAll();
        return ResponseEntity.ok(airportMapper.toDtoList(airports));
    }

    @Transactional
    @Override
    public ResponseEntity<AirportDTO> create(AirportCreateRequestDTO airportCreateRequestDTO) {
        Airport airport = new Airport();
        airport.setCity(airportCreateRequestDTO.getCity());
        airport = airportService.create(airport);
        return ResponseEntity.ok(airportMapper.toDto(airport));
    }
}

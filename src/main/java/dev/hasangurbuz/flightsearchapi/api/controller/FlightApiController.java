package dev.hasangurbuz.flightsearchapi.api.controller;

import dev.hasangurbuz.flightsearchapi.api.ApiUtil;
import dev.hasangurbuz.flightsearchapi.domain.Flight;
import dev.hasangurbuz.flightsearchapi.mapper.FlightMapper;
import dev.hasangurbuz.flightsearchapi.service.FlightService;
import dev.hasangurbuz.flightsearchapi.service.PagedResult;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.FlightApi;
import org.openapitools.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightApiController implements FlightApi {

    private final FlightService flightService;
    private final FlightMapper flightMapper;


    @Transactional
    @Override
    public ResponseEntity<JourneyDTO> create(FlightRequestDTO flightCreateRequestDTO) {
        Flight flight = flightService.create(flightCreateRequestDTO);
        return ResponseEntity.ok(flightMapper.toJourney(flight));
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<FlightSearchResponseDTO> search(FlightSearchRequestDTO flightSearchRequestDTO) {
        PageRequestDTO pageRequest = ApiUtil.normalizePageRequest(flightSearchRequestDTO.getPageRequest());
        flightSearchRequestDTO.setPageRequest(pageRequest);

        PagedResult<Flight> searchResult = flightService.search(flightSearchRequestDTO);

        List<JourneyDTO> journeyList = flightMapper.toJourneyList(searchResult.getItems());

        FlightSearchResponseDTO response = new FlightSearchResponseDTO();
        response.setTotal(searchResult.getTotal());
        response.setItems(journeyList);

        return ResponseEntity.ok(response);
    }

    @Transactional
    @Override
    public ResponseEntity<Void> delete(Long flightId) {
        Flight flight = flightService.findById(flightId);
        flightService.delete(flight);
        return ResponseEntity.ok().build();
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<JourneyDTO> getById(Long flightId) {
        Flight flight = flightService.findById(flightId);
        return ResponseEntity.ok(flightMapper.toJourney(flight));
    }

    @Transactional
    @Override
    public ResponseEntity<JourneyDTO> update(Long flightId, FlightRequestDTO flightRequestDTO) {
        Flight flight = flightService.update(flightId, flightRequestDTO);
        return ResponseEntity.ok(flightMapper.toJourney(flight));
    }
}

package dev.hasangurbuz.flightsearchapi.api.controller;

import dev.hasangurbuz.flightsearchapi.api.ApiException;
import dev.hasangurbuz.flightsearchapi.api.ApiUtil;
import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.domain.Flight;
import dev.hasangurbuz.flightsearchapi.domain.Price;
import dev.hasangurbuz.flightsearchapi.helper.DateHelper;
import dev.hasangurbuz.flightsearchapi.mapper.AirportMapper;
import dev.hasangurbuz.flightsearchapi.mapper.FlightMapper;
import dev.hasangurbuz.flightsearchapi.mapper.PriceMapper;
import dev.hasangurbuz.flightsearchapi.service.AirportService;
import dev.hasangurbuz.flightsearchapi.service.FlightService;
import dev.hasangurbuz.flightsearchapi.service.PagedResult;
import lombok.RequiredArgsConstructor;
import org.openapitools.api.FlightApi;
import org.openapitools.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FlightApiController implements FlightApi {

    private final FlightService flightService;
    private final AirportService airportService;
    private final FlightMapper flightMapper;
    private final AirportMapper airportMapper;
    private final PriceMapper priceMapper;


    @Transactional
    @Override
    public ResponseEntity<FlightDTO> create(FlightCreateRequestDTO flightCreateRequestDTO) {
        Airport originAirport = airportService.findById(flightCreateRequestDTO.getDepartureAirportId());
        if (originAirport == null) {
            throw ApiException.notFound("Not found airport with id: " + flightCreateRequestDTO.getDepartureAirportId());
        }

        Airport destinationAirport = airportService.findById(flightCreateRequestDTO.getArrivalAirportId());
        if (destinationAirport == null) {
            throw ApiException.notFound("Not found airport with id: " + flightCreateRequestDTO.getArrivalAirportId());
        }

        Flight flight = new Flight();
        Price price = priceMapper.toEntity(flightCreateRequestDTO.getPrice());
        flight.setPrice(price);
        flight.setOrigin(originAirport);
        flight.setDestination(destinationAirport);
        flight.setDepartureDate(DateHelper.toUTC(flightCreateRequestDTO.getDepartureDate()));
        flight.setReturnDate(DateHelper.toUTC(flightCreateRequestDTO.getReturnDate()));

        flight = flightService.create(flight);

        return ResponseEntity.ok(flightMapper.toDto(flight));
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<FlightSearchResponseDTO> search(FlightSearchRequestDTO flightSearchRequestDTO) {
        PageRequestDTO pageRequest = ApiUtil.normalizePageRequest(flightSearchRequestDTO.getPageRequest());
        boolean isOneway = flightSearchRequestDTO.getReturnDate() == null;

        Airport departureAirport = airportService.findById(flightSearchRequestDTO.getDepartureAirportId());
        if (departureAirport == null) {
            throw ApiException.notFound("Not found airport with id : " + flightSearchRequestDTO.getDepartureAirportId());
        }

        Airport arrivalAirport = airportService.findById(flightSearchRequestDTO.getArrivalAirportId());
        if (departureAirport == null) {
            throw ApiException.notFound("Not found airport with id : " + flightSearchRequestDTO.getDepartureAirportId());
        }


        PagedResult<Flight> flights = flightService.search(departureAirport,
                arrivalAirport,
                flightSearchRequestDTO.getDepartureDate(),
                flightSearchRequestDTO.getReturnDate(),
                pageRequest);

        List<JourneyDTO> journeys = new ArrayList<>(flights.getItems().size());

        for (Flight flight : flights.getItems()) {
            JourneyDTO journey = new JourneyDTO();
            PriceDTO price = new PriceDTO();
            JourneyTypeDTO journeyType = isOneway ? JourneyTypeDTO.OUTBOUND : JourneyTypeDTO.ROUND_TRIP;

            price.setCurrency(CurrencyDTO.valueOf(flight.getPrice().getCurrency().name()));
            price.setAmount(flight.getPrice().getAmount().multiply(BigDecimal.valueOf(2)));

            journey.setOutbound(flightMapper.toDto(flight));
            journey.setId(flight.getId());
            journey.setPrice(price);
            journey.setType(journeyType);

            if (isOneway) {
                flight.setDepartureDate(flight.getReturnDate());
                flight.setOrigin(flight.getDestination());
                journey.setInbound(flightMapper.toDto(flight));
            }
            journeys.add(journey);
        }

        FlightSearchResponseDTO response = new FlightSearchResponseDTO();
        response.setItems(journeys);
        response.setTotal(flights.getTotal());

        return ResponseEntity.ok(response);


//        if (!isOneWay) {
//            List<RoundFlightDTO> roundFlightDTOS = new ArrayList<>(onewayResults.getItems().size());
//            for (Flight flight : onewayResults.getItems()) {
//                if (!flight.getReturnDate().toLocalDate().equals(flightSearchRequestDTO.getReturnDate())) {
//                    continue;
//                }
//
//                RoundFlightDTO dto = new RoundFlightDTO();
//                FlightDTO returnFlight = new FlightDTO();
//                returnFlight.setId(flight.getId());
//                returnFlight.setPrice(flight.getPrice());
//                returnFlight.setDepartureDate(flight.getReturnDate());
//                returnFlight.setDepartureAirport(airportMapper.toDto(flight.getArrivalAirport()));
//                returnFlight.setArrivalAirport(airportMapper.toDto(flight.getDepartureAirport()));
//                dto.setReturnFlight(returnFlight);
//                dto.setDepartureFlight(flightMapper.toDto(flight));
//                roundFlightDTOS.add(dto);
//
//
//            }
//            RoundSearchResponseDTO response = new RoundSearchResponseDTO();
//            response.setItems(roundFlightDTOS);
//            if (roundFlightDTOS.isEmpty()) {
//                response.setTotal(0);
//            } else {
//                response.setTotal(onewayResults.getTotal());
//            }
//            return ResponseEntity.ok(response);
//        }
//
//
//        OnewaySearchResponseDTO response = new OnewaySearchResponseDTO();
//        response.setTotal(onewayResults.getTotal());
//        List<FlightDTO> flightDTOList = flightMapper.toDtoList(onewayResults.getItems());
//
//        response.setItems(flightDTOList);


    }
}

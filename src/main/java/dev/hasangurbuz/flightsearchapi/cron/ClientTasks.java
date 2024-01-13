package dev.hasangurbuz.flightsearchapi.cron;

import dev.hasangurbuz.flightsearchapi.client.MockAirportApiClient;
import dev.hasangurbuz.flightsearchapi.client.MockFlightApiClient;
import dev.hasangurbuz.flightsearchapi.client.model.AirportResponse;
import dev.hasangurbuz.flightsearchapi.client.model.FlightResponse;
import dev.hasangurbuz.flightsearchapi.helper.DateHelper;
import dev.hasangurbuz.flightsearchapi.service.AirportService;
import dev.hasangurbuz.flightsearchapi.service.FlightService;
import dev.hasangurbuz.flightsearchapi.service.exception.AirportExistsException;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.AirportRequestDTO;
import org.openapitools.model.FlightRequestDTO;
import org.openapitools.model.PriceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClientTasks {


    private final MockAirportApiClient airportApiClient;
    private final MockFlightApiClient flightApiClient;
    private final AirportService airportService;
    private final FlightService flightService;
    private Logger logger = LoggerFactory.getLogger(ClientTasks.class);


    @Scheduled(cron = "${tasks.cron.fetchdata}")
    public void fetchCurrentData() {
        fetchAirports();
        fetchFlights();
    }


    public void fetchAirports() {
        logger.info("Airport fetching cron started");
        int count = 0;
        List<AirportResponse> airportResponses = airportApiClient.get();
        for (AirportResponse airportResponse : airportResponses) {
            AirportRequestDTO airportRequest = new AirportRequestDTO();
            airportRequest.setCity(airportResponse.getCity());

            try {
                airportService.create(airportRequest);
                count++;
            } catch (AirportExistsException e) {
                continue;
            }
        }
        logger.info(count + " airports successfully saved");
    }

    public void fetchFlights() {
        logger.info("Flight fetching cron started");
        int count = 0;
        List<FlightResponse> flightResponses = flightApiClient.get();
        for (FlightResponse flightResponse : flightResponses) {
            PriceDTO price = new PriceDTO();
            FlightRequestDTO flightRequest = new FlightRequestDTO();
            price.setAmount(new BigDecimal(flightResponse.getPrice()));
            price.setCurrency(flightResponse.getCurrency());

            OffsetDateTime departureDate = DateHelper.toUTC(
                    OffsetDateTime.parse(flightResponse.getDepartureDate()));
            OffsetDateTime returnDate = DateHelper.toUTC(
                    OffsetDateTime.parse(flightResponse.getReturnDate()));

            flightRequest.setPrice(price);
            flightRequest.setDepartureDate(departureDate);
            flightRequest.setReturnDate(returnDate);
            flightRequest.setOriginAirportId(flightResponse.getOrigin().longValue());
            flightRequest.setDestinationAirportId(flightResponse.getDestination().longValue());

            try {
                flightService.create(flightRequest);
                count++;
            } catch (Exception e) {
                continue;
            }
        }
        logger.info(count + " flights successfully saved");
    }


}

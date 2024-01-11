package dev.hasangurbuz.flightsearchapi;

import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.domain.Flight;
import dev.hasangurbuz.flightsearchapi.service.AirportService;
import dev.hasangurbuz.flightsearchapi.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class FlightSearchApiApplication  {

    private final AirportService airportService;
    private final FlightService flightService;

    public static void main(String[] args) {
        SpringApplication.run(FlightSearchApiApplication.class, args);
    }


}

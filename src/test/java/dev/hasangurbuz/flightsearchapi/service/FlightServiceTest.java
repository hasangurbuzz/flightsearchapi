package dev.hasangurbuz.flightsearchapi.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.hasangurbuz.flightsearchapi.domain.Flight;
import dev.hasangurbuz.flightsearchapi.repository.FlightRepository;
import dev.hasangurbuz.flightsearchapi.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    FlightRepository flightRepository;

    @Mock
    JPAQueryFactory jpaQueryFactory;

    @Mock
    PriceRepository priceRepository;

    @Mock
    AirportService airportService;

    FlightService flightService;
    List<Flight> flightList;


    @Test
    void assertNotSavingNullValueAndNotThrowingError() {
        assertDoesNotThrow(() -> {
            flightService.create(null);
        });

        verify(flightRepository, never()).save(any());
    }


}
package dev.hasangurbuz.flightsearchapi.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.domain.Flight;
import dev.hasangurbuz.flightsearchapi.repository.FlightRepository;
import dev.hasangurbuz.flightsearchapi.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    FlightRepository flightRepository;

    @Mock
    JPAQueryFactory jpaQueryFactory;

    FlightService flightService;
    List<Flight> flightList;

//    @BeforeEach
//    void init() {
//        flightService = new FlightServiceImpl(flightRepository, jpaQueryFactory);
//        flightList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            Flight flight = new Flight();
//            flight.setId(Long.valueOf(i));
//            flight.setPrice(BigDecimal.TEN);
//            flight.setArrivalAirport(new Airport());
//            flight.setDepartureAirport(new Airport());
//            flight.setDepartureDate(OffsetDateTime.now());
//            flight.setReturnDate(OffsetDateTime.now());
//            flightList.add(flight);
//        }
//
//    }

    @Test
    void create() {
        when(flightRepository.save(any())).thenReturn(flightList.get(0));

        Flight actual = flightService.create(flightList.get(0));

        verify(flightRepository).save(any(Flight.class));

        assertThat(actual).isEqualTo(flightList.get(0));
    }

    @Test
    void assertNotSavingNullValueAndNotThrowingError() {
        assertDoesNotThrow(() -> {
            flightService.create(null);
        });

        verify(flightRepository, never()).save(any());
    }


}
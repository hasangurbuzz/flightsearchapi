package dev.hasangurbuz.flightsearchapi.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.repository.AirportRepository;
import dev.hasangurbuz.flightsearchapi.service.impl.AirportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock
    AirportRepository airportRepository;
    @Mock
    JPAQueryFactory jpaQueryFactory;
    AirportService airportService;
    Airport mockAirport;


    @BeforeEach
    void init() {
        airportService = new AirportServiceImpl(airportRepository, jpaQueryFactory);
        mockAirport = mock(Airport.class);
    }

    @Test
    void findById() {
        when(airportRepository.findById(any())).thenReturn(Optional.of(mockAirport));

        Airport actual = airportService.findById(any());

        verify(airportRepository).findById(any());

        assertThat(actual).isEqualTo(mockAirport);

    }


}
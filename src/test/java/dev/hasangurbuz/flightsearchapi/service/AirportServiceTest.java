package dev.hasangurbuz.flightsearchapi.service;

import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.repository.AirportRepository;
import dev.hasangurbuz.flightsearchapi.service.impl.AirportServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AirportServiceTest {

    @Mock AirportRepository airportRepository;
    AirportService airportService;

    @BeforeEach
    void init() {
        airportService = new AirportServiceImpl(airportRepository);
    }


    @Test
    void createAirport() {
        Airport airport = new Airport();
        airport.setCity("Istanbul");

        Airport expected = new Airport();
        expected.setCity("Istanbul");
        expected.setId(1L);

        when(airportRepository.save(airport)).thenReturn(expected);

        Airport createdAirport = airportService.create(airport);

        verify(airportRepository).save(airport);

        assertThat(createdAirport.getCity()).isEqualTo(airport.getCity());
        assertThat(createdAirport.getId()).isNotNull();

    }

    @Test
    void assertNotThrowingErrorOnNullValue(){
        assertDoesNotThrow(()-> {
            airportService.create(null);
        });
    }

    @Test
    void assertNotSavingNullValue(){
        airportService.create(null);

        verify(airportRepository, never()).save(any());
    }

    @Test
    void getAll() {
        List<Airport> airportList = new ArrayList<>();
        Airport airport = new Airport();
        airport.setId(1L);
        airport.setCity("Istanbul");
        airportList.add(airport);

        when(airportRepository.findAll()).thenReturn(airportList);

        List<Airport> actual = airportService.getAll();

        verify(airportRepository).findAll();

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(airportList);
    }

    @Test
    void findById() {
        Airport airport = new Airport();
        airport.setId(1L);
        airport.setCity("Istanbul");

        when(airportRepository.findById(any())).thenReturn(Optional.of(airport));

        Airport actual = airportService.findById(any());

        verify(airportRepository).findById(any());

        assertThat(actual).isEqualTo(airport);

    }

    @Test
    void assertReturnNullWhenAirportNotFound(){
        when(airportRepository.findById(any())).thenReturn(Optional.empty());

        Airport actual = airportService.findById(1L);

        verify(airportRepository).findById(any());

        assertThat(actual).isNull();
    }
}
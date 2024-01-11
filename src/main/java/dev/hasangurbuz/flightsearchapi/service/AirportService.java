package dev.hasangurbuz.flightsearchapi.service;

import dev.hasangurbuz.flightsearchapi.api.ApiException;
import dev.hasangurbuz.flightsearchapi.domain.Airport;

import java.util.List;

public interface AirportService {
    Airport create(Airport airport);

    List<Airport> getAll();

    Airport findById(Long id);

}

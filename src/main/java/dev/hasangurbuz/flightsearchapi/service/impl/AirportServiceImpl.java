package dev.hasangurbuz.flightsearchapi.service.impl;

import dev.hasangurbuz.flightsearchapi.api.ApiException;
import dev.hasangurbuz.flightsearchapi.domain.Airport;
import dev.hasangurbuz.flightsearchapi.repository.AirportRepository;
import dev.hasangurbuz.flightsearchapi.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;


    @Override
    public Airport create(Airport airport) {
        if (airport == null){
            return null;
        }
        return airportRepository.save(airport);
    }

    @Override
    public List<Airport> getAll() {
        return airportRepository.findAll();
    }

    @Override
    public Airport findById(Long id) {
        Optional<Airport> result = airportRepository.findById(id);
        if (result.isPresent()){
            return result.get();
        }

        return null;
    }

}

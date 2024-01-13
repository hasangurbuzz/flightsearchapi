package dev.hasangurbuz.flightsearchapi.service.exception;

import dev.hasangurbuz.flightsearchapi.exception.NotFoundException;

public class AirportNotFoundException extends NotFoundException {
    public AirportNotFoundException(Long id) {
        super("Airport not found with id : " + id);
    }
}

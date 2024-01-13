package dev.hasangurbuz.flightsearchapi.service.exception;

import dev.hasangurbuz.flightsearchapi.exception.NotFoundException;

public class FlightNotFoundException extends NotFoundException {
    public FlightNotFoundException(Long id) {
        super("Flight not found with id : " + id);
    }

}

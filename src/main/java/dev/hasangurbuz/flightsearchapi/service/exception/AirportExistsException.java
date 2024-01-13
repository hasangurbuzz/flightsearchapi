package dev.hasangurbuz.flightsearchapi.service.exception;

import dev.hasangurbuz.flightsearchapi.exception.ExistsException;

public class AirportExistsException extends ExistsException {
    public AirportExistsException(String name) {
        super("Airport exist with name : " + name);
    }
}

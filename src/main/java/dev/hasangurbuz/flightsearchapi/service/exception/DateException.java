package dev.hasangurbuz.flightsearchapi.service.exception;

import dev.hasangurbuz.flightsearchapi.exception.ApiException;
import dev.hasangurbuz.flightsearchapi.exception.ApiExceptionCode;

public class DateException extends ApiException {
    public DateException(String message) {
        super(ApiExceptionCode.INVALID_INPUT, message);
    }
}

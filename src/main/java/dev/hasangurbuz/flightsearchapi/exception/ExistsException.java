package dev.hasangurbuz.flightsearchapi.exception;

public class ExistsException extends ApiException {
    public ExistsException(String message) {
        super(ApiExceptionCode.INVALID_INPUT, message);
    }
}

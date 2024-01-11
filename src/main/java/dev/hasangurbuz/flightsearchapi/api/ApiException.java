package dev.hasangurbuz.flightsearchapi.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class ApiException extends RuntimeException {
    private ApiExceptionCode code;
    private String message;

    public static ApiException invalidInput(String cause) {
        return new ApiException(ApiExceptionCode.INVALID_INPUT, cause);
    }

    public static ApiException notFound(String cause) {
        return new ApiException(ApiExceptionCode.NOT_FOUND, cause);
    }

}

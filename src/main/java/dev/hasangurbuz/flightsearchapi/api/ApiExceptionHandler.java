package dev.hasangurbuz.flightsearchapi.api;

import org.openapitools.model.ErrorCodeDTO;
import org.openapitools.model.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDTO> handle(ApiException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ErrorDTO error = new ErrorDTO();
        error.setCode(ErrorCodeDTO.valueOf(exception.getCode().name()));
        error.setCause(exception.getMessage());

        if (exception.getCode().equals(ApiExceptionCode.NOT_FOUND)) {
            status = HttpStatus.NOT_FOUND;
        }

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handle(MethodArgumentNotValidException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(ErrorCodeDTO.INVALID_INPUT);
        errorDTO.setCause(e.getBindingResult().getFieldError().getField() + " : is not valid");
        return ResponseEntity.badRequest().body(errorDTO);

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDTO> handle(BadCredentialsException e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(ErrorCodeDTO.AUTH_ERROR);
        errorDTO.setCause(e.getMessage());
        return ResponseEntity.badRequest().body(errorDTO);
    }
}

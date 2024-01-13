package dev.hasangurbuz.flightsearchapi.exception;

import org.openapitools.model.ErrorCodeDTO;
import org.openapitools.model.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GeneralExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handle(Exception e) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(ErrorCodeDTO.SERVER_ERROR);
        errorDTO.setCause("Unexpected error");
        logger.error(e.getMessage());
        return ResponseEntity.internalServerError().body(errorDTO);
    }
}

package com.upgrade.campsitereservation.errorhandler;

import com.upgrade.campsitereservation.dto.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalErrorResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value= { CampsiteException.class})
    protected ResponseEntity<Object> handleReservationException(CampsiteException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .action(ex.getAction())
                .errorMessage(ex.getMessage())
                .build();
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.NOT_ACCEPTABLE, request);
    }
}

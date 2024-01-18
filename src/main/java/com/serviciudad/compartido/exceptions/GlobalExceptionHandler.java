package com.serviciudad.compartido.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse;

        if (e instanceof BusinessException) {
            errorResponse = new ErrorResponse(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } else if (e instanceof DataIntegrityViolationException) {
            String message = extractConstraintViolationMessage((DataIntegrityViolationException) e);
            errorResponse = new ErrorResponse(message);
            log.error("Error de integridad de datos: {}", message, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } else {
            errorResponse = new ErrorResponse("Se ha producido un error en el servidor.");
            log.error("Error inesperado", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    private String extractConstraintViolationMessage(DataIntegrityViolationException e) {
     //   if (e.getCause() instanceof ConstraintViolationException cve) {
     //       return cve.getSQLException().getMessage();
     //   }
        return "Violación de la integridad de los datos.";
    }

    @Getter
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
    }
}

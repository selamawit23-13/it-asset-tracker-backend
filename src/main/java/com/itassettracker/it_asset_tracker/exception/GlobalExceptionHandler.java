
package com.itassettracker.it_asset_tracker.exception;

import com.itassettracker.it_asset_tracker.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ErrorResponse body(
            HttpStatus status,
            String message,
            HttpServletRequest request) {

        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                message,
                request.getRequestURI());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> notFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(body(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ErrorResponse> badRequest(
            Exception ex,
            HttpServletRequest request) {

        String message;

        if (ex instanceof MethodArgumentNotValidException validationEx) {
            message = validationEx.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("; "));

        } else if (ex instanceof MethodArgumentTypeMismatchException mismatchEx) {
            message = "Invalid value for parameter: " + mismatchEx.getName();

        } else {
            message = ex.getMessage();
        }

        return ResponseEntity.badRequest()
                .body(body(HttpStatus.BAD_REQUEST, message, request));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> conflict(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(body(
                        HttpStatus.CONFLICT,
                        "A record with the same unique value already exists.",
                        request));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unexpected(
            Exception ex,
            HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Unexpected server error.",
                        request));
    }
}

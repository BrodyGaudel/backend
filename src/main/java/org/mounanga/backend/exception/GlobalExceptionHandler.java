package org.mounanga.backend.exception;

import org.jetbrains.annotations.NotNull;
import org.mounanga.backend.dto.ExceptionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleException(@NotNull ProductNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND).body( new ExceptionResponseDTO(
                NOT_FOUND.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>()
        ));
    }

    @ExceptionHandler(BarcodeAlreadyExistException.class)
    public ResponseEntity<ExceptionResponseDTO> handleException(@NotNull BarcodeAlreadyExistException exception) {
        return ResponseEntity.status(CONFLICT).body( new ExceptionResponseDTO(
                CONFLICT.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDTO> handleException(@NotNull MethodArgumentNotValidException exception) {
        Set<String> validationErrors = new HashSet<>();
        exception.getBindingResult().getAllErrors().forEach(error -> validationErrors.add(error.getDefaultMessage()));

        return ResponseEntity.status(BAD_REQUEST).body(new ExceptionResponseDTO(
                BAD_REQUEST.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                validationErrors
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponseDTO> handleException(@NotNull Exception exception) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ExceptionResponseDTO(
                INTERNAL_SERVER_ERROR.value(),
                exception.getMessage(),
                exception.getLocalizedMessage(),
                new HashSet<>()
        ));
    }
}

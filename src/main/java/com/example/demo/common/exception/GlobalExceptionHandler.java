package com.example.demo.common.exception;

import com.example.demo.common.model.CustomError;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for handling various types of exceptions across the application.
 * This class provides centralized error handling for the entire application and returns custom error responses.
 */
/**
 * Global exception handler for handling various types of exceptions across the application.
 * This class provides centralized error handling for the entire application and returns custom error responses.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException, which is thrown when method arguments are not valid.
     * The response contains details about the validation errors for each field.
     *
     * @param ex The MethodArgumentNotValidException that is thrown.
     * @return ResponseEntity containing the custom error message and validation details.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {

        List<CustomError.CustomSubError> subErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(
                error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    subErrors.add(
                            CustomError.CustomSubError.builder()
                                    .field(fieldName)
                                    .message(message)
                                    .build()
                    );
                }
        );

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Validation failed")
                .subErrors(subErrors)
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);

    }

    /**
     * Handles ConstraintViolationException, which is thrown when there are constraint violations in method parameters.
     * The response contains details about the constraint violations, including the field, message, invalid value, and type.
     *
     * @param constraintViolationException The ConstraintViolationException that is thrown.
     * @return ResponseEntity containing the custom error message and constraint violation details.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handlePathVariableErrors(final ConstraintViolationException constraintViolationException) {

        List<CustomError.CustomSubError> subErrors = new ArrayList<>();
        constraintViolationException.getConstraintViolations()
                .forEach(constraintViolation ->
                        subErrors.add(
                                CustomError.CustomSubError.builder()
                                        .message(constraintViolation.getMessage())
                                        .field(StringUtils.substringAfterLast(constraintViolation.getPropertyPath().toString(), "."))
                                        .value(constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : null)
                                        .type(constraintViolation.getInvalidValue().getClass().getSimpleName())
                                        .build()
                        )
                );

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Constraint violation")
                .subErrors(subErrors)
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);

    }

    /**
     * Handles general RuntimeException. It responds with an error message and a 404 NOT_FOUND status.
     *
     * @param runtimeException The RuntimeException that is thrown.
     * @return ResponseEntity containing the custom error message.
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleRuntimeException(final RuntimeException runtimeException) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message(runtimeException.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles AccessDeniedException, which is thrown when a user does not have permission to access a resource.
     * The response contains an access denied message and a 403 FORBIDDEN status.
     *
     * @param accessDeniedException The AccessDeniedException that is thrown.
     * @return ResponseEntity containing the custom error message and access denied status.
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<?> handleAccessDeniedException(final AccessDeniedException accessDeniedException) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.FORBIDDEN)
                .header(CustomError.Header.AUTH_ERROR.getName())
                .message(accessDeniedException.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.FORBIDDEN);
    }

}

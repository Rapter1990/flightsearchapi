package com.example.demo.flight.utils.validator;

import com.example.demo.flight.model.dto.request.flight.CreateFlightRequest;
import com.example.demo.flight.utils.annotations.ValidArrivalTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

/**
 * Validates that the arrival time is not earlier than the departure time in {@link CreateFlightRequest}.
 */
@Component
public class ArrivalTimeValidator implements ConstraintValidator<ValidArrivalTime, CreateFlightRequest> {

    /**
     * Checks if the arrival time is after the departure time.
     * Returns {@code true} if valid, {@code false} if arrival time is earlier.
     *
     * @param createFlightRequest the object to validate
     * @param context the validation context
     * @return {@code true} if arrival time is after departure time, {@code false} otherwise
     */
    @Override
    public boolean isValid(CreateFlightRequest createFlightRequest, ConstraintValidatorContext context) {
        // Validate that arrival time is after departure time
        if (createFlightRequest.getArrivalTime() != null && createFlightRequest.getDepartureTime() != null) {
            return !createFlightRequest.getArrivalTime().isBefore(createFlightRequest.getDepartureTime());
        }
        return true; // Valid if either time is null
    }
}

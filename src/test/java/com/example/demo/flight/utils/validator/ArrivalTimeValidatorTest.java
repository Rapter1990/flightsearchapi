package com.example.demo.flight.utils.validator;

import com.example.demo.base.AbstractBaseServiceTest;
import com.example.demo.flight.model.dto.request.flight.CreateFlightRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ArrivalTimeValidatorTest extends AbstractBaseServiceTest {

    private ArrivalTimeValidator arrivalTimeValidator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        arrivalTimeValidator = new ArrivalTimeValidator();
    }

    @Test
    void testValidArrivalTimeAfterDepartureTime() {
        // Given
        CreateFlightRequest validRequest = new CreateFlightRequest();
        validRequest.setDepartureTime(LocalDateTime.of(2025, 1, 13, 10, 0));
        validRequest.setArrivalTime(LocalDateTime.of(2025, 1, 13, 12, 0));

        // When
        boolean isValid = arrivalTimeValidator.isValid(validRequest, context);

        // Then
        assertTrue(isValid, "Arrival time should be after departure time.");
    }

    @Test
    void testInvalidArrivalTimeBeforeDepartureTime() {
        // Given
        CreateFlightRequest invalidRequest = new CreateFlightRequest();
        invalidRequest.setDepartureTime(LocalDateTime.of(2025, 1, 13, 10, 0));
        invalidRequest.setArrivalTime(LocalDateTime.of(2025, 1, 13, 8, 0));

        // When
        boolean isValid = arrivalTimeValidator.isValid(invalidRequest, context);

        // Then
        assertFalse(isValid, "Arrival time cannot be earlier than departure time.");
    }

    @Test
    void testNullArrivalTime() {
        // Given
        CreateFlightRequest requestWithNullArrival = new CreateFlightRequest();
        requestWithNullArrival.setDepartureTime(LocalDateTime.of(2025, 1, 13, 10, 0));
        requestWithNullArrival.setArrivalTime(null);

        // When
        boolean isValid = arrivalTimeValidator.isValid(requestWithNullArrival, context);

        // Then
        assertTrue(isValid, "Arrival time should be valid when it is null.");
    }

    @Test
    void testNullDepartureTime() {
        // Given
        CreateFlightRequest requestWithNullDeparture = new CreateFlightRequest();
        requestWithNullDeparture.setDepartureTime(null);
        requestWithNullDeparture.setArrivalTime(LocalDateTime.of(2025, 1, 13, 12, 0));

        // When
        boolean isValid = arrivalTimeValidator.isValid(requestWithNullDeparture, context);

        // Then
        assertTrue(isValid, "Arrival time should be valid when departure time is null.");
    }

    @Test
    void testBothTimesNull() {
        // Given
        CreateFlightRequest requestWithBothNull = new CreateFlightRequest();
        requestWithBothNull.setDepartureTime(null);
        requestWithBothNull.setArrivalTime(null);

        // When
        boolean isValid = arrivalTimeValidator.isValid(requestWithBothNull, context);

        // Then
        assertTrue(isValid, "Both arrival and departure times should be valid when null.");
    }

    @Test
    void testArrivalTimeEqualToDepartureTime() {
        // Given
        CreateFlightRequest requestWithEqualTimes = new CreateFlightRequest();
        LocalDateTime sameTime = LocalDateTime.of(2025, 1, 13, 10, 0);
        requestWithEqualTimes.setDepartureTime(sameTime);
        requestWithEqualTimes.setArrivalTime(sameTime);

        // When
        boolean isValid = arrivalTimeValidator.isValid(requestWithEqualTimes, context);

        // Then
        assertTrue(isValid, "Arrival time should be valid if it is equal to departure time.");
    }


}
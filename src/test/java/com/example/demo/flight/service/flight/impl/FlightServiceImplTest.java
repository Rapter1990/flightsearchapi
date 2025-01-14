package com.example.demo.flight.service.flight.impl;

import com.example.demo.base.AbstractBaseServiceTest;
import com.example.demo.builder.CreateFlightRequestBuilder;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.dto.request.flight.CreateFlightRequest;
import com.example.demo.flight.model.entity.AirportEntity;
import com.example.demo.flight.service.flight.FlightCreateService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link FlightServiceImpl}.
 * This class verifies the correctness of the business logic in {@link FlightServiceImpl}.
 */
class FlightServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private FlightServiceImpl flightService;

    @Mock
    private FlightCreateService flightCreateService;

    @Test
    void givenValidCreateFlightRequest_whenCreateFlight_ThenReturnFlight() {

        // Given
        final CreateFlightRequest request = new CreateFlightRequestBuilder()
                .withValidFields()
                .build();

        final Airport departureAirport = Airport.builder()
                .id("AIRPORT1")
                .name("Departure Airport")
                .cityName("Departure City")
                .build();

        final Airport arrivalAirport = Airport.builder()
                .id("AIRPORT2")
                .name("Arrival Airport")
                .cityName("Arrival City")
                .build();

        // Use SuperBuilder to construct the mock flight
        final Flight mockFlight = Flight.builder()
                .id("FLIGHT123")
                .fromAirport(departureAirport)
                .toAirport(arrivalAirport)
                .departureTime(LocalDateTime.of(2025, 1, 13, 10, 0))
                .arrivalTime(LocalDateTime.of(2025, 1, 13, 12, 0))
                .price(100.0)
                .build();

        // When
        when(flightCreateService.createFlight(any(CreateFlightRequest.class))).thenReturn(mockFlight);

        // Then
        Flight response = flightService.createFlight(request);

        assertNotNull(response);
        assertEquals(mockFlight.getId(), response.getId());
        assertEquals(mockFlight.getFromAirport(), response.getFromAirport());
        assertEquals(mockFlight.getToAirport(), response.getToAirport());
        assertEquals(mockFlight.getDepartureTime(), response.getDepartureTime());
        assertEquals(mockFlight.getArrivalTime(), response.getArrivalTime());
        assertEquals(mockFlight.getPrice(), response.getPrice());

        // Verify
        verify(flightCreateService, times(1)).createFlight(any(CreateFlightRequest.class));

    }

}
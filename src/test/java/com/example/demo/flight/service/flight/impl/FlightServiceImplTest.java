package com.example.demo.flight.service.flight.impl;

import com.example.demo.base.AbstractBaseServiceTest;
import com.example.demo.builder.AirportBuilder;
import com.example.demo.builder.CreateFlightRequestBuilder;
import com.example.demo.builder.FlightBuilder;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.dto.request.flight.CreateFlightRequest;
import com.example.demo.flight.model.entity.AirportEntity;
import com.example.demo.flight.service.flight.FlightCreateService;
import com.example.demo.flight.service.flight.FlightReadService;
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

    @Mock
    private FlightReadService flightReadService;

    @Test
    void givenValidCreateFlightRequest_whenCreateFlight_ThenReturnFlight() {

        // Given
        final CreateFlightRequest request = new CreateFlightRequestBuilder()
                .withValidFields()
                .build();

        final Airport departureAirport = new AirportBuilder().withValidFields();

        final Airport arrivalAirport = new AirportBuilder().withValidFields();

        final Flight mockFlight = new FlightBuilder().withValidFields()
                .withFromAirport(departureAirport)
                .withToAirport(arrivalAirport)
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

    @Test
    void givenValidFlightId_whenGetFlightById_thenReturnFlight() {

        // Given
        final String flightId = "FLIGHT123";

        final Airport departureAirport = new AirportBuilder().withValidFields();

        final Airport arrivalAirport = new AirportBuilder().withValidFields();

        final Flight mockFlight = new FlightBuilder().withValidFields()
                .withFromAirport(departureAirport)
                .withToAirport(arrivalAirport)
                .build();

        // When
        when(flightReadService.getFlightById(flightId)).thenReturn(mockFlight);

        // Then
        Flight response = flightService.getFlightById(flightId);

        assertNotNull(response);
        assertEquals(mockFlight.getId(), response.getId());
        assertEquals(mockFlight.getFromAirport(), response.getFromAirport());
        assertEquals(mockFlight.getToAirport(), response.getToAirport());
        assertEquals(mockFlight.getDepartureTime(), response.getDepartureTime());
        assertEquals(mockFlight.getArrivalTime(), response.getArrivalTime());
        assertEquals(mockFlight.getPrice(), response.getPrice());

        // Verify
        verify(flightReadService, times(1)).getFlightById(flightId);
    }


}
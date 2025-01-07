package com.example.demo.flight.service.airport.impl;

import com.example.demo.base.AbstractBaseServiceTest;
import com.example.demo.builder.CreateAirportRequestBuilder;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;
import com.example.demo.flight.service.airport.AirportCreateService;
import com.example.demo.flight.service.airport.AirportReadService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link AirportServiceImpl}.
 * This class verifies the correctness of the business logic in {@link AirportServiceImpl}.
 */
class AirportServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private AirportServiceImpl airportService;

    @Mock
    private AirportCreateService airportCreateService;

    @Mock
    private AirportReadService airportReadService;

    @Test
    void givenValidCreateAirportRequest_whenCreateAirport_thenReturnCreatedAirport() {

        // Given
        final CreateAirportRequest request = new CreateAirportRequestBuilder()
                .withValidFields().build();

        Airport expectedAirport = Airport.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .cityName(request.getCityName())
                .build();

        // When
        when(airportCreateService.createAirport(any(CreateAirportRequest.class))).thenReturn(expectedAirport);

        // Then
        Airport result = airportService.createAirport(request);

        assertNotNull(result);
        assertEquals(expectedAirport.getId(), result.getId());
        assertEquals(expectedAirport.getName(), result.getName());
        assertEquals(expectedAirport.getCityName(), result.getCityName());

        // Verify
        verify(airportCreateService, times(1)).createAirport(any(CreateAirportRequest.class));

    }

    @Test
    void givenValidAirportId_whenGetAirportById_thenReturnAirport() {
        // Given
        String airportId = UUID.randomUUID().toString();
        Airport expectedAirport = Airport.builder()
                .id(airportId)
                .name("Test Airport")
                .cityName("Test City")
                .build();

        // When
        when(airportReadService.getAirportById(airportId)).thenReturn(expectedAirport);

        // Then
        Airport result = airportService.getAirportById(airportId);

        assertNotNull(result);
        assertEquals(expectedAirport.getId(), result.getId());
        assertEquals(expectedAirport.getName(), result.getName());
        assertEquals(expectedAirport.getCityName(), result.getCityName());

        // Verify
        verify(airportReadService, times(1)).getAirportById(airportId);
    }

}
package com.example.demo.flight.service.airport.impl;

import com.example.demo.base.AbstractBaseServiceTest;
import com.example.demo.builder.AirportEntityBuilder;
import com.example.demo.flight.exception.AirportNotFoundException;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.entity.AirportEntity;
import com.example.demo.flight.model.mapper.AirportEntityToAirportMapper;
import com.example.demo.flight.repository.AirportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link AirportReadServiceImpl}.
 * This class verifies the correctness of the business logic in {@link AirportReadServiceImpl}.
 */
class AirportReadServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private AirportReadServiceImpl airportReadService;

    @Mock
    private AirportRepository airportRepository;

    private final AirportEntityToAirportMapper airportEntityToAirportMapper =
            AirportEntityToAirportMapper.initialize();

    @Test
    void givenExistAirportId_whenGetAirportById_thenReturnAirport() {

        // Given
        final String mockId = UUID.randomUUID().toString();

        final AirportEntity airportEntity = new AirportEntityBuilder()
                .withId(mockId)
                .withValidFields();

        final Airport mockAirport = airportEntityToAirportMapper.map(airportEntity);

        // When
        when(airportRepository.findById(mockId)).thenReturn(Optional.of(airportEntity));

        // Then
        final Airport expected = airportReadService.getAirportById(mockId);

        assertNotNull(expected);
        assertEquals(mockAirport.getId(),expected.getId());
        assertEquals(mockAirport.getName(),expected.getName());

        // Verify
        verify(airportRepository, times(1)).findById(mockId);

    }

    @Test
    void givenNonExistId_whenGetById_thenThrowTaskNotFoundException() {

        // Given
        final String mockId = UUID.randomUUID().toString();

        // When
        when(airportRepository.findById(mockId)).thenReturn(Optional.empty());

        // Then
        assertThrows(AirportNotFoundException.class,
                ()->airportReadService.getAirportById(mockId));

        // Verify
        verify(airportRepository, times(1)).findById(mockId);

    }

}
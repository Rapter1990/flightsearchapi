package com.example.demo.flight.job;

import com.example.demo.base.AbstractBaseServiceTest;
import com.example.demo.flight.model.entity.AirportEntity;
import com.example.demo.flight.repository.AirportRepository;
import com.example.demo.flight.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FlightDataLoaderTest extends AbstractBaseServiceTest {

    @InjectMocks
    private FlightDataLoader flightDataLoader;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirportRepository airportRepository;

    @Test
    void shouldLoadFlightDummyData() {

        // Given
        AirportEntity airport1 = AirportEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("Los Angeles International Airport")
                .cityName("Los Angeles")
                .build();

        AirportEntity airport2 = AirportEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("John F. Kennedy International Airport")
                .cityName("New York")
                .build();

        AirportEntity airport3 = AirportEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("Chicago O'Hare International Airport")
                .cityName("Chicago")
                .build();

        List<AirportEntity> mockAirports = List.of(airport1, airport2, airport3);

        // When
        when(airportRepository.findAll()).thenReturn(mockAirports);

        // Then
        flightDataLoader.loadFlightDumpyData();

        // Verify
        verify(airportRepository, times(1)).saveAll(anyList());
        verify(flightRepository, times(1)).saveAll(anyList());

    }

}
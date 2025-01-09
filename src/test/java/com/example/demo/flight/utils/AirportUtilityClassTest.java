package com.example.demo.flight.utils;

import com.example.demo.base.AbstractBaseServiceTest;
import com.example.demo.flight.exception.AirportNameAlreadyExistException;
import com.example.demo.flight.repository.AirportRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AirportUtilityClassTest extends AbstractBaseServiceTest {

    @Mock
    private AirportRepository airportRepository;

    @Test
    void utilityClass_ShouldNotBeInstantiated() {
        assertThrows(InvocationTargetException.class, () -> {
            // Attempt to use reflection to create an instance of the utility class
            java.lang.reflect.Constructor<AirportUtilityClass> constructor = AirportUtilityClass.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    void checkAirportNameUniqueness_ShouldThrowException_WhenNameExists() {
        String existingAirportName = "Existing Airport";

        when(airportRepository.existsByName(existingAirportName)).thenReturn(true);

        assertThrows(AirportNameAlreadyExistException.class, () ->
                AirportUtilityClass.checkAirportNameUniqueness(airportRepository, existingAirportName)
        );

        verify(airportRepository).existsByName(existingAirportName);
    }

    @Test
    void checkAirportNameUniqueness_ShouldNotThrowException_WhenNameDoesNotExist() {
        String newAirportName = "New Airport";

        when(airportRepository.existsByName(newAirportName)).thenReturn(false);

        assertDoesNotThrow(() ->
                AirportUtilityClass.checkAirportNameUniqueness(airportRepository, newAirportName)
        );

        verify(airportRepository).existsByName(newAirportName);
    }

}
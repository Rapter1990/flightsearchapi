package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link FlightSearchApiApplication}.
 * This class contains unit tests to verify the behavior of the application's main method
 * and ensure that the Spring application context loads correctly.
 */
@SpringBootTest
class FlightSearchApiApplicationTest {

    @Test
    void testMainMethod() {
        // Ensure that the main method can be executed without throwing any exceptions
        assertDoesNotThrow(() -> FlightSearchApiApplication.main(new String[]{}),
                "The main method should run without throwing exceptions.");
    }

}
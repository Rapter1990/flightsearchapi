package com.example.demo.flight.service.flight;

import com.example.demo.flight.model.Flight;

/**
 * Service implementation for retrieving a flight in the system.
 */
public interface FlightReadService {

    /**
     * Retrieves a flight by its ID.
     *
     * @param id the ID of the flight to be retrieved.
     * @return the {@link Flight} entity with the specified ID.
     */
    Flight getFlightById(final String id);

}

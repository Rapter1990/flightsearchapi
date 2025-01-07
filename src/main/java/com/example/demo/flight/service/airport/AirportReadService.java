package com.example.demo.flight.service.airport;

import com.example.demo.flight.model.Airport;

/**
 * Service interface for retrieving an airport in the system.
 */
public interface AirportReadService {

    /**
     * Retrieves an airport by its ID.
     *
     * @param id the ID of the task to be retrieved.
     * @return the {@link Airport} entity with the specified ID.
     */
    Airport getAirportById(final String id);

}

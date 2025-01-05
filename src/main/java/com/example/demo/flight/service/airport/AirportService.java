package com.example.demo.flight.service.airport;

import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;

/**
 * Service interface for managing an airport in the system.
 */
public interface AirportService {

    /**
     * Create a new airport to the database.
     *
     * @param createAirportRequest the request object containing the details of the CreateAirportRequest to be saved.
     * @return the saved {@link Airport} domain object.
     */
    Airport createAirport(CreateAirportRequest createAirportRequest);

}

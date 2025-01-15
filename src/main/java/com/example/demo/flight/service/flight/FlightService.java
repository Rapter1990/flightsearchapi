package com.example.demo.flight.service.flight;

import com.example.demo.common.model.CustomPage;
import com.example.demo.common.model.dto.request.CustomPagingRequest;
import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.dto.request.flight.CreateFlightRequest;

/**
 * Service interface for managing a flight in the system.
 */
public interface FlightService {

    /**
     * Creates a new flight in the system.
     *
     * @param createFlightRequest the request object containing the details of the flight to be created.
     * @return the created {@link Flight} entity.
     */
    Flight createFlight(CreateFlightRequest createFlightRequest);

    /**
     * Retrieves a flight by its ID.
     *
     * @param id the ID of the flight to be retrieved.
     * @return the {@link Flight} entity with the specified ID.
     */
    Flight getFlightById(final String id);

    /**
     * Retrieves all flights with pagination support.
     *
     * @param customPagingRequest the request object containing paging parameters (e.g., page size, page number).
     * @return a {@link CustomPage} of flights, containing a list of flights and pagination details.
     */
    CustomPage<Flight> getAllFlights(final CustomPagingRequest customPagingRequest);

}

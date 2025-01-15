package com.example.demo.flight.service.flight.impl;

import com.example.demo.common.model.CustomPage;
import com.example.demo.common.model.dto.request.CustomPagingRequest;
import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.dto.request.flight.CreateFlightRequest;
import com.example.demo.flight.service.flight.FlightCreateService;
import com.example.demo.flight.service.flight.FlightReadService;
import com.example.demo.flight.service.flight.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing a flight in the system.
 */
@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightCreateService flightCreateService;
    private final FlightReadService flightReadService;

    /**
     * Creates a new flight in the system.
     *
     * @param createFlightRequest the request object containing the details of the flight to be created.
     * @return the created {@link Flight} entity.
     */
    @Override
    public Flight createFlight(CreateFlightRequest createFlightRequest) {
        return flightCreateService.createFlight(createFlightRequest);
    }

    /**
     * Retrieves a flight by its ID.
     *
     * @param id the ID of the flight to be retrieved.
     * @return the {@link Flight} entity with the specified ID.
     */
    @Override
    public Flight getFlightById(String id) {
        return flightReadService.getFlightById(id);
    }

    /**
     * Retrieves all flights with pagination support.
     *
     * @param customPagingRequest the request object containing paging parameters (e.g., page size, page number).
     * @return a {@link CustomPage} of flights, containing a list of flights and pagination details.
     */
    @Override
    public CustomPage<Flight> getAllFlights(CustomPagingRequest customPagingRequest) {
        return flightReadService.getAllFlights(customPagingRequest);
    }

}

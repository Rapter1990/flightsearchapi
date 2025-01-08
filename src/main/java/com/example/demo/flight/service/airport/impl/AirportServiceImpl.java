package com.example.demo.flight.service.airport.impl;

import com.example.demo.common.model.CustomPage;
import com.example.demo.common.model.dto.request.CustomPagingRequest;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;
import com.example.demo.flight.service.airport.AirportCreateService;
import com.example.demo.flight.service.airport.AirportReadService;
import com.example.demo.flight.service.airport.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing an airport in the system.
 */
@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportCreateService airportCreateService;
    private final AirportReadService airportReadService;

    /**
     * Create a new airport to the database.
     *
     * @param createAirportRequest the request object containing the details of the CreateAirportRequest to be saved.
     * @return the saved {@link Airport} domain object.
     */
    @Override
    public Airport createAirport(CreateAirportRequest createAirportRequest) {
        return airportCreateService.createAirport(createAirportRequest);
    }

    /**
     * Retrieves an airport by its ID.
     *
     * @param id the ID of the task to be retrieved.
     * @return the {@link Airport} entity with the specified ID.
     */
    @Override
    public Airport getAirportById(String id) {
        return airportReadService.getAirportById(id);
    }

    /**
     * Retrieves all tasks with pagination support.
     *
     * @param customPagingRequest the request object containing paging parameters (e.g., page size, page number).
     * @return a {@link CustomPage} of tasks, containing a list of tasks and pagination details.
     */
    @Override
    public CustomPage<Airport> getAllAirports(CustomPagingRequest customPagingRequest) {
        return airportReadService.getAllAirports(customPagingRequest);
    }


}

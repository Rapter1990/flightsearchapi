package com.example.demo.flight.service.flight.impl;

import com.example.demo.flight.exception.FlightNotFoundException;
import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.entity.FlightEntity;
import com.example.demo.flight.model.mapper.flight.FlightEntityToFlightMapper;
import com.example.demo.flight.repository.FlightRepository;
import com.example.demo.flight.service.flight.FlightReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service implementation for retrieving a flight in the system.
 */
@Service
@RequiredArgsConstructor
public class FlightReadServiceImpl implements FlightReadService {

    private final FlightRepository flightRepository;

    private final FlightEntityToFlightMapper flightEntityToFlightMapper =
            FlightEntityToFlightMapper.initialize();

    /**
     * Retrieves a flight by its ID.
     *
     * @param id the ID of the flight to be retrieved.
     * @return the {@link Flight} entity with the specified ID.
     */
    @Override
    public Flight getFlightById(String id) {

        FlightEntity flightEntityFromDb = flightRepository.findById(id)
                .orElseThrow(()->new FlightNotFoundException("Flight given id " + id + " +can't found "));

        return flightEntityToFlightMapper.map(flightEntityFromDb);

    }


}

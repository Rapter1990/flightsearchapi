package com.example.demo.flight.service.airport.impl;

import com.example.demo.flight.exception.AirportNotFoundException;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.entity.AirportEntity;
import com.example.demo.flight.model.mapper.AirportEntityToAirportMapper;
import com.example.demo.flight.repository.AirportRepository;
import com.example.demo.flight.service.airport.AirportReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service interface for retrieving an airport in the system.
 */
@Service
@RequiredArgsConstructor
public class AirportReadServiceImpl implements AirportReadService {

    private final AirportRepository airportRepository;

    private final AirportEntityToAirportMapper airportEntityToAirportMapper =
            AirportEntityToAirportMapper.initialize();

    /**
     * Retrieves an airport by its ID.
     *
     * @param id the ID of the task to be retrieved.
     * @return the {@link Airport} entity with the specified ID.
     */
    @Override
    public Airport getAirportById(String id) {

        AirportEntity airportEntityFromDb = airportRepository.findById(id)
                .orElseThrow(()->new AirportNotFoundException("Airport given id cant found"));

        return airportEntityToAirportMapper.map(airportEntityFromDb);

    }

}

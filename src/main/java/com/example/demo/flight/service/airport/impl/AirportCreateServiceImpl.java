package com.example.demo.flight.service.airport.impl;

import com.example.demo.flight.exception.AirportNameAlreadyExistException;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;
import com.example.demo.flight.model.entity.AirportEntity;
import com.example.demo.flight.model.mapper.AirportEntityToAirportMapper;
import com.example.demo.flight.model.mapper.CreateAirportRequestToAirportEntityMapper;
import com.example.demo.flight.repository.AirportRepository;
import com.example.demo.flight.service.airport.AirportCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirportCreateServiceImpl implements AirportCreateService {

    private final AirportRepository airportRepository;

    private final CreateAirportRequestToAirportEntityMapper createAirportRequestToAirportEntityMapper =
            CreateAirportRequestToAirportEntityMapper.initialize();

    private final AirportEntityToAirportMapper airportEntityToAirportMapper =
            AirportEntityToAirportMapper.initialize();

    /**
     * Create a new airport to the database.
     *
     * @param createAirportRequest the request object containing the details of the CreateAirportRequest to be saved.
     * @return the saved {@link Airport} domain object.
     */
    @Override
    public Airport createAirport(CreateAirportRequest createAirportRequest) {

        checkAirportNameUniqueness(createAirportRequest.getName());

        AirportEntity airportEntityToBeSaved = createAirportRequestToAirportEntityMapper.mapForSaving(createAirportRequest);
        airportRepository.save(airportEntityToBeSaved);

        return airportEntityToAirportMapper.map(airportEntityToBeSaved);

    }

    /**
     * Checks the uniqueness of an airport name in the repository.
     * If an airport with the given name already exists, a {@link AirportNameAlreadyExistException} is thrown.
     *
     * @param airportName the name of the airport to be checked for uniqueness.
     * @throws AirportNameAlreadyExistException if a task with the given name already exists in the repository.
     */
    private void checkAirportNameUniqueness(final String airportName) {
        if (airportRepository.existsByName(airportName)) {
            throw new AirportNameAlreadyExistException("With given airport name = " + airportName);
        }
    }

}

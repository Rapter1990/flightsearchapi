package com.example.demo.flight.service.airport.impl;

import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;
import com.example.demo.flight.service.airport.AirportCreateService;
import com.example.demo.flight.service.airport.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportCreateService airportCreateService;

    @Override
    public Airport createAirport(CreateAirportRequest createAirportRequest) {
        return airportCreateService.createAirport(createAirportRequest);
    }

}

package com.example.demo.flight.controller;

import com.example.demo.common.model.dto.response.CustomResponse;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.dto.request.airport.CreateAirportRequest;
import com.example.demo.flight.model.dto.request.flight.CreateFlightRequest;
import com.example.demo.flight.model.mapper.airport.AirportToAirportResponseMapper;
import com.example.demo.flight.model.mapper.airport.CustomPageAirportToCustomPagingAirportResponseMapper;
import com.example.demo.flight.model.mapper.flight.CustomPageFlightToCustomPagingFlightResponseMapper;
import com.example.demo.flight.model.mapper.flight.FlightEntityToFlightMapper;
import com.example.demo.flight.model.mapper.flight.FlightToFlightResponseMapper;
import com.example.demo.flight.service.flight.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/flights")
@Validated
@Tag(name = "Flight Management",
        description = "Endpoints for managing flights including creation, retrieval, updating, and deletion.")
public class FlightController {

    private final FlightService flightService;

    private final FlightToFlightResponseMapper flightToFlightResponseMapper  =  FlightToFlightResponseMapper.initialize();

    private final CustomPageFlightToCustomPagingFlightResponseMapper customPageFlightToCustomPagingFlightResponseMapper
            = CustomPageFlightToCustomPagingFlightResponseMapper.initialize();


    /**
     * Creates a new flight and saves it to the database.
     *
     * @param createFlightRequest the request body containing details of the flight to be created.
     * @return a {@link CustomResponse} containing the ID of the newly created flight.
     */
    @Operation(
            summary = "Create a new flight",
            description = "Creates a new flight and saves it to the database. Accessible by ADMIN only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Flight successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid flight details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden")
            }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<String> createFlight(@RequestBody @Valid final CreateFlightRequest createFlightRequest){
        final Flight savedFlight = flightService.createFlight(createFlightRequest);

        return CustomResponse.successOf(savedFlight.getId());
    }

}

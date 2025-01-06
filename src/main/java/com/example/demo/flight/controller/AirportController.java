package com.example.demo.flight.controller;

import com.example.demo.common.model.dto.response.CustomResponse;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;
import com.example.demo.flight.model.mapper.AirportToAirportResponseMapper;
import com.example.demo.flight.service.airport.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/airports")
@Validated
@Tag(name = "Airport Management",
     description = "Endpoints for managing airports including creation, retrieval, updating, and deletion.")
public class AirportController {

    private final AirportService airportService;
    private final AirportToAirportResponseMapper airportToAirportResponseMapper  =  AirportToAirportResponseMapper.initialize();


    /**
     * Creates a new airport and saves it to the database.
     *
     * @param createAirportRequest the request body containing the details of the airport to be created.
     *                             It must include a valid airport name and city name.
     * @return a {@link CustomResponse} containing the ID of the newly created airport.
     */
    @Operation(
            summary = "Create a new airport",
            description = "Creates a new airport and saves it to the database. Accessible by ADMIN only.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Airport successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid airport details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden")
            }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<String> createAirport(@RequestBody @Valid final CreateAirportRequest createAirportRequest){
        final Airport createdTask = airportService.createAirport(createAirportRequest);

        return CustomResponse.successOf(createdTask.getId());
    }

}

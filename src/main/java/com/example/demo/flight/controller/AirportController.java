package com.example.demo.flight.controller;

import com.example.demo.common.model.CustomPage;
import com.example.demo.common.model.dto.response.CustomPagingResponse;
import com.example.demo.common.model.dto.response.CustomResponse;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.AirportPagingRequest;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;
import com.example.demo.flight.model.dto.response.AirportResponse;
import com.example.demo.flight.model.mapper.AirportToAirportResponseMapper;
import com.example.demo.flight.model.mapper.CustomPageAirportToCustomPagingAirportResponseMapper;
import com.example.demo.flight.service.airport.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
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

    private final CustomPageAirportToCustomPagingAirportResponseMapper customPageAirportToCustomPagingAirportResponseMapper
            = CustomPageAirportToCustomPagingAirportResponseMapper.initialize();


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
        final Airport savedAirport = airportService.createAirport(createAirportRequest);

        return CustomResponse.successOf(savedAirport.getId());
    }

    /**
     * Retrieves an airport by its ID.
     *
     * @param id the ID of the airport to be retrieved.
     * @return a response containing the airport details.
     */
    @Operation(
            summary = "Get airport by ID",
            description = "Retrieves a airport by its ID. Accessible by both ADMIN and USER roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Airport successfully retrieved"),
                    @ApiResponse(responseCode = "400", description = "Invalid airport details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden"),
                    @ApiResponse(responseCode = "404", description = "Airport not found")
            }
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<AirportResponse> getAirportById(@PathVariable @Valid @UUID final String id){
        final Airport airport = airportService.getAirportById(id);

        final AirportResponse response = airportToAirportResponseMapper.map(airport);

        return CustomResponse.successOf(response);
    }

    /**
     * Retrieves a paginated list of airports.
     *
     * @param request the request body containing pagination and sorting information.
     * @return a paginated response containing a list of airports.
     */
    @Operation(
            summary = "Get all airports",
            description = "Retrieves a paginated list of airports. Accessible by both ADMIN and USER roles.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Airports successfully retrieved"),
                    @ApiResponse(responseCode = "400", description = "Invalid airports details provided"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized, authentication is required"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden"),
                    @ApiResponse(responseCode = "404", description = "Airport not found")
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<CustomPagingResponse<AirportResponse>> getAllAirports(@RequestBody @Valid final AirportPagingRequest request){
        final CustomPage<Airport> airportCustomPage= airportService.getAllAirports(request);

        final CustomPagingResponse<AirportResponse> response = customPageAirportToCustomPagingAirportResponseMapper
                .toPagingResponse(airportCustomPage);

        return CustomResponse.successOf(response);
    }

}

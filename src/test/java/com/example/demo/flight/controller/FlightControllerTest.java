package com.example.demo.flight.controller;

import com.example.demo.base.AbstractRestControllerTest;
import com.example.demo.builder.CreateFlightRequestBuilder;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.dto.request.flight.CreateFlightRequest;
import com.example.demo.flight.model.mapper.flight.CustomPageFlightToCustomPagingFlightResponseMapper;
import com.example.demo.flight.model.mapper.flight.FlightToFlightResponseMapper;
import com.example.demo.flight.service.airport.AirportService;
import com.example.demo.flight.service.flight.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlightControllerTest extends AbstractRestControllerTest {

    @MockitoBean
    FlightService flightService;

    private final FlightToFlightResponseMapper flightToFlightResponseMapper  =  FlightToFlightResponseMapper.initialize();

    private final CustomPageFlightToCustomPagingFlightResponseMapper customPageFlightToCustomPagingFlightResponseMapper
            = CustomPageFlightToCustomPagingFlightResponseMapper.initialize();

    @Test
    void givenValidCreateFlightRequestByAdmin_whenCreateFlight_thenSuccess() throws Exception {

        // Given
        final LocalDateTime now = LocalDateTime.now();

        final CreateFlightRequest request = new CreateFlightRequestBuilder()
                .withValidFields()
                .withDepartureTime(now.plusDays(1)) // Departure time is 1 day in the future
                .withArrivalTime(now.plusDays(1).plusHours(2)) // Arrival time is 2 hours after departure
                .build();

        final Flight expectedFlight = Flight.builder()
                .id(request.getId())
                .fromAirport(Airport.builder().id(request.getFromAirportId()).build())
                .toAirport(Airport.builder().id(request.getToAirportId()).build())
                .departureTime(request.getDepartureTime())
                .arrivalTime(request.getArrivalTime())
                .price(request.getPrice())
                .build();

        // When
        when(flightService.createFlight(any(CreateFlightRequest.class))).thenReturn(expectedFlight);

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/flights")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken())
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value(expectedFlight.getId()));

        // Verify
        verify(flightService, times(1)).createFlight(any(CreateFlightRequest.class));

    }

    @Test
    void givenValidCreateFlightRequest_whenUnauthorized_thenThrowUnAuthorizedException() throws Exception {

        // Given
        final LocalDateTime now = LocalDateTime.now();

        final CreateFlightRequest request = new CreateFlightRequestBuilder()
                .withValidFields()
                .withDepartureTime(now.plusDays(1)) // Departure time is 1 day in the future
                .withArrivalTime(now.plusDays(1).plusHours(2)) // Arrival time is 2 hours after departure
                .build();

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/flights")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // Verify
        verify(flightService, never()).createFlight(any(CreateFlightRequest.class));

    }

    @Test
    void givenValidCreateFlightRequest_whenForbiddenByUser_thenThrowForbidden() throws Exception {

        // Given
        final LocalDateTime now = LocalDateTime.now();

        final CreateFlightRequest request = new CreateFlightRequestBuilder()
                .withValidFields()
                .withDepartureTime(now.plusDays(1)) // Departure time is 1 day in the future
                .withArrivalTime(now.plusDays(1).plusHours(2)) // Arrival time is 2 hours after departure
                .build();

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/flights")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        // Verify
        verify(flightService, never()).createFlight(any(CreateFlightRequest.class));

    }

    @Test
    void givenInvalidArrivalTime_whenCreateFlight_thenThrowsValidationException() throws Exception {

        // Given
        final LocalDateTime now = LocalDateTime.now();

        final CreateFlightRequest invalidRequest = new CreateFlightRequestBuilder()
                .withValidFields()
                .withDepartureTime(now.plusDays(1)) // Departure time is 1 day in the future
                .withArrivalTime(now.plusDays(1).minusHours(1)) // Invalid: Arrival time is 1 hour before departure
                .build();

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/flights")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Validation failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subErrors[0].message").value("Arrival time cannot be earlier than departure time!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.subErrors[0].field").value("createFlightRequest"));

        // Verify
        verify(flightService, never()).createFlight(any(CreateFlightRequest.class));

    }

}
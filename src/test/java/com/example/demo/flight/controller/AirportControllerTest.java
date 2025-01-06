package com.example.demo.flight.controller;

import com.example.demo.base.AbstractRestControllerTest;
import com.example.demo.builder.CreateAirportRequestBuilder;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;
import com.example.demo.flight.model.mapper.AirportToAirportResponseMapper;
import com.example.demo.flight.service.airport.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AirportControllerTest extends AbstractRestControllerTest {

    @MockitoBean
    AirportService airportService;

    private final AirportToAirportResponseMapper airportToAirportResponseMapper =
            AirportToAirportResponseMapper.initialize();

    @Test
    void givenValidCreateAirportRequestByAdmin_whenCreateAirport_thenSuccess() throws Exception{

        // Given
        final CreateAirportRequest request = new CreateAirportRequestBuilder()
                .withValidFields().build();

        final Airport expectedAirport = Airport.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName())
                .build();

        // When
        when(airportService.createAirport(any(CreateAirportRequest.class))).thenReturn(expectedAirport);

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/airports")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header(HttpHeaders.AUTHORIZATION,"Bearer " + mockAdminToken.getAccessToken())

                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value(expectedAirport.getId()));

        // Verify
        verify(airportService,times(1)).createAirport(any(CreateAirportRequest.class));

    }

    @Test
    void givenValidCreateAirportRequest_WhenWithUserCreate_ThenThrowUnAuthorizeException() throws Exception{

        // Given
        final CreateAirportRequest request = new CreateAirportRequestBuilder()
                .withValidFields().build();

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/airports")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // Verify
        verify(airportService, never()).createAirport(any(CreateAirportRequest.class));

    }

    @Test
    void givenValidCreateAirportRequest_whenForbiddenThroughUser_thenThrowForbidden() throws Exception {

        // Given
        final CreateAirportRequest request = new CreateAirportRequestBuilder()
                .withValidFields().build();

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/api/v1/airports")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        // Verify
        verify(airportService, never()).createAirport(any(CreateAirportRequest.class));

    }

}
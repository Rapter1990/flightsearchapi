package com.example.demo.flight.controller;

import com.example.demo.base.AbstractRestControllerTest;
import com.example.demo.builder.AirportBuilder;
import com.example.demo.builder.CreateAirportRequestBuilder;
import com.example.demo.flight.exception.AirportNotFoundException;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;
import com.example.demo.flight.model.dto.response.AirportResponse;
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

/**
 * Integration test class for the {@link AirportController}.
 * This class tests the REST endpoints of the {@link AirportController} to ensure that task-related
 * operations are performed correctly.
 */
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

    @Test
    void givenExistAirportId_whenGetAirportByIdWithAdmin_thenReturnCustomResponse() throws Exception{

        // Given
        final String mockAirportId = UUID.randomUUID().toString();

        final Airport mockAirport = new AirportBuilder()
                .withId(mockAirportId)
                .withValidFields();

        final AirportResponse expectedResponse = airportToAirportResponseMapper.map(mockAirport);

        // When
        when(airportService.getAirportById(mockAirportId)).thenReturn(mockAirport);

        // Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/airports/{id}",mockAirportId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(expectedResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name").value(expectedResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        verify(airportService, times(1)).getAirportById(mockAirportId);

    }

    @Test
    void givenExistAirportId_whenGetAirportByIdWithUser_thenReturnCustomResponse() throws Exception{

        // Given
        final String mockAirportId = UUID.randomUUID().toString();

        final Airport mockAirport = new AirportBuilder()
                .withId(mockAirportId)
                .withValidFields();

        final AirportResponse expectedResponse = airportToAirportResponseMapper.map(mockAirport);

        // When
        when(airportService.getAirportById(mockAirportId)).thenReturn(mockAirport);

        // Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/airports/{id}",mockAirportId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(expectedResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name").value(expectedResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"));

        // Verify
        verify(airportService, times(1)).getAirportById(mockAirportId);

    }

    @Test
    void givenNonExistAirportId_whenGetAirportByIdWithAdmin_thenThrowAirportNotFoundException() throws Exception {

        // Given
        final String nonExistentTaskId = UUID.randomUUID().toString();
        final String expectedMessage = "Airport not found!\n Airport not found with ID: " + nonExistentTaskId;

        // When
        when(airportService.getAirportById(nonExistentTaskId))
                .thenThrow(new AirportNotFoundException("Airport not found with ID: " + nonExistentTaskId));

        // Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/airports/{id}", nonExistentTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header").value("NOT EXIST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false));

        // Verify
        verify(airportService, times(1)).getAirportById(nonExistentTaskId);

    }


    @Test
    void givenNonExistAirportId_whenGetAirportByIdWithUser_thenThrowAirportNotFoundException() throws Exception {

        // Given
        final String nonExistentTaskId = UUID.randomUUID().toString();
        final String expectedMessage = "Airport not found!\n Airport not found with ID: " + nonExistentTaskId;

        // When
        when(airportService.getAirportById(nonExistentTaskId))
                .thenThrow(new AirportNotFoundException("Airport not found with ID: " + nonExistentTaskId));

        // Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/airports/{id}", nonExistentTaskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.header").value("NOT EXIST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false));

        // Verify
        verify(airportService, times(1)).getAirportById(nonExistentTaskId);

    }

    @Test
    void givenExistAirportId_whenUserUnauthorized_thenReturnUnauthorized() throws Exception {

        // Given
        final String mockAirportId = UUID.randomUUID().toString();

        final Airport mockAirport = new AirportBuilder()
                .withId(mockAirportId)
                .withValidFields();

        // When
        when(airportService.getAirportById(mockAirportId)).thenReturn(mockAirport);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/airports/{id}",mockAirportId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // Verify
        verify(airportService,never()).getAirportById(mockAirportId);

    }

}
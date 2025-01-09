package com.example.demo.flight.controller;

import com.example.demo.base.AbstractRestControllerTest;
import com.example.demo.builder.AirportBuilder;
import com.example.demo.builder.AirportEntityBuilder;
import com.example.demo.builder.CreateAirportRequestBuilder;
import com.example.demo.builder.UpdateAirportRequestBuilder;
import com.example.demo.common.model.CustomPage;
import com.example.demo.common.model.CustomPaging;
import com.example.demo.common.model.dto.response.CustomPagingResponse;
import com.example.demo.flight.exception.AirportNameAlreadyExistException;
import com.example.demo.flight.exception.AirportNotFoundException;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.dto.request.AirportPagingRequest;
import com.example.demo.flight.model.dto.request.CreateAirportRequest;
import com.example.demo.flight.model.dto.request.UpdateAirportRequest;
import com.example.demo.flight.model.dto.response.AirportResponse;
import com.example.demo.flight.model.entity.AirportEntity;
import com.example.demo.flight.model.mapper.AirportToAirportResponseMapper;
import com.example.demo.flight.model.mapper.CustomPageAirportToCustomPagingAirportResponseMapper;
import com.example.demo.flight.service.airport.AirportService;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    private final CustomPageAirportToCustomPagingAirportResponseMapper customPageAirportToCustomPagingAirportResponseMapper
            = CustomPageAirportToCustomPagingAirportResponseMapper.initialize();

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

    @Test
    void givenTaskPagingRequest_whenGetTasksFromAdmin_thenReturnCustomPageTask() throws Exception {

        // Given
        final AirportPagingRequest pagingRequest = AirportPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        final String airportId = UUID.randomUUID().toString();

        final AirportEntity expectedEntity = new AirportEntityBuilder()
                .withId(airportId)
                .withValidFields();

        final List<AirportEntity> airportEntities = List.of(expectedEntity);

        final Page<AirportEntity> airportEntityPage = new PageImpl<>(airportEntities, PageRequest.of(1, 1), airportEntities.size());

        final List<Airport> airportDomainModels = airportEntities.stream()
                .map(entity -> new Airport(entity.getId(), entity.getName(), entity.getCityName()))
                .collect(Collectors.toList());

        final CustomPage<Airport> taskPage = CustomPage.of(airportDomainModels, airportEntityPage);

        final CustomPagingResponse<AirportResponse> expectedResponse =
                customPageAirportToCustomPagingAirportResponseMapper.toPagingResponse(taskPage);

        // When
        when(airportService.getAllAirports(any(AirportPagingRequest.class))).thenReturn(taskPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].id").value(expectedResponse.getContent().get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].name").value(expectedResponse.getContent().get(0).getName()));

        // Verify
        verify(airportService, times(1)).getAllAirports(any(AirportPagingRequest.class));

    }

    @Test
    void givenTaskPagingRequest_whenGetTasksFromUser_thenReturnCustomPageTask() throws Exception {

        // Given
        final AirportPagingRequest pagingRequest = AirportPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        final String airportId = UUID.randomUUID().toString();

        final AirportEntity expectedEntity = new AirportEntityBuilder()
                .withId(airportId)
                .withValidFields();

        final List<AirportEntity> airportEntities = List.of(expectedEntity);

        final Page<AirportEntity> airportEntityPage = new PageImpl<>(airportEntities, PageRequest.of(1, 1), airportEntities.size());

        final List<Airport> airportDomainModels = airportEntities.stream()
                .map(entity -> new Airport(entity.getId(), entity.getName(), entity.getCityName()))
                .collect(Collectors.toList());

        final CustomPage<Airport> taskPage = CustomPage.of(airportDomainModels, airportEntityPage);

        final CustomPagingResponse<AirportResponse> expectedResponse =
                customPageAirportToCustomPagingAirportResponseMapper.toPagingResponse(taskPage);

        // When
        when(airportService.getAllAirports(any(AirportPagingRequest.class))).thenReturn(taskPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].id").value(expectedResponse.getContent().get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].name").value(expectedResponse.getContent().get(0).getName()));

        // Verify
        verify(airportService, times(1)).getAllAirports(any(AirportPagingRequest.class));

    }

    @Test
    void givenTaskPagingRequest_WhenUnauthorized_thenThrowUnauthorized() throws Exception {

        // Given
        final AirportPagingRequest pagingRequest = AirportPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/airports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // Verify
        verify(airportService, never()).getAllAirports(any(AirportPagingRequest.class));

    }

    @Test
    void givenValidTaskUpdate_WithAdminUpdate_whenUpdateTask_thenSuccess() throws Exception{

        // Given
        final String mockId = UUID.randomUUID().toString();

        final UpdateAirportRequest request = new UpdateAirportRequestBuilder()
                .withValidFields()
                .build();

        final Airport expectedAirport = new AirportBuilder()
                .withId(mockId)
                .withName(request.getName())
                .withCityName(request.getCityName())
                .build();

        // When
        when(airportService.updateAirportById(anyString(),any(UpdateAirportRequest.class)))
                .thenReturn(expectedAirport);

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/airports/{id}",mockId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header(HttpHeaders.AUTHORIZATION,"Bearer " + mockAdminToken.getAccessToken())

                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(expectedAirport.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name").value(expectedAirport.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.cityName").value(expectedAirport.getCityName()));

        // Verify
        verify(airportService,times(1))
                .updateAirportById(anyString(),any(UpdateAirportRequest.class));

    }

    @Test
    void givenTaskWithDuplicateName_whenUpdateTask_thenThrowTaskWithThisNameAlreadyExistException() throws Exception {

        // Given
        final String mockId = UUID.randomUUID().toString();

        final UpdateAirportRequest request = new UpdateAirportRequestBuilder()
                .withName("Airport Duplicate Name")
                .withCityName("City Name")
                .build();

        // When
        when(airportService.updateAirportById(anyString(), any(UpdateAirportRequest.class)))
                .thenThrow(new AirportNameAlreadyExistException("With given task name = " + request.getName()));

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/airports/{id}", mockId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Airport with this name already exist\n With given task name = " + request.getName()));

        // Verify
        verify(airportService, times(1))
                .updateAirportById(anyString(), any(UpdateAirportRequest.class));

    }

    @Test
    void givenInvalidTaskId_whenUpdateTask_thenThrowNotFoundException() throws Exception {

        // Given
        final String nonExistentTaskId = UUID.randomUUID().toString();

        final UpdateAirportRequest request = new UpdateAirportRequestBuilder()
                .withValidFields()
                .build();

        final String expectedMessage = "Airport not found!\n Airport not found with ID: " + nonExistentTaskId;

        // When
        when(airportService.updateAirportById(anyString(), any(UpdateAirportRequest.class)))
                .thenThrow(new AirportNotFoundException("Airport not found with ID: " + nonExistentTaskId));

        // Then
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/api/v1/airports/{id}", nonExistentTaskId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken())
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("NOT_FOUND"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(expectedMessage))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(false));

        // Verify
        verify(airportService, times(1)).updateAirportById(anyString(), any(UpdateAirportRequest.class));

    }


    @Test
    void givenValidUpdateTaskRequest_whenUserUnAuthorized_thenReturnUnauthorized() throws Exception {

        // Given
        final String mockId = UUID.randomUUID().toString();

        final UpdateAirportRequest request = new UpdateAirportRequestBuilder()
                .withValidFields()
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/airports/{id}",mockId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + mockUserToken.getAccessToken())
                ).andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        // Verify
        verify(airportService,never()).updateAirportById(anyString(), any(UpdateAirportRequest.class));

    }

    @Test
    void givenValidUpdateRequest_whenUserNotAuthenticated_thenThrowUnAuthorize() throws Exception{

        // Given
        final String mockId = UUID.randomUUID().toString();

        final UpdateAirportRequest request = new UpdateAirportRequestBuilder()
                .withValidFields()
                .build();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/tasks/{id}",mockId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // Verify
        verify(airportService,never()).updateAirportById(anyString(), any(UpdateAirportRequest.class));

    }


}
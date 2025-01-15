package com.example.demo.flight.service.flight.impl;

import com.example.demo.base.AbstractBaseServiceTest;
import com.example.demo.builder.AirportBuilder;
import com.example.demo.builder.CreateFlightRequestBuilder;
import com.example.demo.builder.FlightBuilder;
import com.example.demo.common.model.CustomPage;
import com.example.demo.common.model.CustomPaging;
import com.example.demo.common.model.dto.request.CustomPagingRequest;
import com.example.demo.flight.model.Airport;
import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.dto.request.flight.CreateFlightRequest;
import com.example.demo.flight.model.entity.AirportEntity;
import com.example.demo.flight.service.flight.FlightCreateService;
import com.example.demo.flight.service.flight.FlightReadService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link FlightServiceImpl}.
 * This class verifies the correctness of the business logic in {@link FlightServiceImpl}.
 */
class FlightServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private FlightServiceImpl flightService;

    @Mock
    private FlightCreateService flightCreateService;

    @Mock
    private FlightReadService flightReadService;

    @Test
    void givenValidCreateFlightRequest_whenCreateFlight_ThenReturnFlight() {

        // Given
        final CreateFlightRequest request = new CreateFlightRequestBuilder()
                .withValidFields()
                .build();

        final Airport departureAirport = new AirportBuilder().withValidFields();

        final Airport arrivalAirport = new AirportBuilder().withValidFields();

        final Flight mockFlight = new FlightBuilder().withValidFields()
                .withFromAirport(departureAirport)
                .withToAirport(arrivalAirport)
                .build();

        // When
        when(flightCreateService.createFlight(any(CreateFlightRequest.class))).thenReturn(mockFlight);

        // Then
        Flight response = flightService.createFlight(request);

        assertNotNull(response);
        assertEquals(mockFlight.getId(), response.getId());
        assertEquals(mockFlight.getFromAirport(), response.getFromAirport());
        assertEquals(mockFlight.getToAirport(), response.getToAirport());
        assertEquals(mockFlight.getDepartureTime(), response.getDepartureTime());
        assertEquals(mockFlight.getArrivalTime(), response.getArrivalTime());
        assertEquals(mockFlight.getPrice(), response.getPrice());

        // Verify
        verify(flightCreateService, times(1)).createFlight(any(CreateFlightRequest.class));

    }

    @Test
    void givenValidFlightId_whenGetFlightById_thenReturnFlight() {

        // Given
        final String flightId = "FLIGHT123";

        final Airport departureAirport = new AirportBuilder().withValidFields();

        final Airport arrivalAirport = new AirportBuilder().withValidFields();

        final Flight mockFlight = new FlightBuilder().withValidFields()
                .withFromAirport(departureAirport)
                .withToAirport(arrivalAirport)
                .build();

        // When
        when(flightReadService.getFlightById(flightId)).thenReturn(mockFlight);

        // Then
        Flight response = flightService.getFlightById(flightId);

        assertNotNull(response);
        assertEquals(mockFlight.getId(), response.getId());
        assertEquals(mockFlight.getFromAirport(), response.getFromAirport());
        assertEquals(mockFlight.getToAirport(), response.getToAirport());
        assertEquals(mockFlight.getDepartureTime(), response.getDepartureTime());
        assertEquals(mockFlight.getArrivalTime(), response.getArrivalTime());
        assertEquals(mockFlight.getPrice(), response.getPrice());

        // Verify
        verify(flightReadService, times(1)).getFlightById(flightId);
    }

    @Test
    void givenValidPagingRequest_whenGetAllFlights_thenReturnCustomPageOfFlights() {

        // Given
        CustomPaging paging = CustomPaging.builder()
                .pageSize(1)
                .pageNumber(1)
                .build();

        CustomPagingRequest pagingRequest = mock(CustomPagingRequest.class);

        List<Flight> flightList = List.of(new Flight());
        CustomPage<Flight> expectedPage = CustomPage.<Flight>builder()
                .content(flightList)
                .pageNumber(1)
                .pageSize(1)
                .totalElementCount(1L)
                .totalPageCount(1)
                .build();


        // When
        when(pagingRequest.getPagination()).thenReturn(paging);
        when(pagingRequest.toPageable()).thenReturn(PageRequest.of(paging.getPageNumber(), paging.getPageSize()));
        when(flightReadService.getAllFlights(pagingRequest)).thenReturn(expectedPage);

        // Then
        CustomPage<Flight> result = flightService.getAllFlights(pagingRequest);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        assertEquals(1, result.getContent().size());

        // Verify
        verify(flightReadService, times(1)).getAllFlights(pagingRequest);

    }


}
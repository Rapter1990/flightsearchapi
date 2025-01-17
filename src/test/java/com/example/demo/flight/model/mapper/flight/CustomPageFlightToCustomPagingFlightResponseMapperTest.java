package com.example.demo.flight.model.mapper.flight;

import com.example.demo.common.model.CustomPage;
import com.example.demo.common.model.dto.response.CustomPagingResponse;
import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.dto.response.flight.FlightResponse;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomPageFlightToCustomPagingFlightResponseMapperTest {

    private final CustomPageFlightToCustomPagingFlightResponseMapper mapper =
            CustomPageFlightToCustomPagingFlightResponseMapper.initialize();

    @Test
    void testToPagingResponse_WhenFlightPageIsNull() {

        CustomPage<Flight> flightPage = null;

        CustomPagingResponse<FlightResponse> result = mapper.toPagingResponse(flightPage);

        assertNull(result);

    }

    @Test
    void testToFlightResponseList_WhenFlightsIsNull() {

        List<Flight> flights = null;

        List<FlightResponse> result = mapper.toFlightResponseList(flights);

        assertNull(result);
    }

    @Test
    void testToFlightResponseList_WhenFlightsIsEmpty() {

        List<Flight> flights = Collections.emptyList();

        List<FlightResponse> result = mapper.toFlightResponseList(flights);

        assertNotNull(result);
        assertTrue(result.isEmpty());

    }


}
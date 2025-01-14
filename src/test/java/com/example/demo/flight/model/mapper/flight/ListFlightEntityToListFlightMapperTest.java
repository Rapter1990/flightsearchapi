package com.example.demo.flight.model.mapper.flight;

import com.example.demo.flight.model.Flight;
import com.example.demo.flight.model.entity.FlightEntity;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListFlightEntityToListFlightMapperTest {

    private final ListFlightEntityToListFlightMapper mapper = ListFlightEntityToListFlightMapper.initialize();

    @Test
    void testToFlightList_WhenFlightEntitiesIsNull() {

        List<FlightEntity> flightEntities = null;

        List<Flight> result = mapper.toFlightList(flightEntities);

        assertNull(result);

    }

    @Test
    void testToFlightList_WhenFlightEntitiesIsEmpty() {

        List<FlightEntity> flightEntities = Collections.emptyList();

        List<Flight> result = mapper.toFlightList(flightEntities);

        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

}
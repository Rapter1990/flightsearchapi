package com.example.demo.flight.model.dto.response.flight;

import com.example.demo.flight.model.Airport;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Response class representing a flight.
 * This class provides information about a flight, including:
 * - The unique flight ID.
 * - The departure and arrival airports.
 * - The departure and arrival times.
 * - The ticket price.
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FlightResponse {

    private String id;
    private Airport fromAirport;
    private Airport toAirport;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Double price;

}

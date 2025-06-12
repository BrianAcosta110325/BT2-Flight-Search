package com.encora.utils;

import java.util.List;
import java.time.LocalDate;
import com.encora.flight_search_be.dto.FlightSearchResponseDto;

public interface FlightService {
    List<FlightSearchResponseDto> searchFlights(
        String departureCode,
        String arrivalCode,
        LocalDate departureDate,
        Integer noAdults,
        String currency,
        boolean nonStop
    );

    List<String> searchAirports(String query);
}
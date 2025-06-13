package com.encora.utils;

import java.util.List;
// import java.util.Map;
import java.time.LocalDate;

// import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto;
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

    String searchAirportByCode(String code);

    // FlightSearchDetailedResponseDto searchFlightById(String id, Map<String, String> param);
}
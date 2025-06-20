package com.encora.utils;

import java.util.List;
import java.time.LocalDate;

import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto;
import com.encora.flight_search_be.dto.SearchFlightResponseDto;

public interface FlightService {
    SearchFlightResponseDto searchFlights(
        String page,
        String departureCode,
        String arrivalCode,
        LocalDate departureDate,
        Integer noAdults,
        String currency,
        boolean nonStop
    );

    List<String> searchAirports(String query);

    String searchAirportByCode(String code);

    FlightSearchDetailedResponseDto searchFlightById(
        String departureCode,
        String arrivalCode,
        LocalDate departureDate,
        Integer noAdults,
        String currency,
        boolean nonStop,
        String id
    );
}
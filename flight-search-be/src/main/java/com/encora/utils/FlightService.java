package com.encora.utils;

import java.util.List;
import java.time.LocalDate;

import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto;
import com.encora.flight_search_be.dto.AirportDto;
import com.encora.flight_search_be.dto.FlightSearchResponseDto;

public interface FlightService {
    List<FlightSearchResponseDto> searchFlights(
        String page,
        String departureCode,
        String arrivalCode,
        LocalDate departureDate,
        Integer noAdults,
        String currency,
        Boolean nonStop
    );

    List<AirportDto> searchAirports(String query);

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
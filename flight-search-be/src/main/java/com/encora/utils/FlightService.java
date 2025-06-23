package com.encora.utils;

import java.util.List;
import java.time.LocalDate;

import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto;
import com.encora.flight_search_be.dto.AirportDto;
import com.encora.flight_search_be.dto.SearchFlightResponseDto;

public interface FlightService {
    SearchFlightResponseDto searchFlights(
        String page,
        String originAirportCode,
        String destinationAirportCode,
        LocalDate departureDate,
        Integer numberOfAdults,
        String currencyCode,
        Boolean onlyNonStopFlights,
        Sorter sortBy
    );

    List<AirportDto> searchAirports(String query);

    String searchAirportByCode(String code);

    String searchAirlineByCode(String code);

    FlightSearchDetailedResponseDto searchFlightById(
        String id
    );
}
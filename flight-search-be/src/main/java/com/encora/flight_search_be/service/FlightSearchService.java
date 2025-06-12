package com.encora.flight_search_be.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.encora.flight_search_be.dto.FlightSearchResponseDto;
import com.encora.utils.FlightService;

@Service
public class FlightSearchService implements FlightService {
    
    @Override
    public List<FlightSearchResponseDto> searchFlights(
        String departureCode,
        String arrivalCode,
        LocalDate departureDate,
        Integer noAdults,
        String currency,
        boolean nonStop
    ) {
        // TODO: Integrar con la API de Amadeus
        // Por ahora, devolvemos datos simulados

        List<FlightSearchResponseDto> mockResults = new ArrayList<>();

        FlightSearchResponseDto mockFlight = new FlightSearchResponseDto();
        mockFlight.setOriginAirport("MEX - Mexico City");
        mockFlight.setDestinationAirport("JFK - New York");
        mockFlight.setDepartureDateTime(departureDate + "T08:00");
        mockFlight.setArrivalDateTime(departureDate + "T15:30");
        mockFlight.setAirline("Aeromexico (AM)");
        mockFlight.setDuration("7h 30m");
        mockFlight.setPrice("450.00 " + currency);
        mockFlight.setPricePerTraveler("450.00 " + currency);
        mockFlight.setNonStop(true);

        mockResults.add(mockFlight);
        return mockResults;
    }
}

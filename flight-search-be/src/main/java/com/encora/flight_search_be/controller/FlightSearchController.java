package com.encora.flight_search_be.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.encora.flight_search_be.dto.FlightSearchResponseDto;
import com.encora.flight_search_be.service.FlightSearchService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightSearchController {

    private final FlightSearchService flightService;
    public FlightSearchController(FlightSearchService flightService) {
        this.flightService = flightService;
    }
    
    @GetMapping("/search_flights")
    public List<FlightSearchResponseDto> searchFlights(
        @RequestParam(required = false) String departureCode,
        @RequestParam(required = false) String arrivalCode, 
        @RequestParam(required = false) LocalDate departureDate,
        @RequestParam(required = false) Integer noAdults,
        @RequestParam(required = false) String currency,
        @RequestParam(required = false) Boolean nonStops
    ) {
        return this.flightService.searchFlights(
            departureCode, 
            arrivalCode, 
            departureDate, 
            noAdults, 
            currency, 
            nonStops != null ? nonStops : false
        );
    }
}

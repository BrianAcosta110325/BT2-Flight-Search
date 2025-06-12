package com.encora.flight_search_be.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.encora.flight_search_be.dto.FlightSearchResponseDto;
import com.encora.flight_search_be.service.FlightSearchService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightSearchController {

    @Autowired
    private FlightSearchService flightService;
    
    @GetMapping("/search_flights")
    public List<FlightSearchResponseDto> searchFlights(
        @RequestParam() String departureCode,
        @RequestParam() String arrivalCode, 
        @RequestParam(required = false) LocalDate departureDate,
        @RequestParam() Integer noAdults,
        @RequestParam() String currency,
        @RequestParam(required = false) Boolean nonStops
    ) {
        if (departureDate == null) {
            departureDate = LocalDate.now();
        }
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

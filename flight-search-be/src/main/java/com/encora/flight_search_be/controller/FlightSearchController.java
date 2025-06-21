package com.encora.flight_search_be.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.encora.flight_search_be.dto.AirportDto;
import com.encora.flight_search_be.dto.FlightSearchResponseDto;
import com.encora.flight_search_be.service.FlightSearchService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightSearchController {

    @Autowired
    private FlightSearchService flightService;
    
    @GetMapping("/searchFlights")
    public List<FlightSearchResponseDto> searchFlights(
        @RequestParam String originAirportCode,
        @RequestParam String destinationAirportCode, 
        @RequestParam(required = false) LocalDate departureDate,
        @RequestParam Integer numberOfAdults,
        @RequestParam String currencyCode,
        @RequestParam(required = false) Boolean onlyNonStopFlights
    ) {
        return this.flightService.searchFlights(
            originAirportCode, 
            destinationAirportCode, 
            departureDate, 
            numberOfAdults, 
            currencyCode, 
            onlyNonStopFlights
        );
    }

    @GetMapping("/searchAirports")
    public List<AirportDto> searchAirports(@RequestParam() String query) {
        return this.flightService.searchAirports(query);
    }
}

package com.encora.flight_search_be.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto;
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
        @RequestParam String page,
        @RequestParam String originAirportCode,
        @RequestParam String destinationAirportCode, 
        @RequestParam(required = false) LocalDate departureDate,
        @RequestParam Integer numberOfAdults,
        @RequestParam String currencyCode,
        @RequestParam(required = false) Boolean onlyNonStopFlights
    ) {
        return this.flightService.searchFlights(
            page,
            originAirportCode, 
            destinationAirportCode, 
            departureDate, 
            numberOfAdults, 
            currencyCode, 
            onlyNonStopFlights
        );
    }

    @GetMapping("/searchFlightById/{id}")
    public FlightSearchDetailedResponseDto searchFlightById(
        @PathVariable String id,
        @RequestParam String departureCode,
        @RequestParam String arrivalCode,
        @RequestParam(required = false) LocalDate departureDate,
        @RequestParam Integer noAdults,
        @RequestParam String currency,
        @RequestParam(required = false) Boolean nonStops
    ) {
        return this.flightService.searchFlightById(
            departureCode, 
            arrivalCode, 
            departureDate, 
            noAdults, 
            currency, 
            nonStops != null ? nonStops : false,
            id
        );
    }

    @GetMapping("/searchAirports/{query}")
    public List<AirportDto> searchAirports(@RequestParam() String query) {
        return this.flightService.searchAirports(query);
    }

    @GetMapping("/searchAirportByCode")
    public String searchAirportByCode(@RequestParam() String code) {
        return this.flightService.searchAirportByCode(code);
    }
}

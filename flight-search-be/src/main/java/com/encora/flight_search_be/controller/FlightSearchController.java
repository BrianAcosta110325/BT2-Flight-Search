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
import com.encora.flight_search_be.dto.SearchFlightResponseDto;
import com.encora.flight_search_be.service.FlightSearchService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightSearchController {

    @Autowired
    private FlightSearchService flightService;
    
    @GetMapping("/searchFlights")
    public SearchFlightResponseDto searchFlights(
        @RequestParam String page,
        @RequestParam String departureCode,
        @RequestParam String arrivalCode, 
        @RequestParam(required = false) LocalDate departureDate,
        @RequestParam(required = false) Integer numberOfAdults,
        @RequestParam String currency,
        @RequestParam(required = false) Boolean nonStops
    ) {
        return this.flightService.searchFlights(
            page,
            departureCode, 
            arrivalCode, 
            departureDate, 
            numberOfAdults, 
            currency, 
            nonStops
        );
    }

    @GetMapping("/searchFlightById/{id}")
    public FlightSearchDetailedResponseDto searchFlightById(
        @PathVariable String id,
        @RequestParam String page,
        @RequestParam String departureCode,
        @RequestParam String arrivalCode,
        @RequestParam(required = false) LocalDate departureDate,
        @RequestParam(required = false) Integer numberOfAdults,
        @RequestParam String currency,
        @RequestParam(required = false) Boolean nonStops
    ) {
        return this.flightService.searchFlightById(
            page,
            departureCode, 
            arrivalCode, 
            departureDate, 
            numberOfAdults, 
            currency, 
            nonStops,
            id
        );
    }

    @GetMapping("/searchAirports/{query}")
    public List<AirportDto> searchAirports(@PathVariable() String query) {
        return this.flightService.searchAirports(query);
    }

    @GetMapping("/searchAirportByCode/{code}")
    public String searchAirportByCode(@PathVariable() String code) {
        return this.flightService.searchAirportByCode(code);
    }

    @GetMapping("/searchAirlineByCode/{code}")
    public String searchAirlineByCode(@PathVariable String code) {
        return this.flightService.searchAirlineByCode(code);
    }
}

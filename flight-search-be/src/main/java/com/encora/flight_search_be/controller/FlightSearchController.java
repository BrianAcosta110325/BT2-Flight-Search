package com.encora.flight_search_be.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;

// import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto;
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

    // @GetMapping("searchFlightById/:id")
    // public FlightSearchDetailedResponseDto searchFlightById(@RequestParam String param, @RequestParam String id) {
    //     return searchFlightById(param, id);
    // }
    

    @GetMapping("/searchAirports")
    public List<String> searchAirports(@RequestParam() String query) {
        // Normalize the query to handle accents and special characters
        String normalizedQuery = query
            .toLowerCase()
            .replaceAll("[áàäâ]", "a")
            .replaceAll("[éèëê]", "e")
            .replaceAll("[íìïî]", "i")
            .replaceAll("[óòöô]", "o")
            .replaceAll("[úùüû]", "u")
            .replaceAll("ñ", "n");
        return this.flightService.searchAirports(normalizedQuery);
    }

    @GetMapping("/searchAirportByCode")
    public String searchAirportByCode(@RequestParam() String code) {
        return this.flightService.searchAirportByCode(code);
    }
}

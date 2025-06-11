package com.encora.flight_search_be.controller;

import java.time.LocalDate;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.encora.flight_search_be.service.FlightSeacrchService;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightSearchController {

    @Autowired
    private FlightService flightService;
    
    @GetMapping("/search_flights")
    public String getFlights(
        @RequestParam(required = false) String departure_code,
        @RequestParam(required = false) Integer arrival_code, 
        @RequestParam(required = false) LocalDate departure_date,
        @RequestParam(required = false) Number no_adults,
        @RequestParam(required = false) String currency,
        @RequestParam(required = false) Boolean nonStops
    ) {
        return this.flightService.getFlights(
            departure_code, 
            arrival_code, 
            departure_date, 
            no_adults, 
            currency, 
            nonStops
        );
    }
}

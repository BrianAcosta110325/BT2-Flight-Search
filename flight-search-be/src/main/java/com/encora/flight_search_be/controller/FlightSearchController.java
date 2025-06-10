package com.encora.flight_search_be.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FlightSearchController {
    
    @GetMapping("/flights")
    public String getFlights() {
        // This is a placeholder for the actual flight search logic
        return "OH YEAH BABY 6";
    }
}

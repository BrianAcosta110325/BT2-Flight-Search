package com.encora.flight_search_be.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightSearchResponseDto {
    private String id;
    private String departureAirportName;
    private String departureAirportCode;
    private String arrivalAirportName;
    private String arrivalAirportCode;
    
    private String departureDateTime;
    private String arrivalDateTime;

    private String airlineName;
    private String airlineCode;

    private String operatingAirlineName;
    private String operatingAirlineCode;

    private String totalFlightDuration;

    private List<FlightSearchStopDto> stops;

    private String totalPrice;
    private String pricePerTraveler;
}
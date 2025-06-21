package com.encora.flight_search_be.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightSearchResponseDto {
    private String originAirport;
    private String destinationAirport;
    private String departureDateTime;
    private String arrivalDateTime;
    private String airline;
    private String duration;
    private String price;
    private String pricePerTraveler;
    private boolean nonStop;
}

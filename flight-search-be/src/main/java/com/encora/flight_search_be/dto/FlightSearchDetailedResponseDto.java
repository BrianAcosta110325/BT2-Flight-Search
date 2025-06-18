package com.encora.flight_search_be.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightSearchDetailedResponseDto {
    private String id;

    // Airports
    private String departureAirportName;
    private String departureAirportCode;
    private String arrivalAirportName;
    private String arrivalAirportCode;

    // DateTime
    private String departureDateTime;
    private String arrivalDateTime;
    private String totalFlightDuration;

    // Airline
    private String airlineName;
    private String airlineCode;
    private String operatingAirlineName;
    private String operatingAirlineCode;

    // Segment details
    private String aircraftCode;
    private String cabin;       
    private String fareClass;
    private List<FlightFareDetailsDto> fareDetails;

    // Amenity
    private List<AmenityDto> amenities;

    // Equipment
    private int includedCheckedBags;
    private int includedCabinBags;

    // Stops
    private List<FlightSearchDetailedStopDto> stops;
    private List<String> layoverDurations;

    // Price
    private String basePrice;
    private String totalPrice;
    private String pricePerTraveler;
    private List<FeeDto> fees;

    @Getter
    @Setter
    public static class FlightFareDetailsDto {
        private String segmentId;
        private String cabin;
        private String travelClass;
        private Integer includedCheckedBags;
        private Integer includedCabinBags;
        private List<AmenityDto> amenities;
    }

    @Getter
    @Setter
    public static class AmenityDto {
        private String description;
        private boolean isChargeable;
    }

    @Getter
    @Setter
    public static class FeeDto {
        private String type;
        private String amount;
    }

    @Getter
    @Setter
    public static class FlightSearchDetailedStopDto {
        private String airportCode;
        private String arrivalTime;
        private String departureTime;
        private String duration;
    }
}

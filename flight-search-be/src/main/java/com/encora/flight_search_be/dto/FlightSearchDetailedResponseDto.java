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

    // Amenity
    private List<AmenityDto> amenities;

    // Equipment
    private int includedCheckedBags;
    private int includedCabinBags;

    // Stops
    private List<FlightSearchStopDto> stops;
    private List<String> layoverDurations;

    // Price
    private String basePrice;
    private String totalPrice;
    private String pricePerTraveler;
    private List<FeeDto> fees;

    public static class AmenityDto {
        private String description;
        private boolean isChargeable;

        // Getters y setters
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public boolean isChargeable() { return isChargeable; }
        public void setChargeable(boolean chargeable) { isChargeable = chargeable; }
    }

    public static class FeeDto {
        private String type;
        private String amount;

        // Getters y setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }

        public String getAmount() { return amount; }
        public void setAmount(String amount) { this.amount = amount; }
    }

    public static class FlightSearchStopDto {
        private String airportCode;
        private String arrivalTime;
        private String departureTime;
        private String duration;

        // Getters y setters
        public String getAirportCode() { return airportCode; }
        public void setAirportCode(String airportCode) { this.airportCode = airportCode; }

        public String getArrivalTime() { return arrivalTime; }
        public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

        public String getDepartureTime() { return departureTime; }
        public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

        public String getDuration() { return duration; }
        public void setDuration(String duration) { this.duration = duration; }
    }
}

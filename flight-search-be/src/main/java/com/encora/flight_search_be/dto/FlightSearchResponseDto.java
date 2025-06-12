package com.encora.flight_search_be.dto;

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

    public String getOriginAirport() {
        return originAirport;
    }
    public void setOriginAirport(String originAirport) {
        this.originAirport = originAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public void setDestinationAirport(String destinationAirport) {
        this.destinationAirport = destinationAirport;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public String getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(String arrivalDateTime) {
        this.arrivalDateTime = arrivalDateTime;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricePerTraveler() {
        return pricePerTraveler;
    }

    public void setPricePerTraveler(String pricePerTraveler) {
        this.pricePerTraveler = pricePerTraveler;
    }

    public boolean isNonStop() {
        return nonStop;
    }

    public void setNonStop(boolean nonStop) {
        this.nonStop = nonStop;
    }
}

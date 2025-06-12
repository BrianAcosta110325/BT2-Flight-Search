package com.encora.flight_search_be.dto;

public class FlightSearchStopDto {
    private String airportName;
    private String airportCode;
    private String layoverDuration;

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getLayoverDuration() {
        return layoverDuration;
    }

    public void setLayoverDuration(String layoverDuration) {
        this.layoverDuration = layoverDuration;
    }
}
package com.encora.flight_search_be.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.encora.flight_search_be.client.AmadeusClient;
import com.encora.flight_search_be.dto.FlightSearchAmadeusResposeDto;
import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto;
import com.encora.flight_search_be.dto.FlightSearchResponseDto;
import com.encora.utils.FlightService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class FlightSearchService implements FlightService {

    @Autowired
    private AmadeusClient amadeusClient;

    @Override
    public List<FlightSearchResponseDto> searchFlights(
        String originLocationCode,
        String destinationLocationCode,
        LocalDate departureDate,
        Integer adults,
        String currencyCode,
        boolean nonStop
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("originLocationCode", originLocationCode);
        params.put("destinationLocationCode", destinationLocationCode);
        params.put("departureDate", departureDate.toString());
        params.put("adults", String.valueOf(adults));
        params.put("currencyCode", currencyCode);
        params.put("nonStop", String.valueOf(nonStop));
        params.put("max", "10");

        JsonNode data = amadeusClient.searchFlights(params);
        ObjectMapper mapper = new ObjectMapper();
        List<FlightSearchAmadeusResposeDto> flights = new ArrayList<>();

        try {
            flights = mapper.convertValue(
                data,
                new TypeReference<List<FlightSearchAmadeusResposeDto>>() {}
            );

            List<FlightSearchResponseDto> result = new ArrayList<FlightSearchResponseDto>();

            for (FlightSearchAmadeusResposeDto flight : flights) {
                FlightSearchResponseDto dto = new FlightSearchResponseDto(flight, amadeusClient);
                result.add(dto);
            }

            return result;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public FlightSearchDetailedResponseDto searchFlightById(
        String departureCode,
        String arrivalCode,
        LocalDate departureDate,
        Integer noAdults,
        String currency,
        boolean nonStop,
        String flightId
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("originLocationCode", departureCode);
        params.put("destinationLocationCode", arrivalCode);
        params.put("departureDate", departureDate.toString());
        params.put("adults", String.valueOf(noAdults));
        params.put("currencyCode", currency);
        params.put("nonStop", String.valueOf(nonStop));
        params.put("max", "10");

        JsonNode data = amadeusClient.searchFlights(params);
        ObjectMapper mapper = new ObjectMapper();
        List<FlightSearchAmadeusResposeDto> flights = new ArrayList<>();

        try {
            flights = mapper.convertValue(
                data,
                new TypeReference<List<FlightSearchAmadeusResposeDto>>() {}
            );

            List<FlightSearchDetailedResponseDto> result = new ArrayList<FlightSearchDetailedResponseDto>();
            for (FlightSearchAmadeusResposeDto flight : flights) {
                if (flight.getId().equals(flightId)) {
                    FlightSearchDetailedResponseDto dto = new FlightSearchDetailedResponseDto(flight, amadeusClient);
                    result.add(dto);
                    return dto;
                }
            }
            return new FlightSearchDetailedResponseDto();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new FlightSearchDetailedResponseDto();
        }
    }

    @Override
    public List<String> searchAirports(String query) {
        ResponseEntity<String> response = amadeusClient.searchAirports(query);

        List<String> result = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.path("data");

            for (JsonNode airport : data) {
                result.add(airport.path("iataCode").asText() + " - " + airport.path("name").asText());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String searchAirportByCode(String code) {
        return amadeusClient.searchAirportByCode(code);
    }
}

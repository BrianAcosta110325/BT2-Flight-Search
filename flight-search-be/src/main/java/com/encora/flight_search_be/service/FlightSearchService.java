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
import com.encora.flight_search_be.dto.AirportDto;
import com.encora.flight_search_be.dto.FlightSearchResponseDto;
import com.encora.utils.FlightService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class FlightSearchService implements FlightService {

    @Autowired
    private AmadeusClient amadeusClient;
    
    @Override
    public List<FlightSearchResponseDto> searchFlights(
        String originAirportCode,
        String destinationAirportCode,
        LocalDate departureDate,
        Integer numberOfAdults,
        String currencyCode,
        boolean onlyNonStopFlights
    ) {
        if (departureDate == null) {
            departureDate = LocalDate.now();
        }

        Map<String, String> params = new HashMap<>();
        params.put("originLocationCode", originAirportCode);
        params.put("destinationLocationCode", destinationAirportCode);
        params.put("departureDate", departureDate.toString());
        params.put("adults", String.valueOf(numberOfAdults));
        params.put("currencyCode", currencyCode);
        params.put("nonStop", String.valueOf(onlyNonStopFlights));
        params.put("max", "10");

        ResponseEntity<String> response = amadeusClient.searchFlights(params);

        List<FlightSearchResponseDto> result = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.path("data");

            for (JsonNode offer : data) {
                JsonNode itinerary = offer.path("itineraries").get(0);
                JsonNode firstSegment = itinerary.path("segments").get(0);
                JsonNode lastSegment = itinerary.path("segments").get(itinerary.path("segments").size() - 1);

                FlightSearchResponseDto dto = new FlightSearchResponseDto();
                dto.setOriginAirport(firstSegment.path("departure").path("iataCode").asText());
                dto.setDestinationAirport(lastSegment.path("arrival").path("iataCode").asText());
                dto.setDepartureDateTime(firstSegment.path("departure").path("at").asText());
                dto.setArrivalDateTime(lastSegment.path("arrival").path("at").asText());
                dto.setAirline(firstSegment.path("carrierCode").asText());
                dto.setDuration(itinerary.path("duration").asText());

                JsonNode price = offer.path("price");
                dto.setPrice(price.path("total").asText() + " " + currencyCode);
                dto.setPricePerTraveler(price.path("total").asText() + " " + currencyCode);
                dto.setNonStop(offer.path("numberOfBookableSeats").asInt() > 0 && itinerary.path("segments").size() == 1);

                result.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public List<AirportDto> searchAirports(String unNormalizedQuery) {
        // Normalize the query
        String query = unNormalizedQuery
            .toLowerCase()
            .replaceAll("[áàäâ]", "a")
            .replaceAll("[éèëê]", "e")
            .replaceAll("[íìïî]", "i")
            .replaceAll("[óòöô]", "o")
            .replaceAll("[úùüû]", "u")
            .replaceAll("ñ", "n");

        ResponseEntity<String> response = amadeusClient.searchAirports(query);

        List<AirportDto> result = new ArrayList<AirportDto>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.path("data");

            for (JsonNode airport : data) {
                String iataCode = airport.path("iataCode").asText();
                String name = airport.path("name").asText();
                result.add(new AirportDto(iataCode, name));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}

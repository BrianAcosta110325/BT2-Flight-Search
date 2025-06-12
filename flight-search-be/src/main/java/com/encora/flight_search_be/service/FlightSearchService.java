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
import com.encora.flight_search_be.dto.FlightSearchResponseDto;
import com.encora.flight_search_be.dto.FlightSearchStopDto;
import com.encora.utils.DurationUtils;
import com.encora.utils.FlightService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class FlightSearchService implements FlightService {

    @Autowired
    private AmadeusClient amadeusClient;
    
    @Override
    public List<FlightSearchResponseDto> searchFlights(
        String departureCode,
        String arrivalCode,
        LocalDate departureDate,
        Integer noAdults,
        String currency,
        boolean nonStop
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("originLocationCode", departureCode);
        params.put("destinationLocationCode", arrivalCode);
        params.put("departureDate", departureDate.toString());
        params.put("adults", String.valueOf(noAdults));
        params.put("currencyCode", currency);
        params.put("nonStop", String.valueOf(nonStop));
        params.put("max", "10");

        ResponseEntity<String> response = amadeusClient.searchFlights(params);

        List<FlightSearchResponseDto> result = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode data = root.path("data");
            JsonNode dictionaries = root.path("dictionaries");
            JsonNode carriers = dictionaries.path("carriers");
        
            for (JsonNode offer : data) {
                JsonNode itinerary = offer.path("itineraries").get(0);
                JsonNode segments = itinerary.path("segments");
                JsonNode firstSegment = segments.get(0);
                JsonNode lastSegment = segments.get(segments.size() - 1);
        
                FlightSearchResponseDto dto = new FlightSearchResponseDto();
        
                // Airports
                String rDepartureCode = firstSegment.path("departure").path("iataCode").asText();
                String rArrivalCode = lastSegment.path("arrival").path("iataCode").asText();

                dto.setDepartureAirportCode(rDepartureCode);
                dto.setArrivalAirportCode(rArrivalCode);
        
                dto.setDepartureAirportName(amadeusClient.searchAirportByCode(rDepartureCode));
                dto.setArrivalAirportName(amadeusClient.searchAirportByCode(rArrivalCode));
        
                // DateTime
                dto.setDepartureDateTime(firstSegment.path("departure").path("at").asText());
                dto.setArrivalDateTime(lastSegment.path("arrival").path("at").asText());
        
                // Airline
                String carrierCode = firstSegment.path("carrierCode").asText();
                dto.setAirlineCode(carrierCode);
                dto.setAirlineName(carriers.path(carrierCode).asText());

                // Operating Airline
                String operatingCarrierCode = firstSegment.path("operating").path("carrierCode").asText();
                dto.setOperatingAirlineCode(operatingCarrierCode);
                dto.setOperatingAirlineName(carriers.path(operatingCarrierCode).asText());
        
                // Duration
                dto.setTotalFlightDuration(DurationUtils.formatDuration(itinerary.path("duration").asText()));
        
                // Stops
                List<FlightSearchStopDto> stops = new ArrayList<>();
                for (int i = 1; i < segments.size(); i++) {
                    JsonNode prevSegment = segments.get(i - 1);
                    JsonNode currSegment = segments.get(i);
        
                    String stopCode = prevSegment.path("arrival").path("iataCode").asText();
    
                    String layoverDuration = currSegment.path("duration").asText();
        
                    // Calcular duración de escala (opcional: puedes usar java.time para hacerlo más preciso)
        
                    FlightSearchStopDto stopDto = new FlightSearchStopDto();
                    stopDto.setAirportCode(stopCode);
                    stopDto.setAirportName(amadeusClient.searchAirportByCode(stopCode));
                    stopDto.setLayoverDuration(DurationUtils.formatDuration(layoverDuration));
        
                    stops.add(stopDto);
                }
                dto.setStops(stops);
        
                // Price
                JsonNode price = offer.path("price");
                String total = price.path("total").asText() + " " + currency;
                dto.setTotalPrice(total);
                dto.setPricePerTraveler(total); // Asumimos precio por adulto ya que no se separa en JSON
        
                result.add(dto);
            }
        
        } catch (Exception e) {
            e.printStackTrace();
        }        

        return result;
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

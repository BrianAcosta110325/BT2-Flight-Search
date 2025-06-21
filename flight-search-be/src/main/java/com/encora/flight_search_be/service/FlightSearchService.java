package com.encora.flight_search_be.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.encora.flight_search_be.client.AmadeusClient;
import com.encora.flight_search_be.dto.AirportDto;
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

    private static final Logger logger = LoggerFactory.getLogger(FlightSearchService.class);
    
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

        ResponseEntity<String> response = amadeusClient.searchFlights(paramsMap(
            originAirportCode,
            destinationAirportCode,
            departureDate,
            numberOfAdults,
            currencyCode,
            onlyNonStopFlights
        ));

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
                String total = price.path("total").asText() + " " + currencyCode;
                dto.setTotalPrice(total);
                dto.setPricePerTraveler(total); // Asumimos precio por adulto ya que no se separa en JSON

                result.add(dto);
            }
        
        } catch (Exception e) {
            logger.error("Error parsing airport response", e);
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
            logger.error("Error parsing airport response", e);
        }


        return result;
    }

    @Override
    public String searchAirportByCode(String code) {
        return amadeusClient.searchAirportByCode(code);
    }
  
    private Map<String, String> paramsMap(
        String originAirportCode, 
        String destinationAirportCode, 
        LocalDate departureDate, 
        Integer numberOfAdults,
        String currencyCode,
        Boolean onlyNonStopFlights
        ){
        Map<String, String> params = new HashMap<>();
        params.put("originLocationCode", originAirportCode);
        params.put("destinationLocationCode", destinationAirportCode);
        params.put("departureDate", departureDate.toString());
        params.put("adults", String.valueOf(numberOfAdults));
        params.put("currencyCode", currencyCode);
        params.put("nonStop", String.valueOf(onlyNonStopFlights));
        params.put("max", "10");

        return params;
    }
}

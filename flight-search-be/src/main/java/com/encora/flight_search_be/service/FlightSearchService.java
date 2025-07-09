package com.encora.flight_search_be.service;

import java.time.LocalDate;
import java.util.*;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.encora.flight_search_be.client.AmadeusClient;
import com.encora.flight_search_be.dto.*;
import com.encora.utils.FlightService;
import com.encora.utils.FlightSorter;
import com.encora.utils.Sorter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FlightSearchService implements FlightService {

    @Autowired
    private AmadeusClient amadeusClient;

    private static final Logger logger = LoggerFactory.getLogger(FlightSearchService.class);

    private static Map<String, String> lastQuery = new HashMap<>();

    @Override
    public SearchFlightResponseDto searchFlights(
        String page,
        String originAirportCode,
        String destinationAirportCode,
        LocalDate departureDate,
        Integer numberOfAdults,
        String currencyCode,
        Boolean onlyNonStopFlights,
        Sorter sortBy
    ) {
        int pageInt = Integer.parseInt(page);
        int limit = 10;
        int offset = (pageInt - 1) * limit;

        if (departureDate == null) {
            departureDate = LocalDate.now();
        }

        numberOfAdults = numberOfAdults != null ? numberOfAdults : 1;
        onlyNonStopFlights = onlyNonStopFlights != null ? onlyNonStopFlights : false;

        Map<String, String> params = paramsMap(
            page,
            originAirportCode,
            destinationAirportCode,
            departureDate,
            numberOfAdults,
            currencyCode,
            onlyNonStopFlights
        );

        JsonNode data = amadeusClient.searchFlights(params);

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<FlightSearchAmadeusResposeDto> flights = mapper.convertValue(
                data,
                new TypeReference<List<FlightSearchAmadeusResposeDto>>() {}
            );

            FlightSorter.sort(flights, sortBy);

            List<FlightSearchResponseDto> result = new ArrayList<FlightSearchResponseDto>();

            for (FlightSearchAmadeusResposeDto flight : flights) {
                if(flight.getDepartureCode() == null || !flight.getDepartureCode().equals(originAirportCode)) {
                    continue;
                }
                if(flight.getArrivalCode() == null || !flight.getArrivalCode().equals(destinationAirportCode)) {
                    continue;
                }
                result.add(new FlightSearchResponseDto(flight, amadeusClient));
            }

            int fromIndex = Math.min(offset, result.size());
            int toIndex = Math.min(offset + limit, result.size());
            List<FlightSearchResponseDto> pageResults = result.subList(fromIndex, toIndex);

            int totalPages = (int) Math.ceil((double) result.size() / limit);

            lastQuery = params;

            return new SearchFlightResponseDto(pageResults, totalPages);

        } catch (Exception e) {
            logger.error("Error parsing airport response", e);
        }
        return null;
    }


    @Override
    public FlightSearchDetailedResponseDto searchFlightById(
        String id
    ) {
        JsonNode data = amadeusClient.searchFlights(lastQuery);

        try {
            ObjectMapper mapper = new ObjectMapper();
            List<FlightSearchAmadeusResposeDto> flights = mapper.convertValue(
                data,
                new TypeReference<List<FlightSearchAmadeusResposeDto>>() {}
            );

            for (FlightSearchAmadeusResposeDto flight : flights) {
                if (flight.getId().equals(id)) {
                    return new FlightSearchDetailedResponseDto(flight, amadeusClient);
                }
            }

        } catch (Exception e) {
            logger.error("Error parsing flight by ID", e);
        }

        return new FlightSearchDetailedResponseDto();
    }

    @Override
    public List<AirportDto> searchAirports(String unNormalizedQuery) {
        String query = unNormalizedQuery
            .toLowerCase()
            .replaceAll("[áàäâ]", "a")
            .replaceAll("[éèëê]", "e")
            .replaceAll("[íìïî]", "i")
            .replaceAll("[óòöô]", "o")
            .replaceAll("[úùüû]", "u")
            .replaceAll("ñ", "n");

        ResponseEntity<String> response = amadeusClient.searchAirports(query);
        List<AirportDto> result = new ArrayList<>();

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

    @Override
    public String searchAirlineByCode(String code) {
        return amadeusClient.searchAirlineByCode(code);
    }

    private Map<String, String> paramsMap(
        String page,
        String originAirportCode,
        String destinationAirportCode,
        LocalDate departureDate,
        Integer numberOfAdults,
        String currencyCode,
        Boolean onlyNonStopFlights
    ) {
        Map<String, String> params = new HashMap<>();
        // params.put("page", page); // Usar si implementas paginación manual
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
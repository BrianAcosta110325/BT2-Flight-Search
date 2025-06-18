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
import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto.FlightSearchDetailedStopDto;
import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto.FlightFareDetailsDto;
import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto.AmenityDto;
import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto.FeeDto;
// import com.encora.flight_search_be.dto.FlightSearchAmadeusResposDto;
import com.encora.flight_search_be.dto.FlightSearchAmadeusResposeDto;
import com.encora.flight_search_be.dto.FlightSearchDetailedResponseDto;
import com.encora.flight_search_be.dto.FlightSearchResponseDto;
import com.encora.flight_search_be.dto.FlightSearchStopDto;
import com.encora.utils.DurationUtils;
import com.encora.utils.FlightService;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    // @Override
    // public FlightSearchDetailedResponseDto searchFlightById(
    //     String departureCode,
    //     String arrivalCode,
    //     LocalDate departureDate,
    //     Integer noAdults,
    //     String currency,
    //     boolean nonStop,
    //     String flightId
    // ) {
    //     Map<String, String> params = new HashMap<>();
    //     params.put("originLocationCode", departureCode);
    //     params.put("destinationLocationCode", arrivalCode);
    //     params.put("departureDate", departureDate.toString());
    //     params.put("adults", String.valueOf(noAdults));
    //     params.put("currencyCode", currency);
    //     params.put("nonStop", String.valueOf(nonStop));
    //     params.put("max", "10");

    //     ResponseEntity<String> response = amadeusClient.searchFlights(params);
    //     FlightSearchDetailedResponseDto dto = new FlightSearchDetailedResponseDto();

    //     try {
    //         ObjectMapper mapper = new ObjectMapper();
    //         JsonNode root = mapper.readTree(response.getBody());
    //         JsonNode data = root.path("data");

    //         for (JsonNode flight : data) {
    //             if (flight.path("id").asText().equals(flightId)) {
    //                 dto.setId(flightId);

    //                 JsonNode itinerary = flight.path("itineraries").get(0);
    //                 JsonNode segments = itinerary.path("segments");
    //                 JsonNode firstSegment = segments.get(0);
    //                 JsonNode lastSegment = segments.get(segments.size() - 1);

    //                 // Airport codes
    //                 String rDepartureCode = firstSegment.path("departure").path("iataCode").asText();
    //                 String rArrivalCode = lastSegment.path("arrival").path("iataCode").asText();
    //                 dto.setDepartureAirportCode(rDepartureCode);
    //                 dto.setArrivalAirportCode(rArrivalCode);
    //                 dto.setDepartureAirportName(amadeusClient.searchAirportByCode(rDepartureCode));
    //                 dto.setArrivalAirportName(amadeusClient.searchAirportByCode(rArrivalCode));

    //                 // Tiempos
    //                 dto.setDepartureDateTime(firstSegment.path("departure").path("at").asText());
    //                 dto.setArrivalDateTime(lastSegment.path("arrival").path("at").asText());

    //                 // Aerolínea principal
    //                 String carrierCode = firstSegment.path("carrierCode").asText();
    //                 dto.setAirlineCode(carrierCode);
    //                 dto.setAirlineName(root.path("dictionaries").path("carriers").path(carrierCode).asText());

    //                 // Aerolínea operadora
    //                 JsonNode operatingNode = firstSegment.path("operating");
    //                 if (!operatingNode.isMissingNode()) {
    //                     String opCode = operatingNode.path("carrierCode").asText();
    //                     dto.setOperatingAirlineCode(opCode);
    //                     dto.setOperatingAirlineName(root.path("dictionaries").path("carriers").path(opCode).asText());
    //                 }

    //                 // Duración total
    //                 dto.setTotalFlightDuration(DurationUtils.formatDuration(itinerary.path("duration").asText()));

    //                 // Paradas y layovers
    //                 List<FlightSearchDetailedStopDto> stops = new ArrayList<>();

    //                 for (int i = 1; i < segments.size(); i++) {
    //                     JsonNode prev = segments.get(i - 1);
    //                     JsonNode curr = segments.get(i);

    //                     String stopCode = prev.path("arrival").path("iataCode").asText();
    //                     String layoverTime = curr.path("duration").asText();

    //                     FlightSearchDetailedStopDto stopDto = new FlightSearchDetailedStopDto();
    //                     stopDto.setAirportCode(stopCode);
    //                     stopDto.setDuration(layoverTime);

    //                     stops.add(stopDto);
    //                 }

    //                 dto.setStops(stops);
    //                 dto.setAircraftCode(firstSegment.path("aircraft").path("code").asText());



    //                 // Detalles de tarifa
    //                 List<FlightFareDetailsDto> fareDetailsList = new ArrayList<FlightFareDetailsDto>();
    //                 JsonNode fareDetailsBySegment = flight.path("travelerPricings").get(0).path("fareDetailsBySegment");

    //                 for (JsonNode segmentFare : fareDetailsBySegment) {
                        
    //                     FlightFareDetailsDto fareDto = new FlightFareDetailsDto();
    //                     fareDto.setSegmentId(segmentFare.path("segmentId").asText());
    //                     fareDto.setCabin(segmentFare.path("cabin").asText());
    //                     fareDto.setTravelClass(segmentFare.path("class").asText());

    //                     // Equipaje
    //                     if (segmentFare.has("includedCheckedBags")) {
    //                         fareDto.setIncludedCheckedBags(segmentFare.path("includedCheckedBags").path("quantity").asInt());
    //                     }
    //                     if (segmentFare.has("includedCabinBags")) {
    //                         fareDto.setIncludedCabinBags(segmentFare.path("includedCabinBags").path("quantity").asInt());
    //                     }

    //                     // Amenities
    //                     List<AmenityDto> amenities = new ArrayList<>();
    //                     if (segmentFare.has("amenities")) {
    //                         for (JsonNode amenity : segmentFare.path("amenities")) {
    //                             AmenityDto a = new AmenityDto();
    //                             a.setDescription(amenity.path("description").asText());
    //                             a.setChargeable(amenity.path("isChargeable").asBoolean());
    //                             amenities.add(a);
    //                         }
    //                     }
    //                     fareDto.setAmenities(amenities);

    //                     fareDetailsList.add(fareDto);
    //                 }
    //                 dto.setFareDetails(fareDetailsList);

    //                 // Precio
    //                 JsonNode price = flight.path("price");
    //                 String rCurrency = price.path("currency").asText();
    //                 dto.setBasePrice(price.path("base").asText() + " " + rCurrency);
    //                 dto.setTotalPrice(price.path("total").asText() + " " + rCurrency);
    //                 dto.setPricePerTraveler(price.path("total").asText() + " " + rCurrency);

    //                 List<FeeDto> fees = new ArrayList<>();
    //                 for (JsonNode fee : price.path("fees")) {
    //                     FeeDto f = new FeeDto();
    //                     f.setAmount(fee.path("amount").asText());
    //                     f.setType(fee.path("type").asText());
    //                     fees.add(f);
    //                 }
    //                 dto.setFees(fees);

    //                 break;
    //             }
    //         }

    //     } catch (Exception e) {
    //         e.printStackTrace();
    //     }

    //     return dto;
    // }


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

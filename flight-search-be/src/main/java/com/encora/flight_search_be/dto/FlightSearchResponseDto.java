package com.encora.flight_search_be.dto;

import java.util.List;

import com.encora.flight_search_be.client.AmadeusClient;
import com.encora.utils.DurationUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightSearchResponseDto {
    private String id;
    private String departureAirportName;
    private String departureAirportCode;
    private String arrivalAirportName;
    private String arrivalAirportCode;
    
    private String departureDateTime;
    private String arrivalDateTime;

    private String airlineName;
    private String airlineCode;

    private String operatingAirlineName;
    private String operatingAirlineCode;

    private String totalFlightDuration;

    private List<FlightSearchStopDto> stops;

    private String totalPrice;
    private String pricePerTraveler;

    @Getter
    @Setter
    public class FlightSearchStopDto {
        private String airportName;
        private String airportCode;
        private String layoverDuration;

        public FlightSearchStopDto(FlightSearchAmadeusResposeDto.Segment segment, AmadeusClient amadeusClient) {
            this.airportName = amadeusClient.searchAirportByCode(segment.getArrival().getIataCode());
            this.airportCode = segment.getArrival().getIataCode();
            this.layoverDuration = DurationUtils.formatDuration(segment.getDuration());
        }
    }

    public FlightSearchResponseDto(FlightSearchAmadeusResposeDto flightSearchAmadeusResposeDto, AmadeusClient amadeusClient) {
        this.id = flightSearchAmadeusResposeDto.getId();
        this.departureAirportName = amadeusClient.searchAirportByCode(getDepartureAirportCode());
        this.departureAirportCode = flightSearchAmadeusResposeDto.getDepartureCode();
        this.arrivalAirportName = amadeusClient.searchAirportByCode(flightSearchAmadeusResposeDto.getArrivalCode());
        this.arrivalAirportCode = flightSearchAmadeusResposeDto.getArrivalCode();

        this.departureDateTime = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getDeparture().getAt();
        this.arrivalDateTime = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().size() - 1).getArrival().getAt();

        this.airlineName = amadeusClient.searchAirlineByCode(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getCarrierCode());
        this.airlineCode = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getCarrierCode();

        this.operatingAirlineName = amadeusClient.searchAirlineByCode(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getOperating().getCarrierCode());
        this.operatingAirlineCode = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getOperating().getCarrierCode();

        if (flightSearchAmadeusResposeDto.getItineraries().size() > 1) {
            this.operatingAirlineName = flightSearchAmadeusResposeDto.getItineraries().stream()
                    .map(itinerary -> itinerary.getSegments())
                    .flatMap(List::stream)
                    .findFirst()
                    .orElse(null)
                    .getOperating()
                    .toString();
            this.operatingAirlineCode = flightSearchAmadeusResposeDto.getItineraries().stream()
                    .map(itinerary -> itinerary.getSegments())
                    .flatMap(List::stream)
                    .findFirst()
                    .orElse(null)
                    .getOperating()
                    .toString();
        }

        this.totalFlightDuration = DurationUtils.formatDuration(flightSearchAmadeusResposeDto.getItineraries().get(0).getDuration());
        this.stops = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().stream()
                .map(segment -> new FlightSearchStopDto(segment, amadeusClient))
                .toList();
        this.totalPrice = flightSearchAmadeusResposeDto.getPrice().getTotal();
        this.pricePerTraveler = flightSearchAmadeusResposeDto.getTravelerPricings().get(0).getPrice().getTotal();
    }
}

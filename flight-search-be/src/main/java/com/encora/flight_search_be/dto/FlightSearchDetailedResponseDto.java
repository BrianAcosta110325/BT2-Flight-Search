package com.encora.flight_search_be.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.encora.flight_search_be.client.AmadeusClient;
import com.encora.flight_search_be.dto.FlightSearchAmadeusResposeDto.Amenity;
import com.encora.flight_search_be.dto.FlightSearchAmadeusResposeDto.FareDetail;
import com.encora.flight_search_be.dto.FlightSearchAmadeusResposeDto.Fee;
import com.encora.flight_search_be.dto.FlightSearchAmadeusResposeDto.Segment;
import com.encora.utils.DurationUtils;

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
    private List<FlightFareDetailsDto> fareDetails;

    // Amenity
    private List<AmenityDto> amenities;

    // Stops
    private List<FlightSearchDetailedStopDto> stops;
    private List<String> layoverDurations;

    // Price
    private String basePrice;
    private String totalPrice;
    private String pricePerTraveler;
    private List<FeeDto> fees;

    @Getter
    @Setter
    public static class FlightFareDetailsDto {
        private String segmentId;
        private String cabin;
        private String travelClass;
        private Integer includedCheckedBags;
        private Integer includedCabinBags;
        private List<AmenityDto> amenities;

        public FlightFareDetailsDto(FareDetail fareDetail) {
            this.segmentId = fareDetail.getSegmentId();
            this.cabin = fareDetail.getCabin();
            this.travelClass = fareDetail.getFlightClass();
            this.includedCheckedBags = fareDetail.getIncludedCheckedBags() != null ? fareDetail.getIncludedCheckedBags().getWeight() : 0;
            this.includedCabinBags = fareDetail.getIncludedCabinBags() != null ? fareDetail.getIncludedCabinBags().getWeight() : 0;
            this.amenities = fareDetail.getAmenities().stream()
                .map(amenity -> {
                    AmenityDto amenityDto = new AmenityDto(amenity);
                    return amenityDto;
                }).toList();

        }
    }

    @Getter
    @Setter
    public static class AmenityDto {
        private String description;
        private boolean isChargeable;

        public AmenityDto(Amenity amenity) {
            this.description = amenity.getDescription();
            this.isChargeable = amenity.isChargeable();
        }
    }

    @Getter
    @Setter
    public static class FeeDto {
        private String type;
        private String amount;

        public FeeDto(Fee fee) {
            this.type = fee.getType();
            this.amount = fee.getAmount();
        }
    }

    @Getter
    @Setter
    public static class FlightSearchDetailedStopDto {
        private String arrivalAirportName;
        private String arrivalAirportCode;
        private String arrivalDate;
        private String departureAirportName;
        private String departureAirportCode;
        private String departureDate;

        public FlightSearchDetailedStopDto(Segment segment, AmadeusClient amadeusClient) {
            this.arrivalAirportName = amadeusClient.searchAirportByCode(segment.getArrival().getIataCode());
            this.arrivalAirportCode = segment.getArrival().getIataCode();
            this.arrivalDate = segment.getArrival().getAt();
            this.departureAirportName = amadeusClient.searchAirportByCode(segment.getDeparture().getIataCode());
            this.departureAirportCode = segment.getDeparture().getIataCode();
            this.departureDate = segment.getDeparture().getAt();
        }
    }

    public FlightSearchDetailedResponseDto() {
        // Default constructor
    }

    public FlightSearchDetailedResponseDto(FlightSearchAmadeusResposeDto flightSearchAmadeusResposeDto, 
                                           AmadeusClient amadeusClient) {
        this.id = flightSearchAmadeusResposeDto.getId();
        this.departureAirportName = amadeusClient.searchAirportByCode(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getDeparture().getIataCode());
        this.departureAirportCode = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getDeparture().getIataCode();
        this.arrivalAirportName = amadeusClient.searchAirportByCode(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().size() - 1).getArrival().getIataCode());
        this.arrivalAirportCode = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().size() - 1).getArrival().getIataCode();
        this.departureDateTime = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getDeparture().getAt();
        this.arrivalDateTime = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().size() - 1).getArrival().getAt();
        this.totalFlightDuration = flightSearchAmadeusResposeDto.getItineraries().get(0).getDuration();
        this.airlineName = amadeusClient.searchAirlineByCode(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getCarrierCode());
        this.airlineCode = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getCarrierCode();
        this.operatingAirlineName = amadeusClient.searchAirlineByCode(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getOperating().getCarrierCode());
        this.operatingAirlineCode = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getOperating().getCarrierCode();
        this.aircraftCode = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(0).getAircraft().getCode();
        this.cabin = flightSearchAmadeusResposeDto.getTravelerPricings().get(0).getFareDetailsBySegment().get(0).getCabin();
        this.fareClass = flightSearchAmadeusResposeDto.getTravelerPricings().get(0).getFareDetailsBySegment().get(0).getFlightClass();
        this.fareDetails = flightSearchAmadeusResposeDto.getTravelerPricings().get(0).getFareDetailsBySegment().stream()
            .map(segment -> {
                FlightFareDetailsDto fareDetail = new FlightFareDetailsDto(segment);
                return fareDetail;
            }).toList();
        this.amenities = flightSearchAmadeusResposeDto.getTravelerPricings().get(0).getFareDetailsBySegment().get(0).getAmenities().stream()
            .map(amenity -> {
                AmenityDto amenityDto = new AmenityDto(amenity);
                return amenityDto;
            }).toList();
        this.stops = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().stream()
            .map(segment -> {
                FlightSearchDetailedStopDto stop = new FlightSearchDetailedStopDto(segment, amadeusClient);
                return stop;
            }).toList();
        this.layoverDurations = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().stream()
            .map(segment -> DurationUtils.formatDuration(segment.getDuration()))
            .toList();
        this.basePrice = flightSearchAmadeusResposeDto.getPrice().getBase();
        this.totalPrice = flightSearchAmadeusResposeDto.getPrice().getTotal();
        this.pricePerTraveler = flightSearchAmadeusResposeDto.getTravelerPricings().get(0).getPrice().getTotal();
        this.fees = flightSearchAmadeusResposeDto.getPrice().getFees().stream()
            .map(fee -> {
                FeeDto feeDto = new FeeDto(fee);
                return feeDto;
            }).toList();
        
        if (flightSearchAmadeusResposeDto.getItineraries().size() > 1) {
            this.operatingAirlineName = flightSearchAmadeusResposeDto.getItineraries().stream()
                .map(itinerary -> itinerary.getSegments())
                .flatMap(List::stream)
                .findFirst()
                .orElse(null)
                .getOperating()
                .getCarrierCode();
            this.operatingAirlineCode = flightSearchAmadeusResposeDto.getItineraries().stream()
                .map(itinerary -> itinerary.getSegments())
                .flatMap(List::stream)
                .findFirst()
                .orElse(null)
                .getOperating()
                .getCarrierCode();
        }
        // if (flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().size() > 1) {
        //     this.aircraftCode = flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(1).getAircraft().getCode();
        //     this.cabin = flightSearchAmadeusResposeDto.getTravelerPricings().get(0).getFareDetailsBySegment().get(1).getCabin();
        //     this.fareClass = flightSearchAmadeusResposeDto.getTravelerPricings().get(0).getFareDetailsBySegment().get(1).getFlightClass();
        // }
        // if (flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().size() > 1) {
        //     this.fareDetails.addAll(flightSearchAmadeusResposeDto.getItineraries().get(0).getSegments().get(1).getFareDetailsBySegment().stream()
        //         .map(segment -> {
        //             FlightFareDetailsDto fareDetail = new FlightFareDetailsDto();
        //             fareDetail.setSegmentId(segment.getSegmentId());
        //             fareDetail.setCabin(segment.getCabin());
        //             fareDetail.setTravelClass(segment.getFlightClass());
        //             fareDetail.setIncludedCheckedBags(segment.getIncludedCheckedBags() != null ? segment.getIncludedCheckedBags().getWeight() : 0);
        //             fareDetail.setIncludedCabinBags(segment.getIncludedCabinBags() != null ? segment.getIncludedCabinBags().getWeight() : 0);
        //             fareDetail.setAmenities(segment.getAmenities());
        //             return fareDetail;
        //         }).toList());
        // }
    }
}

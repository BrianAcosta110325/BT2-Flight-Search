package com.encora.flight_search_be.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightSearchAmadeusResposeDto {
    private String type;
    private String id;
    private String source;
    private boolean instantTicketingRequired;
    private boolean nonHomogeneous;
    private boolean oneWay;
    @JsonProperty("isUpsellOffer")
    private boolean isUpsellOffer;
    private String lastTicketingDate;
    private String lastTicketingDateTime;
    private int numberOfBookableSeats;
    private List<Itinerary> itineraries;
    private Price price;
    private PricingOptions pricingOptions;
    private List<String> validatingAirlineCodes;
    private List<TravelerPricing> travelerPricings;

    @Getter
    @Setter
    public static class Itinerary {
        private String duration;
        private List<Segment> segments;
    }

    @Getter
    @Setter
    public static class Segment {
        private Location departure;
        private Location arrival;
        private String carrierCode;
        private String number;
        private Aircraft aircraft;
        private Operating operating;
        private String duration;
        private String id;
        private int numberOfStops;
        private boolean blacklistedInEU;
    }

    @Getter
    @Setter
    public static class Location {
        private String iataCode;
        private String terminal; 
        private String at;
    }

    @Getter
    @Setter
    public static class Aircraft {
        private String code;
    }

    @Getter
    @Setter
    public static class Operating {
        private String carrierCode;
        private String carrierName;
    }

    @Getter
    @Setter
    public static class Price {
        private String currency;
        private String total;
        private String base;
        private List<Fee> fees;
        private String grandTotal;
    }

    @Getter
    @Setter
    public static class Fee {
        private String amount;
        private String type;
    }

    @Getter
    @Setter
    public static class PricingOptions {
        private List<String> fareType;
        private boolean includedCheckedBagsOnly;
    }

    @Getter
    @Setter
    public static class TravelerPricing {
        private String travelerId;
        private String fareOption;
        private String travelerType;
        private Price price;
        private List<FareDetail> fareDetailsBySegment;
    }

    @Getter
    @Setter
    public static class FareDetail {
        private String segmentId;
        private String cabin;
        private String fareBasis;
        private String brandedFare;
        private String brandedFareLabel;

        @JsonProperty("class")
        private String flightClass;

        private IncludedBags includedCheckedBags;
        private IncludedBags includedCabinBags;
        private List<Amenity> amenities;
    }

    @Getter
    @Setter
    public static class IncludedBags {
        private int weight;   
        private String weightUnit;
        private Integer quantity;
    }

    @Getter
    @Setter
    public static class Amenity {
        private String description;
        @JsonProperty("isChargeable")
        private boolean isChargeable;
        private String amenityType;
        private AmenityProvider amenityProvider;
    }

    @Getter
    @Setter
    public static class AmenityProvider {
        private String name;
    }

    // Quick access methods for departure and arrival codes
    public String getDepartureCode() {
        return this.itineraries.get(0).getSegments().get(0).getDeparture().getIataCode();
    }

    public String getArrivalCode() {
        return this.itineraries.get(0).getSegments().get(this.itineraries.get(0).getSegments().size() - 1).getArrival().getIataCode();
    }
}

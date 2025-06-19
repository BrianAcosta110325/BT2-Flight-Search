export interface FlightSearchDetailedResponseDto {
  id: string;

  // Airports
  departureAirportName: string;
  departureAirportCode: string;
  arrivalAirportName: string;
  arrivalAirportCode: string;

  // DateTime
  departureDateTime: string;
  arrivalDateTime: string;
  totalFlightDuration: string;

  // Airline
  airlineName: string;
  airlineCode: string;
  operatingAirlineName: string;
  operatingAirlineCode: string;

  // Segment details
  aircraftCode: string;
  cabin: string;
  fareClass: string;
  fareDetails: FlightFareDetailsDto[];

  // Amenities
  amenities: AmenityDto[];

  // Stops
  stops: FlightSearchDetailedStopDto[];
  layoverDurations: string[];

  // Price
  basePrice: string;
  totalPrice: string;
  pricePerTraveler: string;
  fees: FeeDto[];
}

export interface FlightFareDetailsDto {
  segmentId: string;
  cabin: string;
  travelClass: string;
  includedCheckedBags: number;
  includedCabinBags: number;
  amenities: AmenityDto[];
}

export interface AmenityDto {
  description: string;
  isChargeable: boolean;
}

export interface FeeDto {
  type: string;
  amount: string;
}

export interface FlightSearchDetailedStopDto {
  arrivalAirportName: string;
  arrivalAirportCode: string;
  arrivalDate: string;
  departureAirportName: string;
  departureAirportCode: string;
  departureDate: string;
}

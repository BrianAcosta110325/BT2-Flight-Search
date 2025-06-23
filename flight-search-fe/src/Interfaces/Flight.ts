export interface FlightStop {
  airportName: string;
  airportCode: string;
  layoverDuration: string;
}

export interface Flight {
  id: string;
  departureAirportName: string;
  departureAirportCode: string;
  arrivalAirportName: string;
  arrivalAirportCode: string;

  departureDateTime: string;
  arrivalDateTime: string;

  airlineName: string;
  airlineCode: string;

  operatingAirlineName: string;
  operatingAirlineCode: string;

  totalFlightDuration: string;

  stops: FlightStop[];

  totalPrice: string;
  pricePerTraveler: string;
}

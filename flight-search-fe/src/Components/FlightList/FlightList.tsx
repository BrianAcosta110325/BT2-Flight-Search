import { Flight } from '../../Interfaces/Flight';
import './FlightList.css';

interface FlightListProps {
  flights: Flight[];
  loading: boolean;
}

function formatDateTime(datetime: string): string {
  const date = new Date(datetime);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

function FlightList({ flights, loading }: FlightListProps) {
  if (loading) {
    return <p className="text-center mt-4">Loading flights...</p>;
  }

  if (flights.length === 0) {
    return <p className="text-center mt-4">No flights found.</p>;
  }

  return (
    <div className="container mt-4">
      {flights.map((flight, index) => (
        <div key={index} className="card mb-3 p-3 shadow-sm">
          <div className="row align-items-center">
            {/* Horarios y ruta */}
            <div className="col-md-6">
              <div className="fw-bold">
                {formatDateTime(flight.departureDateTime)} - {formatDateTime(flight.arrivalDateTime)}
              </div>
              <div>
                {flight.departureAirportName} ({flight.departureAirportCode}) - {flight.arrivalAirportName} ({flight.arrivalAirportCode})
              </div>
              <div className="text-muted">
                {flight.totalFlightDuration} {flight.stops?.length === 0 ? '(Nonstop)' : `(${flight.stops?.length} stop${flight.stops?.length > 1 ? 's' : ''})`}
              </div>
              <div className="mt-2">{flight.airlineName} ({flight.airlineCode})</div>
            </div>

            {/* Precios */}
            <div className="col-md-6 text-end">
              <div className="fs-5 fw-bold">
                ${parseFloat(flight.totalPrice).toFixed(2)} MXN
              </div>
              <div className="text-muted">total</div>
              <div className="mt-3 fw-bold">
                ${parseFloat(flight.pricePerTraveler).toFixed(2)} MXN
              </div>
              <div className="text-muted">per Traveler</div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}

export default FlightList;

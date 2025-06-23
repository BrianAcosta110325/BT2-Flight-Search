import { Flight } from '../../Interfaces/Flight';
import { useNavigate } from 'react-router-dom';
import './FlightList.css';
import { useState } from 'react';
import { Sorter } from '../../Interfaces/Sorter';

interface FlightListProps {
  flights: Flight[];
  loading: boolean;
  onChangeSorter: (sortBy: Sorter) => void;
}

function formatDateTime(datetime: string): string {
  const date = new Date(datetime);
  return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

const sortOptions = [
  { value: Sorter.BasePrice, label: 'Base Price' },
  { value: Sorter.TotalPrice, label: 'Total Price' },
  { value: Sorter.Fees, label: 'Fees' },
  { value: Sorter.PricePerTraveler, label: 'Price Per Traveler' },
];

function FlightList({ flights, loading, onChangeSorter }: FlightListProps) {
  const navigate = useNavigate();
  const [sortBy, setSortBy] = useState<string>('totalPrice');

  const handleSortChange = (value: string) => {
    setSortBy(value);
    onChangeSorter(value as Sorter);
  };

  const handleClick = (flightId: string) => {
    navigate(`/flight-details/${flightId}`);
  };

  const sortedFlights = [...flights].sort((a, b) => {
    const aValue = parseFloat((a as any)[sortBy]);
    const bValue = parseFloat((b as any)[sortBy]);
    return aValue - bValue;
  });

  if (loading) {
    return <p className="text-center mt-4">Loading flights...</p>;
  }

  if (flights.length === 0) {
    return <p className="text-center mt-4">No flights found.</p>;
  }

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-end align-items-center mb-3">
        <span className="me-2">Sort by</span>
        <select
          className="form-select w-auto"
          value={sortBy}
          onChange={e => handleSortChange(e.target.value)}
        >
          {sortOptions.map(option => (
            <option key={option.value} value={option.value}>
              {option.label}
            </option>
          ))}
        </select>
      </div>
      {sortedFlights.map((flight, index) => (
        <div
          key={index}
          className="card card-flight-list mb-3 p-3 shadow-sm"
          style={{ cursor: 'pointer' }}
          onClick={() => {
            handleClick(flight.id);
          }}
        >
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

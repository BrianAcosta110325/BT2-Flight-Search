import './FlightDetails.css';
import { DetailedFlight } from '../../Interfaces/DetailedFlight';
import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { SearchFlightsService } from '../../Services/FlightSearchService';

const FlightDetails = () => {
  const { id } = useParams<{ id: string }>();
  const [flight, setFlight] = useState<DetailedFlight | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      SearchFlightsService.getDetailedFlight(id)
        .then((data) => {
          setFlight(data);
        })
        .catch((error) => {
          console.error('Error fetching flight details:', error);
        });
    }
  }, [id]);

  if (!flight) return <div>Loading...</div>;
  
  const handleClick = () => {
    navigate(`/`);
  };

  return (
    <div className="flight-details-container container mt-4">
      <button
      className="btn btn-secondary mb-3"
      onClick={() => handleClick()}
      >
      Return to Filter
      </button>
      <div className="row">
      {/* Segmentos - Izquierda */}
      <div className="col-md-8">
          {flight.fareDetails?.map((fare, index) => (
            <div key={fare.segmentId} className="segment-card mb-4 p-3 border rounded">
              <div className="segment-info">
                <p><strong>Segment {index + 1}</strong></p>
                <p>{flight.stops[index].departureDate} - {flight.stops[index].arrivalDate}</p>
                <p>{flight.stops[index].departureAirportName} ({flight.stops[index].departureAirportCode}) - {flight.stops[index].arrivalAirportName} ({flight.stops[index].arrivalAirportCode})</p>
                <p>{flight.airlineName} ({flight.airlineCode})</p>
              </div>
              <div className="fare-box mt-2">
                <p><strong>Traveler Fare Details</strong></p>
                <p>Cabin: {fare.cabin}</p>
                <p>Class: {fare.travelClass}</p>
                <p>Checked Bags: {fare.includedCheckedBags}</p>
                <p>Cabin Bags: {fare.includedCabinBags}</p>
              </div>
            </div>
          ))}
        </div>

        {/* Precio - Derecha */}
        <div className="col-md-4">
          <div className="price-section border p-4 rounded bg-white shadow-sm">
            <h5 className="mb-3">Price Breakdown</h5>
            <p>Base: ${flight.basePrice}</p>
            <p>
              Fees: $
              {(Number(flight.totalPrice) - Number(flight.basePrice)).toFixed(2)}
            </p>
            <p><strong>Total: ${flight.totalPrice}</strong></p>

            {/* Subcuadro dentro del cuadro */}
            <div className="per-traveler-box mt-4 p-3 rounded bg-light text-center border">
              <h6>Per Traveler</h6>
              <p className="fs-5 fw-bold">${flight.pricePerTraveler}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FlightDetails;
import './FlightDetails.css';
import { DetailedFlight } from '../../Interfaces/DetailedFlight';
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { SearchFlightsService } from '../../Services/FlightSearchService';

const FlightDetails = () => {
  const { id } = useParams<{ id: string }>();
  const [flight, setFlight] = useState<DetailedFlight | null>(null);

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

  return (
    <div className="flight-details-container">
      {/* Segmentos */}
      {flight.fareDetails.map((fare, index) => (
        <div key={fare.segmentId} className="segment-card">
          <div className="segment-info">
            <p><strong>Segment {index + 1}</strong></p>
            <p>{flight.departureDateTime} - {flight.arrivalDateTime}</p>
            <p>{flight.departureAirportName} ({flight.departureAirportCode}) - {flight.arrivalAirportName} ({flight.arrivalAirportCode})</p>
            <p>{flight.airlineName} ({flight.airlineCode})</p>
          </div>
          <div className="fare-box">
            <p><strong>Travelers fare details</strong></p>
            <p>Cabin: {fare.cabin}</p>
            <p>Class: {fare.travelClass}</p>
            <p>Checked Bags: {fare.includedCheckedBags}</p>
            <p>Cabin Bags: {fare.includedCabinBags}</p>
          </div>
        </div>
      ))}

      {/* Desglose de precios */}
      <div className="price-section">
        <div className="breakdown">
          <h5>Price Breakdown</h5>
          <p>Base: ${flight.basePrice}</p>
          <p>Fees: ${flight.fees.reduce((acc, f) => acc + parseFloat(f.amount), 0).toFixed(2)}</p>
          <p><strong>Total: ${flight.totalPrice}</strong></p>
        </div>
        <div className="per-traveler">
          <h5>Per Traveler</h5>
          <p>${flight.pricePerTraveler}</p>
        </div>
      </div>
    </div>
  );
};

export default FlightDetails;

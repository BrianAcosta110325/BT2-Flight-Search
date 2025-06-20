import { useState } from 'react';
import { QueryParams } from '../../Interfaces/QueryParams';
import Filter from '../Filter/Filter';
import './App.css';
import { Flight } from '../../Interfaces/Flight';
import React from 'react';
import { SearchFlightsService } from '../../Services/FlightSearchApi';

function App() {
  // Flights
  const [flights, setFlights] = useState<Flight[]>([]);
  // Pagination
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);

  // FilterParams
  const [filter, setFilter] = useState<QueryParams>({} as QueryParams);

  // Loading state
  const [loading, setLoading] = useState(true);

  const applyFilter = React.useCallback(() => {
    setLoading(true);

    const queryParams: QueryParams = {
      page: page,
      departureCode: '',
      arrivalCode: '',
      departureDate: undefined,
      noAdults: undefined,
      currency: 'USD',
      nonStops: false,
    };
    
    if (filter.departureCode) {
      queryParams.departureCode = filter.departureCode;
    }
    if (filter.arrivalCode) {
      queryParams.arrivalCode = filter.arrivalCode;
    }
    if (filter.departureDate) {
      queryParams.departureDate = filter.departureDate;
    }
    if (filter.noAdults) {
      queryParams.noAdults = filter.noAdults;
    }
    if (filter.currency) {
      queryParams.currency = filter.currency;
    }
    if (filter.nonStops !== undefined) {
      queryParams.nonStops = filter.nonStops;
    }

    SearchFlightsService.getFlights(queryParams).then((data) => {
      setFlights(data.flights);
      setTotalPages(data.totalPages);
      setLoading(false);
    });
  }, [page, filter]);

  return (
    <div className="App">
      <Filter
        onApplyFilter={(params) => {
          setFilter((prev) => prev
            ? { ...prev, ...params }
            : { ...params } as QueryParams
          );
        }}
      />
    </div>
  );
}

export default App;

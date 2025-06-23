import { useState, useCallback } from 'react';
import { QueryParams } from '../../Interfaces/QueryParams';
import Filter from '../Filter/Filter';
import './Home.css';
import { Flight } from '../../Interfaces/Flight';
import { SearchFlightsService } from '../../Services/FlightSearchService';
import FlightList from '../FlightList/FlightList';

function Home() {
  const [flights, setFlights] = useState<Flight[]>([]);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [filter, setFilter] = useState<QueryParams>({} as QueryParams);
  const [loading, setLoading] = useState(false);

  const applyFilter = useCallback((newFilter: QueryParams) => {
    setLoading(true);
    setFilter(newFilter);

    const queryParams: QueryParams = {
      ...newFilter,
      page: page,
    };

    SearchFlightsService.getFlights(queryParams).then((data) => {
      setFlights(data.flights);
      setTotalPages(data.totalPages);
      setLoading(false);
      console.log('Flights fetched:', data.flights);
    }).catch(err => {
      console.error('Error fetching flights:', err);
      setLoading(false);
    });
  }, [page]);

  return (
    <div className="Home">
      <Filter onApplyFilter={applyFilter} />
      <FlightList flights={flights} loading={loading} />
    </div>
  );
}

export default Home;
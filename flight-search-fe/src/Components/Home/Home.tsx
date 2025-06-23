import { useState, useCallback } from 'react';
import { QueryParams } from '../../Interfaces/QueryParams';
import Filter from '../Filter/Filter';
import './Home.css';
import { Flight } from '../../Interfaces/Flight';
import { SearchFlightsService } from '../../Services/FlightSearchService';
import FlightList from '../FlightList/FlightList';
import { Sorter } from '../../Interfaces/Sorter';

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
    }).catch(err => {
      console.error('Error fetching flights:', err);
      setLoading(false);
    });
  }, [page]);

  const onChangeSorter = useCallback((sortBy: Sorter) => {
    setLoading(true);
    const queryParams: QueryParams = {
      ...filter,
      page: page,
      sortBy: sortBy,
    };

    SearchFlightsService.getFlights(queryParams).then((data) => {
      setFlights(data.flights);
      setTotalPages(data.totalPages);
      setLoading(false);
    }).catch(err => {
      console.error('Error fetching flights:', err);
      setLoading(false);
    });
  }, [filter, page]);

  return (
    <div className="Home">
      <Filter onApplyFilter={applyFilter} />
      <FlightList flights={flights} loading={loading} onChangeSorter={onChangeSorter}/>
    </div>
  );
}

export default Home;
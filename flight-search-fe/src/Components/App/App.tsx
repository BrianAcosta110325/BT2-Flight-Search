import { useState } from 'react';
import { QueryParams } from '../../Interfaces/QueryParams';
import Filter from '../Filter/Filter';
import './App.css';
import { Flight } from '../../Interfaces/Flight';

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

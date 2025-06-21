import { useState } from 'react';
import { QueryParams } from '../../Interfaces/QueryParams';
import { SearcherDropdown } from './SearcherDropdown';

interface FilterProps {
  onApplyFilter: (filterData: QueryParams) => void;
}

function Filter({ onApplyFilter }: FilterProps) {
  const [query, setQuery] = useState<QueryParams>({
    page: 1,
    departureCode: '',
    arrivalCode: '',
    currency: 'USD',
    nonStops: false,
  });

  const handleAirportSearch = (code: string) => {
    setQuery(prev => ({ ...prev, departureCode: code }));

    // Puedes aplicar filtro directamente:
    const updatedQuery: QueryParams = {
      ...query,
      departureCode: code,
    };

    onApplyFilter(updatedQuery);
  };

  return (
    <div className="container mt-4">
      <div className="card p-3 mb-4">
        <div className="g-3 align-items-center">
          <SearcherDropdown onSearch={handleAirportSearch} />
        </div>
      </div>
    </div>
  );
}

export default Filter;

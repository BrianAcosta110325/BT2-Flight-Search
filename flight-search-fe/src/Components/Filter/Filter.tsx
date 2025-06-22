import { useState } from 'react';
import { QueryParams } from '../../Interfaces/QueryParams';
import { SearcherDropdown } from './SearcherDropdown';
import './Filter.css';

interface FilterProps {
  onApplyFilter: (filterData: QueryParams) => void;
}

function Filter({ onApplyFilter }: FilterProps) {
  const [query, setQuery] = useState<QueryParams>({
    page: 1,
    departureCode: '',
    arrivalCode: '',
    departureDate: '',
    returnDate: '',
    currency: 'USD',
    nonStops: false,
  });

  const handleSearch = () => {
    onApplyFilter(query);
  };

  const handleAirportChange = (type: 'departureCode' | 'arrivalCode', code: string) => {
    setQuery((prev) => ({ ...prev, [type]: code }));
  };

  return (
    <div className="container mt-4">
      <div className="card p-4">
        {/* Airports Row */}
        <div className="row mb-3">
          <div className="col-6">
            <label className="form-label">Departure Airport</label>
            <SearcherDropdown onSearch={(code) => handleAirportChange('departureCode', code)} />
          </div>
          <div className="col-6">
            <label className="form-label">Arrival Airport</label>
            <SearcherDropdown onSearch={(code) => handleAirportChange('arrivalCode', code)} />
          </div>
        </div>

        {/* Dates Row */}
        <div className="row mb-3">
          <div className="col">
            <label className="form-label">Departure Date</label>
            <input
              type="date"
              className="form-control"
              value={query.departureDate || ''}
              onChange={(e) => setQuery((prev) => ({ ...prev, departureDate: e.target.value }))}
            />
          </div>
          <div className="col">
            <label className="form-label">Return Date</label>
            <input
              type="date"
              className="form-control"
              value={query.returnDate || ''}
              onChange={(e) => setQuery((prev) => ({ ...prev, returnDate: e.target.value }))}
            />
          </div>
        </div>

        <div className="row mb-3">
          <div className="col">
            <label className="form-label">Currency</label>
            <select
              className="form-select"
              value={query.currency}
              onChange={(e) => setQuery((prev) => ({ ...prev, currency: e.target.value }))}
            >
              <option value="USD">USD</option>
              <option value="EUR">EUR</option>
              <option value="MXN">MXN</option>
              <option value="GBP">GBP</option>
            </select>
          </div>

          <div className="col">
            <label className="form-label" style={{ visibility: 'hidden' }}>.</label>
            <div className="form-check mb-3">
              <input
                className="form-check-input"
                type="checkbox"
                id="nonStopCheck"
                checked={query.nonStops}
                onChange={(e) => setQuery((prev) => ({ ...prev, nonStops: e.target.checked }))}
              />
              <label className="form-check-label" htmlFor="nonStopCheck">
                Non-stop
              </label>
            </div>
          </div>
        </div>

        <div className="d-grid">
          <button className="btn btn-secondary" onClick={handleSearch}>
            Search
          </button>
        </div>
      </div>
    </div>
  );
}

export default Filter;
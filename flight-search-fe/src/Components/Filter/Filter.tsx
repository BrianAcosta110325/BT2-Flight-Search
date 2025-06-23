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

  const [errors, setErrors] = useState({
    departureCode: '',
    arrivalCode: '',
    departureDate: '',
  });

  const handleSearch = () => {
    const newErrors: typeof errors = {
      departureCode: '',
      arrivalCode: '',
      departureDate: '',
    };

    const today = new Date();
    const departureDateObj = query.departureDate ? new Date(query.departureDate) : null;

    if (!query.departureCode) {
      newErrors.departureCode = 'Departure airport is required.';
    }

    if (!query.arrivalCode) {
      newErrors.arrivalCode = 'Arrival airport is required.';
    }

    if (!query.departureDate) {
      newErrors.departureDate = 'Departure date is required.';
    } else if (
      console.log(departureDateObj, today),
      departureDateObj &&
      departureDateObj.getTime() < new Date(today.setHours(0, 0, 0, 0)).getTime()
    ) {
      newErrors.departureDate = 'Departure date must be today or later.';
    }

    setErrors(newErrors);

    const hasErrors = Object.values(newErrors).some((e) => e !== '');
    if (hasErrors) return;

    onApplyFilter(query);
  };

  const handleAirportChange = (type: 'departureCode' | 'arrivalCode', code: string) => {
    setQuery((prev) => ({ ...prev, [type]: code }));
    setErrors((prev) => ({ ...prev, [type]: '' }));
  };

  return (
    <div className="container mt-4">
      <div className="card card-filter p-4">
        {/* Airports Row */}
        <div className="row mb-3">
          <div className="col-6">
            <label className="form-label">Departure Airport</label>
            <SearcherDropdown onSearch={(code) => handleAirportChange('departureCode', code)} />
            {errors.departureCode && <div className="text-danger mt-1">{errors.departureCode}</div>}
          </div>
          <div className="col-6">
            <label className="form-label">Arrival Airport</label>
            <SearcherDropdown onSearch={(code) => handleAirportChange('arrivalCode', code)} />
            {errors.arrivalCode && <div className="text-danger mt-1">{errors.arrivalCode}</div>}
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
              onChange={(e) => {
                setQuery((prev) => ({ ...prev, departureDate: e.target.value }));
                setErrors((prev) => ({ ...prev, departureDate: '' }));
              }}
            />
            {errors.departureDate && <div className="text-danger mt-1">{errors.departureDate}</div>}
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

        {/* Currency and NonStop */}
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

        {/* Search Button */}
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
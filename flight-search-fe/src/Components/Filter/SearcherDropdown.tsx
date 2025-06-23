import { useState } from "react";
import { Dropdown } from "react-bootstrap";
import { SearchFlightsService } from "../../Services/FlightSearchService";

interface Airport {
  iataCode: string;
  name: string;
}

interface SearcherDropdownProps {
  onSearch: (airportCode: string) => void;
}

export const SearcherDropdown = ({ onSearch }: SearcherDropdownProps) => {
  const [show, setShow] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const [items, setItems] = useState<Airport[]>([]);
  const [selectedCode, setSelectedCode] = useState<string>('');
  const [selectedName, setSelectedName] = useState<string>('');
  const [loading, setLoading] = useState(false);
  const [hasSearched, setHasSearched] = useState(false);

  const handleToggle = (isOpen: boolean) => {
    setShow(isOpen);
    if (!isOpen) {
      setInputValue('');
      setItems([]);
      setHasSearched(false);
    }
  };

  const handleSearch = async () => {
    if (inputValue.trim().length < 2) return;
    setLoading(true);
    setHasSearched(true);
    try {
      const airports = await SearchFlightsService.getAirports(inputValue);
      setItems(airports);
    } catch (error) {
      console.error("Error searching airports:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleItemClick = (code: string, name: string) => {
    setSelectedCode(code);
    setSelectedName(name);
    onSearch(code);
    setShow(false);
  };

  return (
    <Dropdown show={show} onToggle={handleToggle}>
      <Dropdown.Toggle variant="primary" id="dropdown-basic">
        {selectedCode ? `${selectedCode} - ${selectedName}` : "Select an airport"}
      </Dropdown.Toggle>

      <Dropdown.Menu style={{ minWidth: '300px', padding: '1rem' }}>
        <input
          type="text"
          className="form-control mb-2"
          placeholder="Search airport..."
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
        />
        <button
          className="btn btn-sm btn-primary w-100 mb-2"
          onClick={handleSearch}
          disabled={inputValue.trim().length < 2}
        >
          Search Airports
        </button>

        <div className="dropdown-list mb-2" style={{ maxHeight: 200, overflowY: 'auto' }}>
          {loading && <div className="text-center text-muted">Loading...</div>}

          {!loading && inputValue.trim().length < 2 && (
            <div className="text-center text-muted">Write at least 2 characters.</div>
          )}

          {!loading && hasSearched && items.length === 0 && inputValue.trim().length >= 2 && (
            <div className="text-center text-muted">No results found.</div>
          )}

          {!loading && items.map((item) => (
            <Dropdown.Item
              key={item.iataCode}
              onClick={() => handleItemClick(item.iataCode, item.name)}
              active={selectedCode === item.iataCode}
            >
              {item.iataCode} - {item.name}
            </Dropdown.Item>
          ))}
        </div>
      </Dropdown.Menu>
    </Dropdown>
  );
};
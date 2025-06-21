import { useState } from "react";
import { Dropdown } from "react-bootstrap";
import { SearchFlightsService } from "../../Services/FlightSearchService";

interface Airport {
  code: string;
  name: string;
}

interface SearcherDropdownProps {
  onSearch: (airportCode: string) => void;
}

export const SearcherDropdown = ({ onSearch }: SearcherDropdownProps) => {
  const [show, setShow] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const [items, setItems] = useState<Airport[]>([]);
  const [selectedCode, setSelectedCode] = useState<string>("");

  const handleToggle = (isOpen: boolean) => {
    setShow(isOpen);
    if (!isOpen) {
      setInputValue('');
      setItems([]);
      setSelectedCode('');
    }
  };

  const handleSearch = async () => {
    if (inputValue.trim().length < 2) return;
    try {
      console.log("Buscando aeropuertos con:", inputValue);
      const airports = await SearchFlightsService.getAirports(inputValue);
      setItems(airports);
      console.log("Aeropuertos encontrados:", airports);
    } catch (error) {
      console.error("Error al buscar aeropuertos:", error);
    }
  };

  const handleSelect = () => {
    if (selectedCode) {
      onSearch(selectedCode);
      setShow(false);
    }
  };

  return (
    <Dropdown show={show} onToggle={handleToggle}>
      <Dropdown.Toggle variant="primary" id="dropdown-basic">
        Selecciona aeropuerto
      </Dropdown.Toggle>

      <Dropdown.Menu style={{ minWidth: '300px', padding: '1rem' }}>
        <input
          type="text"
          className="form-control mb-2"
          placeholder="Buscar aeropuerto..."
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
        />
        <button className="btn btn-sm btn-primary w-100 mb-2" onClick={handleSearch}>
          Buscar aeropuertos
        </button>

        <div className="dropdown-list mb-2" style={{ maxHeight: 200, overflowY: 'auto' }}>
          {items.map((item) => (
            <Dropdown.Item
              key={item.code}
              onClick={() => setSelectedCode(item.code)}
              active={selectedCode === item.code}
            >
              {item.code} - {item.name}
            </Dropdown.Item>
          ))}
        </div>

        <button
          className="btn btn-sm btn-success w-100"
          onClick={handleSelect}
          disabled={!selectedCode}
        >
          Seleccionar aeropuerto
        </button>
      </Dropdown.Menu>
    </Dropdown>
  );
};
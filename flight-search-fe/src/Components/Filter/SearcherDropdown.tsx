import { SetStateAction, useState } from "react";
import { Dropdown } from "react-bootstrap";


export const SearcherDropdown = () => {

    const [show, setShow] = useState(false);
    const [inputValue, setInputValue] = useState('');
    const [items, setItems] = useState(['Elemento 1', 'Elemento 2', 'Elemento 3']);

    const handleToggle = (isOpen: SetStateAction<boolean>) => {
        setShow(isOpen);
    };

    const handleSearch = () => {
        alert(`Buscando: ${inputValue}`);
    };

    return (
        <Dropdown show={show} onToggle={handleToggle}>
            <Dropdown.Toggle variant="primary" id="dropdown-basic">
                Menú personalizado
            </Dropdown.Toggle>

            <Dropdown.Menu style={{ minWidth: '250px', padding: '1rem' }}>
                <input
                type="text"
                className="form-control mb-2"
                placeholder="Escribe algo..."
                value={inputValue}
                onChange={(e) => setInputValue(e.target.value)}
                />
                <button className="btn btn-sm btn-primary w-100 mb-2" onClick={handleSearch}>
                Buscar
                </button>
                <div className="dropdown-list">
                {items.map((item, index) => (
                    <Dropdown.Item key={index}>{item}</Dropdown.Item>
                ))}
                </div>
            </Dropdown.Menu>
        </Dropdown>
    );
}
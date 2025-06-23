import { Routes, Route } from 'react-router-dom';
import Home from './Components/Home/Home';
import FlightDetails from './Components/FlightDetails/FlightDetails';

function App() {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/flight-details/:id" element={<FlightDetails />} />
    </Routes>
  );
}

export default App;
import { useState } from 'react';
import { QueryParams } from '../../Interfaces/QueryParams';
import Filter from '../Filter/Filter';
import './App.css';

function App() {

  // Filter
  const [filterParams, setFilterParams] = useState<QueryParams>();

  return (
    <div className="App">
      <Filter
        onApplyFilter={(params) => {
          setFilterParams((prev) => prev
            ? { ...prev, ...params }
            : { ...params } as QueryParams
          );
        }}
      />
    </div>
  );
}

export default App;

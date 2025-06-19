import React, { SetStateAction, useState } from 'react'
import { QueryParams } from '../../Interfaces/QueryParams';
import { SearcherDropdown } from './SearcherDropdown';
import { Dropdown } from 'react-bootstrap';

interface FilterProps {
  onApplyFilter: (filterData: QueryParams) => void;
}

// interface CheckboxOption {
//   id: string;
//   checked: boolean;
// }

function Filter({ onApplyFilter }: FilterProps) {
  const [searchText, setSearchText] = useState("");

  const [currency, setCurrency] = useState([
    { id: 'USD' },
    { id: 'MXN' },
    { id: 'EUR'},
  ]);

  const [stops, setStops] = useState([
    { id: 'Done', checked: false },
    { id: 'Undone', checked: false },
  ]);

  // const updatePriorities = (updatedPriorities: CheckboxOption[]) => {
  //   setPriorities(updatedPriorities);
  // };

  // const updateStatus = (updatedStatus: CheckboxOption[]) => {
  //   setStatus(updatedStatus);
  // };

  // const applyFilter = () => {
  //   const completed = status[0].checked === status[1].checked ? null : status[0].checked;
  //   const prioritiesChecked: string[] = priorities.filter(option => option.checked).map(option => option.id);

  //   const queryParams: QueryParams = {
  //     text: searchText,
  //   };

  //   if (completed != null) queryParams.completed = completed.toString();
  //   if (prioritiesChecked.length > 0) queryParams.priorities = prioritiesChecked.join(',');

  //   onApplyFilter(queryParams);
  // };

  return (
    <div className="container mt-4">
      {/* Filter */}
      <div className="card p-3 mb-4">
        <div className="g-3 align-items-center">
          <SearcherDropdown />
        </div>
      </div>
    </div>
  );
}

export default Filter

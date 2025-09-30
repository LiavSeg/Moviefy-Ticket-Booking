// components/SearchBar.jsx
import React from 'react';
import { FaSearch } from 'react-icons/fa';
import './SearchBar.css';

export default function SearchBar({ value, onChange, onSubmit }) {
  return (
    <form className="search-bar-main" onSubmit={onSubmit}>
      <input
        type="text"
        placeholder="Search Moviefy..."
        value={value}
        onChange={(e) => onChange(e.target.value)}
      />
      <button  >
        <FaSearch size={20}/>
      </button>
    </form>
  );
}

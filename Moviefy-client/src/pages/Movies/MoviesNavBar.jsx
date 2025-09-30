import React from 'react';
import './styles/MoviesNavBar.css';
const tabs = [
  'MOVIES IN THEATERS',
  'COMING SOON',
  'ALL TIMES FANS FAVORITES',
];

function MoviesNavBar({ selected, onSelect }) {
  return (
    <div className="navigation-tabs">
      <div className="nav-title">
        <span>MOVIES</span>
        <span className="highlight">{selected}</span>
      </div>
      <div className="tabs-list">
        {tabs.map((tab, i) => (
          <button
            key={i}
            className={`tab-btn ${selected === tab ? 'active' : ''}`}
            onClick={() => onSelect(tab)}
          >
            {tab}
          </button>
        ))}
      </div>
    </div>
  );
}
 export default MoviesNavBar;

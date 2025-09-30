import React from 'react';
import './DateSelector.css';

export default function DateSelector({ dateKeys = [], selectedDate, onSelect }) {
  const hasDates = Array.isArray(dateKeys) && dateKeys.length > 0;

  const title = hasDates ? 'Select a date' : 'No showtimes available';
  const help  = hasDates ? 'Choose a date to view sessions.' : 'New dates will appear here once scheduled.';

  return (
    <div className="showtime-date-selector" role="region" aria-label="Showtime dates">
      <div className="section-header">
        <h3 className="section-title">{title}</h3>
        <p className="section-help">{help}</p>
      </div>

      {hasDates && (
        <div className="date-scroll-row" role="tablist" aria-label="Available dates">
          {dateKeys.map((dateKey) => {
            // Ensure stable parsing
            const date = new Date(`${dateKey}T00:00:00`);
            const dow  = date.toLocaleDateString('en-US', { weekday: 'short' }); // Tue
            const mon  = date.toLocaleDateString('en-US', { month: 'short' });   // Aug
            const day  = date.toLocaleDateString('en-US', { day: 'numeric' });   // 13
            const isActive = selectedDate === dateKey;

            return (
              <button
                key={dateKey}
                type="button"
                className={`date-box ${isActive ? 'active' : ''}`}
                onClick={() => onSelect(dateKey)}
                aria-pressed={isActive}
                aria-label={`${dow}, ${mon} ${day}`}
              >
                <span className="dow">{dow}</span>
                <span className="day">{day}</span>
                <span className="mon">{mon}</span>
              </button>
            );
          })}
        </div>
      )}
    </div>
  );
}

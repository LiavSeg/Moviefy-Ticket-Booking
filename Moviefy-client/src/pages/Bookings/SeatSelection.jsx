// src/components/seating/SeatSelection.jsx
import React, { useEffect, useState } from 'react';
import { FaChair } from 'react-icons/fa';
import { getSeatAvailability, holdSeats } from '../../api/seats';
import './SeatSelection.css';

export default function SeatSelection({ showtimeId, selectedSeats, onSelectChange, onContinue }) {
  const [seats, setSeats] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    refreshAvailability();
    const interval = setInterval(refreshAvailability, 5000);
    return () => clearInterval(interval);
  }, [showtimeId]);

  const refreshAvailability = async () => {
    try {
      const data = await getSeatAvailability(showtimeId);
      setSeats(data);
      setError('');
    } catch (err) {
      console.error('Failed to load seat availability:', err);
      if (seats.length === 0) {
        setError('Failed to load seat availability.');
      }
    }
  };

  const toggleSelect = (label) => {
    onSelectChange((prev) =>
      prev.includes(label)
        ? prev.filter((s) => s !== label)
        : [...prev, label]
    );
  };

  const handleConfirm = async () => {
    try {
      setLoading(true);
      await holdSeats(showtimeId, selectedSeats); // HOLD בלבד
      onContinue(); // ShowtimePage יעביר ל-BookingForm
    } catch (err) {
      setError(err.response?.data || 'Failed to hold seats.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="seat-selection-3d">
      <div className="screen">SCREEN</div>

      {error && <div className="error">{error}</div>}

      <div className="seats-container">
        {seats.length > 0 &&
          Array.from(new Set(seats.map((s) => s.seatLabel[0]))).map((row) => (
            <div className="row" key={row}>
              <div className="row-label">{row}</div>
              {seats
                .filter((seat) => seat.seatLabel.startsWith(row))
                .map((seat) => (
                  <div
                    key={seat.seatLabel}
                    className={`seat ${seat.state.toLowerCase()} ${
                      selectedSeats.includes(seat.seatLabel) ? 'selected' : ''
                    }`}
                    onClick={() =>
                      seat.state === 'AVAILABLE' &&
                      toggleSelect(seat.seatLabel)
                    }
                  >
                    <FaChair />
                  </div>
                ))}
            </div>
          ))}
      </div>

      <div className="legend">
        <div><FaChair className="available" /> Available</div>
        <div><FaChair className="selected" /> Selected</div>
        <div><FaChair className="reserved" /> Reserved</div>
        <div><FaChair className="sold" /> Sold</div>
      </div>

      <div className="actions" style={{ textAlign: 'center' }}>
        <button
          className="confirm-button"
          onClick={handleConfirm}
          disabled={!selectedSeats.length || loading}
        >
          Confirm Seats
        </button>
      </div>
    </div>
  );
}

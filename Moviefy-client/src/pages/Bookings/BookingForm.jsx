import React, { useState, useEffect, useMemo } from 'react';
import { createBooking } from "../../api/bookings";
import './BookingForm.css';
import { FaCheckCircle, FaTimesCircle, FaSpinner, FaTimes } from "react-icons/fa";

export default function BookingForm({ showtimeId, seats = [], onSuccess, showtime, movie }) {
  const [status, setStatus] = useState('idle');   
  const [message, setMessage] = useState('');
  const [timeLeft, setTimeLeft] = useState(300);  

  // Reset timer when seats change
  useEffect(() => {
    if (seats.length > 0) setTimeLeft(300);
  }, [seats]);

  // Tick timer
  useEffect(() => {
    if (timeLeft <= 0) return;
    const id = setInterval(() => setTimeLeft(prev => prev - 1), 1000);
    return () => clearInterval(id);
  }, [timeLeft]);

  const formatTime = (seconds) => {
    const min = Math.floor(seconds / 60);
    const sec = seconds % 60;
    return `${min}:${sec.toString().padStart(2, '0')}`;
  };

  const perSeatPrice = useMemo(() => Number(showtime?.price || 0), [showtime]);
  const seatCount = seats?.length || 0;
  const totalPrice = useMemo(
    () => Number((perSeatPrice * seatCount).toFixed(2)),
    [perSeatPrice, seatCount]
  );

  const pctLeft = useMemo(() => Math.max(0, Math.min(100, (timeLeft / 300) * 100)), [timeLeft]);
  const urgency = useMemo(() => (pctLeft > 66 ? 'ok' : pctLeft > 33 ? 'warn' : 'danger'), [pctLeft]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (timeLeft <= 0) {
      setStatus('error');
      setMessage('Reservation expired. Please select seats again.');
      return;
    }
    if (seatCount === 0) return;

    setStatus('loading');
    setMessage('');
    console.log(movie);
    const CURRENT_USER_ID = localStorage.getItem("userId");
    const bookingDto = {
      userId: CURRENT_USER_ID,
      showtimeId,
      movieTitle: movie?.title,
      theater: showtime?.theater,
      totalPrice,
      seats
    };

    try {
      await createBooking(bookingDto);
      setStatus('success');
      setMessage(`Booking successful for ${bookingDto.seats.join(', ')}`);
      setTimeout(() => onSuccess?.(), 900);
    } catch (err) {
      const apiMsg = err?.response?.data?.message || err?.response?.data || err?.message;
      setStatus('error');
      setMessage(apiMsg || 'Unknown error');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="booking-form" aria-live="polite">
      {/* Countdown */}
      <div className="timer-row">
        <div
          className={`countdown-ring ${urgency}`}
          style={{
            '--pct': `${pctLeft}%`,
          }}
          role="timer"
          aria-label={`Time left to confirm: ${formatTime(timeLeft)}`}
        >
          <div className="ring-inner">
            <span className="ring-time">{formatTime(timeLeft)}</span>
            <span className="ring-label">hold</span>
          </div>
        </div>

        <div className="summary">
          <div className="movie-line">
            <span className="label">Movie</span>
            <span className="value">{movie?.title || '—'}</span>
          </div>
          <div className="movie-line">
            <span className="label">Theater</span>
            <span className="value">{showtime?.theater || '—'}</span>
          </div>
          <div className="price-line">
            <span className="label">Price</span>
            <span className="value">
              {perSeatPrice.toFixed(2)} × {seatCount} = <strong>{totalPrice.toFixed(2)}</strong>
            </span>
          </div>
        </div>
      </div>

      {/* Seats */}
      <div className="seat-review">
        <label>Seats selected</label>
        <div className="seat-pills">
          {Array.isArray(seats) && seats.length > 0 ? (
            seats.map(s => (
              <span key={s} className="pill" title={`Seat ${s}`}>{s}</span>
            ))
          ) : (
            <span className="no-seats">No seats selected</span>
          )}
        </div>
      </div>

      {/* Submit */}
      <button
        type="submit"
        className="cta"
        disabled={status === 'loading' || seatCount === 0 || timeLeft <= 0}
      >
        {status === 'loading' ? <FaSpinner className="spin" /> : 'Confirm Booking'}
        <span className="btn-shimmer" />
      </button>

      {/* Messages */}
      {status === 'success' && (
        <div className="message success" role="status">
          <FaCheckCircle />
          <span>{message}</span>
        </div>
      )}

      {status === 'error' && (
        <div className="message error" role="alert">
          <FaTimesCircle />
          <span>{message}</span>
          <button
            type="button"
            className="dismiss"
            aria-label="Dismiss error"
            onClick={() => { setStatus('idle'); setMessage(''); }}
          >
            <FaTimes />
          </button>
        </div>
      )}

      {timeLeft <= 0 && (
        <p className="expired-warning">
          Your seat reservation has expired. Please go back and select seats again.
        </p>
      )}
    </form>
  );
}

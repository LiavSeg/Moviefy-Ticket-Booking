import React from 'react';
import './ShowtimeCarousel.css'; 

export default function ShowtimeCarousel({selectedDate, groupedShowtimes,onSelectShowtime}) {

  if (!selectedDate || !groupedShowtimes[selectedDate]) return null;
  return(
  <div className="showtime-carousel fade-in">
    <h4 className="carousel-title">Showtimes for {new Date(selectedDate).toDateString()}</h4>
    <div className="carousel-container">
      {groupedShowtimes[selectedDate].map((s) => (
        <div 
            key={s.id} 
            className="showtime-card" 
            onClick={() =>onSelectShowtime(s.id)}
        >
            <div className="showtime-time">
                {new Date(s.startTime).toLocaleTimeString([],
                { hour: '2-digit', minute: '2-digit' })}-{new Date(s.endTime).toLocaleTimeString([],
                { hour: '2-digit', minute: '2-digit' })}
            </div>
          <div className="showtime-theater">{s.theater}</div>
          <div className="showtime-price">Price: {s.price}$</div>
        </div>
      ))}
    </div>
  </div>
);
}
import React, { useState,useEffect  } from 'react';
import './UserBookingList.css';
import { useNavigate,useParams } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

function UserBookingsList({ bookings = [], expandedBookingId, setExpandedBookingId }) {
  const [loadingTicketId, setLoadingTicketId] = useState(null);


  const { user,loading } = useAuth();
  const navigate = useNavigate();
  useEffect(() => {
    let us =localStorage.getItem('user');
    if (us)
      us = JSON.parse(us);
    if (loading) return;                 
    if (!user || !user.userId || us && us.userId!==user.userId){
      navigate('/', { replace: true });
    }
  }, [loading, user, navigate]); 
   return (
    <section className="section-block">
      <h3>My Bookings</h3>

      {bookings.length === 0 ? (
        <p>No bookings found.</p>
      ) : (
        <div className="booking-list">
          {bookings.map((b) => (
            <div
              key={b.bookingId}
              className={`booking-card ${expandedBookingId === b.bookingId ? 'expanded' : ''}`}
              onClick={() =>
                setExpandedBookingId(
                  expandedBookingId === b.bookingId ? null : b.bookingId
                )
              }
            >
              <div className="booking-header">
    
                <div className="booking-info">
                  <h4>• Movie: {b.movieTitle }</h4>
                  <h4>• Showtime:  {b.bookingDateTime}</h4>
                </div>
              </div>

{expandedBookingId === b.bookingId && (
  <div className="booking-extra">
    <p><strong>Booking ID:</strong> {b.bookingId}</p>
    <p><strong>Name:</strong> {b.userName}</p>
    <p><strong>Order Time:</strong> {b.timeOfOrder}</p>
    <p><strong>Seats:</strong> {b.seatList.join(', ')}</p>
    <p><strong>Total Price:</strong> ${b.totalPrice.toFixed(2)}</p>
    
<button
  className="ticket-button"
  disabled={loadingTicketId === b.bookingId}
  onClick={async (e) => {
    e.stopPropagation();
    setLoadingTicketId(b.bookingId);
    try {
      const res = await fetch(`http://localhost:8080/bookings/${b.bookingId}/pdf`);
      const blob = await res.blob();
      const url = URL.createObjectURL(blob);
      window.open(url, '_blank');
    } catch (err) {
      console.error(err);
      alert("Failed to generate ticket PDF");
    } finally {
      setLoadingTicketId(null);
    }
  }}
>
  {loadingTicketId === b.bookingId ?'...Generating PDF...' : ' View Ticket (PDF)'}
</button>

  </div>
)}
            </div>
          ))}
        </div>
      )}
    </section>
  );
}

export default UserBookingsList;

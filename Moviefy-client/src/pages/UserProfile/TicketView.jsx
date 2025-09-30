import { useState } from 'react';
import './TicketView.css'; 

export default function TicketView() {
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch(`/bookings/${bookingId}/ticket`, {
      method: 'GET',
    })
      .then((res) => res.blob())
      .then((blob) => {
        const url = URL.createObjectURL(blob);
        window.open(url, '_blank');
        setLoading(false);
      })
      .catch(() => {
        alert("Failed to load ticket");
        setLoading(false);
      });
  }, []);

  return (
    <div className="ticket-view-container">
      {loading ? (
        <div className="spinner">
          <div className="loader"></div>
          <p>Generating your ticket...</p>
        </div>
      ) : (
        <p>✅ Ticket opened in new tab.</p>
      )}
    </div>
  );
}

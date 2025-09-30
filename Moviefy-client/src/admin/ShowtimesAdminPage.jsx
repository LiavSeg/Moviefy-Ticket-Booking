import React, { useEffect, useState } from 'react';
import { getAllShowtimes } from '../api/admin';
import './ShowtimesAdminPage.css';
import EditShowtimeModal from '../modals/EditShowtimeModal.jsx';
export default function ShowtimeAdminPage() {
  const [showtimes, setShowtimes] = useState([]);
  const [selectedMovie, setSelectedMovie] = useState(null);
  const [editTarget, setEditTarget] = useState(null);
  const [editOpen, setEditOpen] = useState(false);
  const openEdit = (s) => {
    setEditTarget(s);
    setEditOpen(true);
    console.log(s);
  };

  const closeEdit = () => {
    setEditOpen(false);
    setEditTarget(null);
  };
    const applySaved = (partial) => {
    if (!editTarget) return;
    setShowtimes((prev) =>
      prev.map((st) =>
        st.id === editTarget.id ? { ...st, ...partial } : st
      )
    );
  };

  useEffect(() => {
    const fetch = async () => {
      const res = await getAllShowtimes();
      setShowtimes(res.data);
    };
    fetch();
  }, []);

  return (
    <section className="admin-section">
      <h2>Showtime Management</h2>

      <table className="admin-table">
        <thead>
          <tr>
            <th>Theater</th>
            <th>Start</th>
            <th>End</th>
            <th>Price</th>
            <th>Movie Info</th>
            <th>Edit</th>
            {/* <th>Delete</th> */}
          </tr>
        </thead>
        <tbody>
          {showtimes.map((s) => (
            <tr key={s.id}>
              <td>{s.theater}</td>
              <td>{s.startTime}</td>
              <td>{s.endTime}</td>
              <td>${s.price?.toFixed(2)}</td>
              <td className="admin-actions">
                <button onClick={() => setSelectedMovie(s.movie)}>View Movie</button>
              </td>
              <td className="admin-actions">
              <button onClick={() => openEdit(s)}>Edit</button>
              </td>
              {/* <td className="admin-actions">
                <button className="delete-btn">Delete</button>
              </td> */}
            </tr>
          ))}
        </tbody>
      </table>

      {selectedMovie && (
        <div className="modal-backdrop" onClick={() => setSelectedMovie(null)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <h3>{selectedMovie.title}</h3>
            <p><strong>Genre:</strong> {selectedMovie.genre}</p>
            <p><strong>Release Date:</strong> {selectedMovie.releaseDate}</p>
            <p><strong>Language:</strong> {selectedMovie.language}</p>
            <p><strong>Description:</strong> {selectedMovie.description}</p>
            <button onClick={() => setSelectedMovie(null)}>Close</button>
          </div>
        </div>
      )}
        <EditShowtimeModal
        open={editOpen}
        showtime={editTarget}
        onClose={closeEdit}
        onSaved={applySaved}
      />
    </section>
  );
}

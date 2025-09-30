import React, { useEffect, useState } from 'react';
import { getAllMovies, DeleteMovie } from '../api/movies';
import EditMovieModal from '../modals/EditMovieModal';
import './MovieAdminPage.css';

export default function MovieAdminPage() {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deletingId, setDeletingId] = useState(null);

  // Modal state
  const [isEditOpen, setIsEditOpen] = useState(false);
  const [editingMovie, setEditingMovie] = useState(null);

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const res = await getAllMovies();
        setMovies(res.data || []);
      } catch (err) {
        console.error(err);
        alert('Failed to load movies.');
      } finally {
        setLoading(false);
      }
    };
    fetchMovies();
  }, []);

  const handleDelete = async (movieId, title) => {
    const ok = window.confirm(`Delete "${title}"? This cannot be undone.`);
    if (!ok) return;

    try {
      setDeletingId(movieId);
      await DeleteMovie(movieId);
      setMovies(prev => prev.filter(m => (m.movieId ?? m.id) !== movieId));
    } catch (e) {
      console.error(e);
      alert('Failed to delete movie.');
    } finally {
      setDeletingId(null);
    }
  };

  const openEdit = (movie) => {
    setEditingMovie(movie);
    console.log(movie);
    setIsEditOpen(true);
  };
  const closeEdit = () => {
    setIsEditOpen(false);
    setEditingMovie(null);
  };

  if (loading) {
    return (
      <section className="admin-section">
        <h2>Movie Management</h2>
        <p>Loading…</p>
      </section>
    );
  }

  return (
    <section className="admin-section">
      <h2>Movie Management</h2>
      <table className="admin-table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Genre</th>
            <th>Release Date</th>
            <th>Language</th>
            <th>Edit Movie</th>
            <th>Delete Movie</th>
          </tr>
        </thead>
        <tbody>
          {movies.map((m) => {
            const id = m.movieId ?? m.id;
            return (
              <tr key={id}>
                <td>{m.title}</td>
                <td>{m.genre}</td>
                <td>{m.releaseDate?.slice?.(0, 10) || m.releaseDate || '—'}</td>
                <td>{m.language}</td>
                <td className="admin-actions">
                  <button onClick={() => openEdit(m)}>Edit</button>
                </td>
                <td className="admin-actions">
                  <button
                    className="delete-btn"
                    onClick={() => handleDelete(id, m.title)}
                    disabled={deletingId === id}
                  >
                    {deletingId === id ? 'Deleting…' : 'Delete'}
                  </button>
                </td>
              </tr>
            );
          })}
          {movies.length === 0 && (
            <tr>
              <td colSpan="6" style={{ textAlign: 'center' }}>No movies found.</td>
            </tr>
          )}
        </tbody>
      </table>

      <EditMovieModal
        open={isEditOpen}
        movie={editingMovie}
        onClose={closeEdit}
        onSaved={() => {
        }}
      />
    </section>
  );
}

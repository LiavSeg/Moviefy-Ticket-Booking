import React, { useEffect, useMemo, useState } from 'react';
import { patchMovieField } from '../api/movies';
import './styles/EditMovieModal.css';

export default function EditMovieModal({ open, movie, onClose, onSaved }) {
  const [field, setField] = useState('trailerUrl');
  const [value, setValue] = useState('');
  console.log(movie)
  const fieldMeta = useMemo(() => ({
    trailerUrl: { type: 'url', placeholder: 'https://youtube.com/watch?v=...' },
    title:      { type: 'text', placeholder: 'Movie title' },
    language:   { type: 'text', placeholder: 'en / he / fr' },
    releaseDate:{ type: 'date', placeholder: '' },
  }), []);

  useEffect(() => {
    if (!movie) return;

    setField('trailerUrl');
    setValue(movie.trailerUrl || '');
  }, [movie]);

  if (!open || !movie) return null;

  const movieId = movie.movieId ?? movie.id;

  const handleSave = async () => {
    if (!field) return;
    const raw = (value ?? '').toString().trim();

    // Minimal validation for URL field
    if (field === 'trailerUrl' && raw && !/^https?:\/\//i.test(raw)) {
      const ok = window.confirm('The URL does not start with http/https. Continue anyway?');
      if (!ok) return;
    }

    try {
      const respone = await patchMovieField(movieId, field, raw);
      if (typeof onSaved === 'function') onSaved({ movieId, field, value: raw });
      onClose();
      if (respone.status==200)
        window.alert(field+" updated successfully")
    } catch (err) {
      console.error(err);
      const msg = err?.response?.data || 'Failed to update movie.';
      alert(msg);
    }
  };

  const { type, placeholder } = fieldMeta[field] || { type: 'text', placeholder: '' };

  return (
      <div className="emm-backdrop" onClick={onClose}>
        <div className="emm-modal" onClick={(e) => e.stopPropagation()}>
          <header className="emm-header">
            <h3>Edit Movie</h3>
            <button className="emm-close" onClick={onClose} aria-label="Close">×</button>
          </header>

          <div className="emm-body">
            <div className="emm-row">
              <label>Movie</label>
              <div className="emm-readonly">{movie.title}</div>
            </div>

            <div className="emm-row">
              <label htmlFor="emm-field-select">Field to update</label>
              <select
                  id="emm-field-select"
                  value={field}
                  onChange={(e) => {
                    const f = e.target.value;
                    setField(f);
                    // Pre-fills current value from movie if exists
                    if (f === 'trailerUrl') setValue(movie.trailerUrl || '');
                    else if (f === 'title') setValue(movie.title || '');
                    else if (f === 'language') setValue(movie.language || '');
                    else if (f === 'releaseDate') {
                      // Normalize to yyyy-MM-dd if server returns date-time
                      const d = movie.releaseDate?.slice?.(0, 10) || movie.releaseDate || '';
                      setValue(d);
                    } else setValue('');
                  }}
                  className="emm-select"
              >
                {/* You can limit to trailerUrl only by leaving just the first option */}
                <option value="trailerUrl">Trailer URL</option>
                <option value="title">Title</option>
                <option value="language">Language</option>
                <option value="releaseDate">Release Date</option>
              </select>
            </div>

            <div className="emm-row">
              <label htmlFor="emm-value-input">New value</label>
              <input
                  id="emm-value-input"
                  type={type}
                  placeholder={placeholder}
                  value={value}
                  onChange={(e) => setValue(e.target.value)}
                  className="emm-input"
              />
            </div>
          </div>

          <footer className="emm-actions">
            <button className="emm-btn secondary" onClick={onClose}>Cancel</button>
            <button className="emm-btn primary" onClick={handleSave}>Save</button>
          </footer>
        </div>
      </div>
  );
}
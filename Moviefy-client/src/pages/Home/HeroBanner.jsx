// src/components/HeroBanner.jsx
import React, { useEffect, useMemo, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './styles/HeroBanner.css';

const ADVANCE_MS = 6000;

export default function HeroBanner({ movies = [] }) {
  const [current, setCurrent] = useState(0);
  const [paused, setPaused] = useState(false);
  const touchStartX = useRef(null);
  const sliderRef = useRef(null);
  const navigate = useNavigate();

  const total = movies.length;

  useEffect(() => {
    if (total === 0) return;
    if (current > total - 1) setCurrent(0);
  }, [total, current]);

  useEffect(() => {
    if (total <= 1) return;
    if (paused) return;
    if (document.visibilityState === 'hidden') return;

    const id = setInterval(() => setCurrent((i) => (i + 1) % total), ADVANCE_MS);
    return () => clearInterval(id);
  }, [total, paused, current]);

  useEffect(() => {
    const onVis = () => setPaused((p) => document.visibilityState === 'hidden' ? true : p);
    document.addEventListener('visibilitychange', onVis);
    return () => document.removeEventListener('visibilitychange', onVis);
  }, []);

  useEffect(() => {
    if (total < 2) return;
    const next = (current + 1) % total;
    const url = movies[next]?.backdrop || movies[next]?.poster || movies[next]?.imageUrl;
    if (!url) return;
    const img = new Image();
    img.src = url;
  }, [current, total, movies]);

  const goTo = (idx) => setCurrent(((idx % total) + total) % total);
  const next = () => goTo(current + 1);
  const prev = () => goTo(current - 1);

  const onTouchStart = (e) => (touchStartX.current = e.touches[0].clientX);
  const onTouchEnd = (e) => {
    if (touchStartX.current == null) return;
    const dx = e.changedTouches[0].clientX - touchStartX.current;
    touchStartX.current = null;
    if (Math.abs(dx) < 40) return; 
    dx < 0 ? next() : prev();
  };

  const onKeyDown = (e) => {
    if (e.key === 'ArrowRight') { e.preventDefault(); next(); }
    if (e.key === 'ArrowLeft')  { e.preventDefault(); prev(); }
  };

  const currentMovie = movies[current] || {};
  const currentImg =
    currentMovie.backdrop || currentMovie.poster || currentMovie.imageUrl || '';
  const objectPosition = currentMovie.objectPosition || '50% 30%';

  const overlayStyle = useMemo(() => {
    const pos = currentMovie.textPosition;
    if (pos && typeof pos === 'object') return pos;
    return { left: '5%', bottom: '8%' }; 
  }, [currentMovie]);

  
  const goToMovie = (id) => {
    if (!id) return;
    navigate(`/movies/${id}`);
  };

  if (total === 0) return null;

  return (
    <div
      className="hero-banner"
      ref={sliderRef}
      tabIndex={0}
      onKeyDown={onKeyDown}
      onMouseEnter={() => setPaused(true)}
      onMouseLeave={() => setPaused(false)}
      onTouchStart={onTouchStart}
      onTouchEnd={onTouchEnd}
      aria-roledescription="carousel"
      aria-label="Featured movies"
    >
      {movies.map((m, i) => {
        const src = m.backdrop || m.poster || m.imageUrl || '';
        const pos = m.objectPosition || objectPosition;
        return (
          <img
            key={m.id ?? i}
            src={src}
            alt={m.title ?? 'Featured'}
            className={`hero-img ${i === current ? 'active' : ''}`}
            style={{ objectPosition: pos }}
            aria-hidden={i !== current}
          />
        );
      })}

      {currentMovie && (
        <div className="hero-overlay" style={overlayStyle}>
          <h1>{currentMovie.title}</h1>
          {currentMovie.overview && <p>{currentMovie.overview}</p>}
          {currentMovie.id && (
            <button
              type="button"
              className="cta-button"
              onClick={() => goToMovie(currentMovie.id)}
              aria-label={`Book tickets for ${currentMovie.title}`}
            >
              Book Your Tickets Now!
            </button>
          )}
        </div>
      )}
      <div className="hero-dots" role="tablist" aria-label="Slides">
        {movies.map((_, i) => (
          <button
            key={`dot-${i}`}
            className="hero-dot"
            aria-label={`Go to slide ${i + 1}`}
            aria-current={i === current ? 'true' : 'false'}
            onClick={() => goTo(i)}
          />
        ))}
      </div>
    </div>
  );
}

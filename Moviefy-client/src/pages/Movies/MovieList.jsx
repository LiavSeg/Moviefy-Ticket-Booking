import React, { useEffect, useState } from 'react';
import axios from 'axios';
import './styles/MovieList.css';
import GlobalHeader from '../Home/HomeHeader';
import SortBarHeader from './SortBarHeader'; // (kept as in your original imports)
import GenreFilterBar from './GenreFilterBar';
import MoviesNavBar from './MoviesNavBar';
import { useNavigate, useSearchParams } from 'react-router-dom';

const tabs = [
  'MOVIES IN THEATERS',
  'COMING SOON',
  'MOVIE GENRES',
  'ALL TIMES FANS FAVORITES',
  'AT HOME'
];

function MoviesList() {
  const [searchParams, setSearchParams] = useSearchParams();

  const tab = searchParams.get("tab");
  const genre = searchParams.get("genre");

  const [movies, setMovies] = useState([]);
  const [selectedTab, setSelectedTab] = useState(() =>
    tab && tabs.includes(tab) ? tab : tabs[0]
  );
  const [selectedGenre, setSelectedGenre] = useState(() =>
    genre ? genre : 'All'
  );

  const [activeIndex, setActiveIndex] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMovies = async () => {
      let url = `http://localhost:8080/movies?tab=${encodeURIComponent(selectedTab)}`;
      if (selectedGenre){
        url += `&genre=${encodeURIComponent(selectedGenre)}`;
      }
      try {
        const response = await axios.get(url);
        setMovies(response.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchMovies();
  }, [selectedTab, selectedGenre]);

  useEffect(() => {
    const currentTab = searchParams.get("tab");
    const currentGenre = searchParams.get("genre");

    if (currentTab !== selectedTab || currentGenre !== selectedGenre) {
      setSearchParams({
        tab: selectedTab,
        genre: selectedGenre,
      });
    }
  }, [selectedTab, selectedGenre]);

  const activeMovie = activeIndex !== null ? movies[activeIndex] : null;

  const handleNext = () => {
    if (activeIndex !== null && movies.length) {
      setActiveIndex((prev) => (prev + 1) % movies.length);
    }
  };

  const handlePrev = () => {
    if (activeIndex !== null && movies.length) {
      setActiveIndex((prev) => (prev - 1 + movies.length) % movies.length);
    }
  };

  return (
    <div className="movie-grid-page">
      <GlobalHeader />
      <MoviesNavBar selected={selectedTab} onSelect={setSelectedTab} />
      <GenreFilterBar selected={selectedGenre} onSelect={setSelectedGenre} />

      <div className="movie-grid-container">
        {movies.map((movie, idx) => (
          <div
            className="movie-card"
            key={idx}
            onClick={() => setActiveIndex(idx)}
            role="button"
            aria-label={`Expand details for ${movie.title}`}
            tabIndex={0}
            onKeyDown={(e) => (e.key === 'Enter' ? setActiveIndex(idx) : null)}
          >
            <div className="poster-wrapper">
              <img
                src={movie.imageUrl}
                alt={movie.title}
                className="movie-card-image"
              />
              <span className="click-hint">Click to expand</span>
            </div>
            <div className="list-card-meta-data">{/* LAST ADDED, REMOVE IF PROBLEMATIC IDK YET*/}
            <h3>{movie.title}</h3>
            <p className="movie-card-meta">
              {movie.genre} • {movie.releaseDate}
            </p>
            </div>
            {/* Card actions: do not trigger popup */}
            <div className="card-actions">
              {/* <button
                className="movie-card-button"
                onClick={(e) => {
                  e.stopPropagation();
                  navigate(`/movies/${movie.id}`);
                }}
                aria-label={`Go to ${movie.title} page`}
              >
                Details
              </button> */}

              {/* <button
                className="movie-card-button book-btn"
                onClick={(e) => {
                  e.stopPropagation();
                  // Adjust route if you have a dedicated booking page:
                  // navigate(`/booking/${movie.id}`);
                  navigate(`/movies/${movie.id}?book=true`);
                }}
                aria-label={`Book tickets for ${movie.title}`}
              >
                Book Tickets
              </button> */}
            </div>
          </div>
        ))}
      </div>

      {activeMovie && (
        <div className="movie-popup-overlay" onClick={() => setActiveIndex(null)}>
          <div className="movie-popup" onClick={(e) => e.stopPropagation()}>
            <img src={activeMovie.imageUrl} alt={activeMovie.title} />
            <h2>{activeMovie.title}</h2>
            <p className="movie-card-meta">
              {activeMovie.genre} • {activeMovie.releaseDate}
            </p>
            <p className="movie-popup-description">{activeMovie.description}</p>

            <div className="popup-actions">
              <button className="popup-btn" onClick={handlePrev}>◀ Prev</button>

              <div className="popup-actions-center">
                <button
                  className="popup-btn primary"
                  onClick={() => {
                    setActiveIndex(null);
                    navigate(`/movies/${activeMovie.id}?book=true`);
                  }}
                >
                  Book Tickets
                </button>
                {/* <button className="popup-btn" onClick={() => setActiveIndex(null)}>
                  Close
                </button> */}
              </div>

              <button className="popup-btn" onClick={handleNext}>Next ▶</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default MoviesList;

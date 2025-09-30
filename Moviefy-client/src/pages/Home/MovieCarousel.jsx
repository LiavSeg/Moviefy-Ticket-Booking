import React, { useEffect, useState, useRef } from 'react';
import axios from 'axios';
import MovieCard from './MovieCard';
import './styles/MovieCarousel.css';
import { FaArrowRight, FaArrowLeft } from "react-icons/fa";

function MovieCarousel({ tab = '', genre = 'All' }) {
  const [movies, setMovies] = useState([]);
  const [canScrollLeft, setCanScrollLeft] = useState(false);
  const [canScrollRight, setCanScrollRight] = useState(true);
  const carouselRef = useRef();
  const scrollByAmount = 400;

  useEffect(() => {
    const fetchMovies = async () => {
      let url = `http://localhost:8080/movies?tab=${encodeURIComponent(tab)}`;
      if (genre)
        url += `&genre=${encodeURIComponent(genre)}`;
      try {
        const response = await axios.get(url);
        setMovies(response.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchMovies();
  }, [tab, genre]);

  const handleScroll = (direction) => {
    const container = carouselRef.current;
    if (!container) return;
    const scrollAmount = direction === 'left' ? -scrollByAmount : scrollByAmount;
    container.scrollBy({ left: scrollAmount, behavior: 'smooth' });
  };

  const updateScrollButtons = () => {
    const container = carouselRef.current;
    if (!container) return;

    setCanScrollLeft(container.scrollLeft > 16);
    const maxScrollLeft = container.scrollWidth - container.clientWidth;
    setCanScrollRight(container.scrollLeft < maxScrollLeft - 5);
  };

  useEffect(() => {
    const container = carouselRef.current;
    if (!container) return;

    updateScrollButtons();
    const onScroll = () => updateScrollButtons();
    container.addEventListener('scroll', onScroll);
    return () => container.removeEventListener('scroll', onScroll);
  }, [movies]);

  return (
    <div className="carousel-container">
      {canScrollLeft && (
        <button className="arrow left" onClick={() => handleScroll('left')}>
          <FaArrowLeft />
        </button>
      )}
      <div className="carousel" ref={carouselRef}>
        {movies.map((movie) => (
          <MovieCard key={movie.id} movie={movie} />
        ))}
      </div>
      {canScrollRight && (
        <button className="arrow right" onClick={() => handleScroll('right')}>
          <FaArrowRight />
        </button>
      )}
    </div>
  );
}

export default MovieCarousel;

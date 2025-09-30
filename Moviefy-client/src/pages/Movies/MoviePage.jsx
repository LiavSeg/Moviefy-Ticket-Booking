// MoviePage.jsx
import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from 'axios';
import './styles/MoviePage.css';
import GlobalHeader from '../Home/HomeHeader';
import BookingForm from '../Bookings/BookingForm';
import TrailerBox from "../../components/trailer/TrailerBox";

import ShowtimeCardWithBooking from '../Bookings/ShowtimePage'; 
import DateSelector from '../../components/showtime/DateSelector'; 
import ShowtimeCarousel from '../../components/showtime/ShowtimeCarousel';
import StarRating from '../../components/reviews/RatingBar'; 
import LoginRequiredModal from '../../modals/LoginRequiredModal'; // adjust path
import { useAuth } from '../../context/AuthContext';
import {
  fetchMovieById,
  fetchReviewsByMovieId,
  fetchShowtimesByMovieId
} from '../../api/movies';
import RatingBar from '../../components/reviews/RatingBar';
export default function MoviePage() {
  const { id } = useParams();
  const { user, loading: authLoading } = useAuth();
  const navigate = useNavigate();
  const [movie, setMovie] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [newReview, setNewReview] = useState({username:'', rating: '', comment: '' });
  const [sortOption, setSortOption] = useState('newest');
  const [showtimes, setShowtimes] = useState([]);
  const [showtimeId, setShowtime] = useState(null);
  const [bookingShowtimeId, setBookingShowtimeId] = useState(null);
  const [showLoginModal, setShowLoginModal] = useState(false); // login-required modal

const [showTrailer, setShowTrailer] = useState(false);

useEffect(() => {
  const loadMovieData = async () => {
    try {
      const movieRes = await fetchMovieById(id);
      console.log(movieRes.data);
      setMovie(movieRes.data);


      const showtimesRes = await fetchShowtimesByMovieId(id);
      setShowtimes(showtimesRes.data);
      const reviewsRes = await fetchReviewsByMovieId(id);
       setReviews(reviewsRes.data);

    } catch (error) {
      console.error(" Error loading data:", error);
    }
  };

  loadMovieData();
}, [id]);



  const handleSubmitReview = async () => {
    try {
      // Safety: if session expired in background, show login modal instead
      if (!user?.userId) {
        setShowModal(false);
        setShowLoginModal(true);
        return;
      }
      const response  = await axios.post(`http://localhost:8080/reviews/movie/${id}`, {
        userId: user.userId,
        rating: newReview.rating,
        comment: newReview.comment,
      });
      
      setShowModal(false);
      setNewReview({ rating: '', comment: '' });
      alert('Thanks! Your review was submitted.');

      const updated = await axios.get(`http://localhost:8080/reviews/by-movie/${id}`);
      setReviews(updated.data);
    } catch (error) {
      const status = error.response?.status;
      if (status === 401) {
        // not authenticated → show login
        setShowModal(false);
        setShowLoginModal(true);
        return;
      }
      if (status === 403) {
        // authenticated but not allowed
        console.error('Forbidden:', error.response?.data?.details || 'Not allowed to review');
        alert('In order to post a review you must have a past booking for this movie!');
        setShowModal(false);
        return;
      }
      if (status === 409) {
        // authenticated but not allowed
        console.error('Forbidden:', error.response?.data?.details || 'Not allowed to review');
        alert('Error submitting review: You already posted a review for this movie');
        setShowModal(false);
        return;
      }

      if (status===400){
        console.error('Forbidden:', error.response?.data?.details || 'Not allowed to review');
      if (!newReview.comment){
        alert('A review must contain at least 5 characters.');
      }
      else if (!newReview.rating){
        alert('A review must contain numeric rating.');
      }
        setShowModal(false);
        return;
      }


      // other errors (400 validation, 500 server)
      console.error('Error submitting review:', error.response?.data?.details || error.message);
    }
  };

  const sortedReviews = [...reviews].sort((a, b) => {
    if (sortOption === 'newest') {
      return new Date(b.created_at) - new Date(a.created_at);
    } else if (sortOption === 'highest') {
      return b.rating - a.rating;
    } else {
      return a.rating - b.rating;
    }
  });

const [selectedDate, setSelectedDate] = useState(null);

const groupShowtimesByDate = (showtimes) => {
  return showtimes.reduce((acc, curr) => {
    const dateKey = new Date(curr.startTime).toISOString().split('T')[0]; // YYYY-MM-DD
    acc[dateKey] = acc[dateKey] || [];
    acc[dateKey].push(curr);
    return acc;
  }, {});
};

const groupedShowtimes = groupShowtimesByDate(showtimes);
const dateKeys = Object.keys(groupedShowtimes).sort();
const handleSelectShowtime = (showtimeId) => {
  navigate(`/showtimes/${showtimeId}`);
};


  const averageRating = reviews.length
    ? (reviews.reduce((acc, r) => acc + r.rating, 0) / reviews.length).toFixed(1)
    : null;

  
  
  if (!movie || authLoading) return <div className="loading">Loading...</div>;
  console.log(movie);


  return (
    <div className="movie-page-container">
      <GlobalHeader />
      <div
        className="movie-backdrop"
        style={{
          backgroundImage: `url(${movie.imageUrl})`,
        }}
      >
        <div className="movie-overlay">
<div className="movie-content">
  {/* 1) Title + Rating (right aligned) */}
  <div className="title-row">
    <h1 className="movie-title">
      {movie.title}{" "}
      <span className="year">
        ({movie.releaseDate ? new Date(movie.releaseDate).getFullYear() : "TBA"})
      </span>
    </h1>
  <div className="movie-meta">
    {movie.genre && <span className="chip">{movie.genre }</span>}
    {movie.duration && <span className="chip">{movie.duration} min</span>}
    {movie.language && <span className="chip">{movie.language}</span>}
  </div>
    <div className="rating-box">
      {averageRating ? (
        (() => {
          const pct = Math.max(0, Math.min(100, Math.round(Number(averageRating) * 10)));
          return (
            <div
              className="rating-circle"
              style={{ "--pct": pct }}
              aria-label={`User score: ${pct}%`}
              title={`User score: ${pct}%`}
            >
              <span>{pct}<small>%</small></span>
            </div>
          );
        })()
      ) : (
        <div className="rating-circle rating-circle--empty" aria-label="No ratings yet">
          <span>NR</span>
        </div>
      )}
      <span className="rating-label">User Score</span>
    </div>
  </div>

  {/* 3) Media row: left Poster, right Trailer */}
  <div className="media-row">
    <div className="poster-col">
      <img src={movie.imageUrl} alt={movie.title} />
    </div>
    <div className="trailer-col">
      <TrailerBox
        trailerUrl={movie.trailerUrl}
        posterUrl={movie.imageUrl}
        title={movie.title}
      />
    </div>
  </div>
{/* 2) Description */}
{movie.description && (
  <div className="movie-description-container">
    <h3 className="movie-section-title">Overview</h3>
    <p className="movie-description">
      <strong>{movie.description.split(' ')[0]}</strong>{" "}
      {movie.description.split(' ').slice(1).join(' ')}
    </p>
  </div>
)}

  {/* 4) Showtime selection */}
  <div className="showtimes-block">
    <DateSelector
      dateKeys={dateKeys}
      selectedDate={selectedDate}
      onSelect={(date) => setSelectedDate(date)}
    />
    
    <ShowtimeCarousel
      selectedDate={selectedDate}
      groupedShowtimes={groupedShowtimes}
      onSelectShowtime={handleSelectShowtime}
    />
    
  </div>

  {/* (optional) Actions — keep or remove as you like */}
  <div className="action-buttons">
    <button className="btn book">Reserve Seats</button>
    <button
      className="btn btn--ghost"
      onClick={() => (!user ? setShowLoginModal(true) : setShowModal(true))}
    >
      Add Review
    </button>
  </div>
</div>

        </div>
      </div>
      {showLoginModal && (
        <LoginRequiredModal onClose={() => setShowLoginModal(false)} msg={'add a review'}  />
      )}
      {/* REVIEW MODAL */}
      {showModal && (
        <div className="modal-overlay fade-in">
          <div className="modal-content">
            <h2>Write a Review</h2>
          <RatingBar
            rating={parseInt(newReview.rating)}
            setRating={(val) => setNewReview({ ...newReview, rating: val })}
          />
            <textarea
              placeholder="Write your review here..."
              value={newReview.comment}
              onChange={(e) => setNewReview({ ...newReview, comment: e.target.value })}
            ></textarea>
            <div className="modal-actions">
              <button className="btn submit" onClick={handleSubmitReview}>Submit</button>
              <button className="btn cancel" onClick={() => setShowModal(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}

      {/* REVIEW SECTION */}
      <div className="review-section">
        <h2>User Reviews</h2>
        <div className="review-sort">
          <label htmlFor="sort">Sort by: </label>
          <select id="sort" value={sortOption} onChange={(e) => setSortOption(e.target.value)}>
            <option value="newest">Newest</option>
            <option value="highest">Highest Rating</option>
            <option value="lowest">Lowest Rating</option>
          </select>
        </div>
        {sortedReviews.length === 0 ? (
          <p className="no-reviews">No reviews yet. Be the first to leave one!</p>
        ) : (
          <ul className="review-list fade-in">
            {sortedReviews.map((review) => (
              <li key={review.reviewId} className="review-item">
                <strong>{review.username || 'Anonymous'}:</strong>
                <span className="rating">{'⭐'.repeat(review.rating)} {review.rating}/10</span>
                <p>{review.comment}</p>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}

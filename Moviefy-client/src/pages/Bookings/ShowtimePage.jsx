import React, { useEffect, useState } from 'react';
import { useParams, useNavigate,useLocation } from 'react-router-dom';
import LoginRequiredModal from '../../modals/LoginRequiredModal';
import { fetchReservedSeats, fetchShowtimeById } from '../../api/showtimes';
import { fetchMovieById } from '../../api/movies';
import BookingForm from './BookingForm';
import SeatSelection from './SeatSelection';
import { useAuth } from '../../context/AuthContext';
import './ShowtimePage.css';

export default function ShowtimePage() {
  //
  const {user} = useAuth();
  const { showtimeId } = useParams();
  // Navigation
  const navigate = useNavigate();
  const location = useLocation();
  const redirectTo = location.state?.from || '/';

  const [showtime, setShowtime] = useState(null);
  const [movie, setMovie] = useState(null);
  const [reservedSeats, setReservedSeats] = useState([]);
  const [selectedSeats, setSelectedSeats] = useState([]);
  const [step, setStep] = useState('select');
  const [showLoginPopup, setShowLoginPopup] = useState(false);


  useEffect(() => {
    const loadData = async () => {
      try {
        const showtimeRes = await fetchShowtimeById(showtimeId);
        setShowtime(showtimeRes);

        const reserved = await fetchReservedSeats(showtimeId);
        setReservedSeats(Array.isArray(reserved) ? reserved.map(String) : []);

        const movieRes = await fetchMovieById(showtimeRes.movieId);
        setMovie(movieRes);
      } catch (err) {
        console.error('Failed to load data:', err);
      }
    };
    loadData();
  }, [showtimeId]);

  const handleContinue = () => {
    if (!user) {
      setShowLoginPopup(true);
      return;
    }
    setStep('confirm');
  };
  const handleBookingSuccess = () => navigate('/');

  if (!showtime || !movie) return <div className="loading">Loading showtime...</div>;

  const genre = movie.data.genre || 'Unknown';
  const duration = movie.data.duration ? `${movie.data.duration} min` : '';
  const language = movie.data.language || '';
  const releaseYear = movie.data.releaseDate
    ? new Date(movie.data.releaseDate).getFullYear()
    : '';

  return (
    <div className="showtime-page-container">
      <div className="movie-info">
      <div 
        className="poster-background"
        style={{ backgroundImage: `url(${movie.data.imageUrl})` }}
      />        
        <div className="movie-meta">
          <h2 className="movie-title">{movie.data.title}</h2>
          {/* <div className="meta-details">
            {genre && <span>{genre}</span>}
            {duration && <span>{duration}</span>}
            {language && <span>{language}</span>}
            {releaseYear && <span>{releaseYear}</span>}
          </div> */}
        </div>
      </div>

      <div className="showtime-details">
        <h3>Showtime Details</h3>
        <p><strong>Theater:</strong> {showtime.theater}</p>
 <p>
  <strong>Time:</strong>{' '}
  {new Date(showtime.startTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })} - 
   { new Date(showtime.endTime).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
</p>
        <p><strong>Price per seat:</strong> {showtime.price} ₪</p>
      </div>

{step === 'select' ? 
 (  !user ? (
  <LoginRequiredModal onClose={() => navigate('/')} msg={'book seats'} />  ) :
        <SeatSelection
          showtimeId={showtimeId}
          selectedSeats={selectedSeats}
          onSelectChange={setSelectedSeats}
          onContinue={handleContinue}
        />
      ) : (
        <BookingForm
          showtimeId={showtimeId}
          seats={selectedSeats}
          showtime={showtime} 
          onSuccess={handleBookingSuccess}
          movie={movie.data}
        />
        
      )}

    </div>
  );
}

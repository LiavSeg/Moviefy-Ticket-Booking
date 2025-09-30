import './styles/MovieCard.css'

import { useNavigate } from 'react-router-dom'; 

function MovieCard({ movie }) {
    const navigate = useNavigate(); 
    const formatDate = (dateStr) => {
    const d = new Date(dateStr);
  return d.toLocaleDateString('en-US', { month: 'short', day: '2-digit', year: 'numeric' });
};
function isUpcoming({ releaseDate }) {
    const release = new Date(movie.releaseDate);
    const today = new Date();
    today.setHours(0,0,0,0);
    release.setHours(0,0,0,0);
  return release > today;
};

let tag = movie.title.toLowerCase().includes("potter")?'Fans Favourite':(isUpcoming(movie.releaseDate)?'Coming Soon':'NEW');
  return (
    <div className="movie-card">
      <span className="tag tag-new">{tag}</span>
      <img src={movie.imageUrl} alt={movie.title} onClick={() => navigate(`/movies/${movie.id}`)}/>
      <h4 className='moive-card-title'>{movie.title}</h4>
      <p className="movie-meta">
        {movie.genre && (
          <span className="chip chip--genre">{movie.genre}</span>
        )}
                {movie.language && (
          <span className="chip chip--duration">{movie.language}</span>
        )}
      </p>
      <button className="details-button" onClick={() => navigate(`/movies/${movie.id}`)}>View Details 
      </button>
    </div>
  );
}

export default MovieCard;
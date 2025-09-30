import { Navigate, useLocation } from 'react-router-dom';
import axios from 'axios';
import { useEffect, useState } from 'react';
import './SearchResults.css';
import GlobalHeader from '../Home/HomeHeader';
import { useNavigate } from 'react-router-dom';

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

export default function SearchResultsPage() {
  const query = useQuery().get('query');
  const [results, setResults] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    if (query) {
      axios
          .get(`http://localhost:8080/search/movies/${encodeURIComponent(query)}`)
          .then((res) => setResults(res.data))
          .catch((err) => console.error(err));
    }
  }, [query]);

  return (
      <>
        <GlobalHeader/>
        <div className="search-results-container">
          <h2 className="search-results-title">
            Results for: <em>{query}</em>
          </h2>

          {results.length === 0 ? (
              <p style={{ textAlign: 'center', color: '#aaa' }}>No results found.</p>
          ) : (
              <div className="search-grid">
                {results.map((movie) => (
                    <div className="search-card" key={movie.id}>
                      <img
                          src={movie.imageUrl || '/placeholder.jpg'}
                          alt={movie.title}
                          onClick={()=>navigate(`/movies/${movie.id}`)}
                      />
                      <div className="search-card-body">
                        <div className="search-card-title">{movie.title}</div>
                        <div className="search-card-genre">{movie.genre}</div>
                        <div className="search-card-date">
                          {new Date(movie.releaseDate).toLocaleDateString('en-US')}
                        </div>
                        <div className="search-card-desc">{movie.description}</div>
                      </div>
                    </div>
                ))}
              </div>
          )}
        </div>
      </>
  );
}

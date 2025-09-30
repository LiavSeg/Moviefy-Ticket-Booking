import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

import './styles/HomeHeader.css';
import { MdMovieCreation, MdOutlineWatchLater } from "react-icons/md";
import { BsTicketDetailed } from "react-icons/bs";
import MoviefyLogo from '../../MoviefyLogo';
import AuthButtons from './AuthButtons';
import { useAuth } from '../../context/AuthContext';
import SearchBar from '../../components/utils/SearchBar';

function GlobalHeader() {
  const { user } = useAuth();
  const [searchTerm, setSearchTerm] = useState('');
  const navigate = useNavigate();

  const handleSearchSubmit = (e) => {
    e.preventDefault();
    if (searchTerm.trim()) {
      navigate(`/search?query=${encodeURIComponent(searchTerm.trim())}`);
    }
  };

  return ( 
    <header className="site-header">
      {/* Top bar */}
      <div className="top-bar">
        {user?.username && (
          <Link to={`/users/profile/${user.userId}`} className="welcome-user">
            Welcome, <strong>{user.username}</strong>
          </Link>
        )}
        <span className="special-offer">
           Don’t miss our special month: <em>HARRY POTTER MONTH</em> – <Link to={`/movies?tab=${encodeURIComponent('ALL TIMES FANS FAVORITES')}&genre=${encodeURIComponent('All')}`}
>Explore Now!</Link>
        </span>
      </div>

      {/* Main nav */}
      <div className="main-navigator">
        {/* Left: Logo */}
        <div className="logo-area">
          <Link to="/">
            <MoviefyLogo />
          </Link>
        </div>

        {/* Center: Search */}
        <div className="nav-center">
          <SearchBar
            value={searchTerm}
            onChange={setSearchTerm}
            onSubmit={handleSearchSubmit}
          />
        </div>

        {/* Right: Nav links + Auth */}
        <div className="nav-actions">
          <nav className="nav-links">
            <Link to={`/movies?tab=${encodeURIComponent('MOVIES IN THEATERS')}&genre=${encodeURIComponent('All')}`}>
              <MdMovieCreation size={24} />
              <span>Movies</span>
            </Link>

            {/* <Link to="/watchlist">
              <MdOutlineWatchLater size={24} />
              <span>Watchlist</span>
            </Link> */}

            <Link to={user?.userId ? `/users/profile/${user.userId}/bookings` : "/sign-in"}>
              <BsTicketDetailed size={22} />
              <span>Bookings</span>
            </Link>
          </nav>
          <AuthButtons />
        </div>
      </div>
    </header>
  );
}

export default GlobalHeader;

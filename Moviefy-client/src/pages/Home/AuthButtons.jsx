import React, { useState, useRef, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { VscAccount } from 'react-icons/vsc';
import { SlUserFollow } from 'react-icons/sl';
import { Link, useNavigate, useLocation } from 'react-router-dom';

function AuthButtons() {
  const { user, logout } = useAuth();
  const [open, setOpen] = useState(false);
  const ref = useRef(null);
  const navigate = useNavigate();
  const location = useLocation();

  // Close on outside click
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (ref.current && !ref.current.contains(e.target)) setOpen(false);
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);
  

  // Close on Escape
  useEffect(() => {
    const onKey = (e) => { if (e.key === 'Escape') setOpen(false); };
    document.addEventListener('keydown', onKey);
    return () => document.removeEventListener('keydown', onKey);
  }, []);

  // Close when route changes
  useEffect(() => { setOpen(false); }, [location.pathname]);

  if (!user) {
    return (
      <div className="auth-buttons">
        
        <Link to="/sign-in" className="auth-link">
          <VscAccount size={20} />
          <span>Sign In</span>
        </Link>
        <Link to="/register" className="auth-link">
          <SlUserFollow size={20} />
          <span>Join</span>
        </Link>
      </div>
    );
  }

  const goProfile = () => navigate(`/users/profile/${user.userId}`);
  const goAdmin = () => navigate('/admin');
  const doLogout = async () => {
    try { await logout(); } finally { navigate('/'); }
  };

  return (
    <div className="auth-buttons" ref={ref}>
      <button
        type="button"
        className="profile-display"
        onClick={() => setOpen((v) => !v)}
        aria-haspopup="menu"
        aria-expanded={open}
      >
        <div className="profile-icon">
          {user.username?.charAt(0).toUpperCase() || 'U'}
        </div>
        <span className="profile-name">{user.username}</span>
        <span className="profile-arrow" aria-hidden>▾</span>
      </button>

      {open && (
        <div className="profile-dropdown" role="menu" onClick={(e) => e.stopPropagation()}>
          <div className="dropdown-arrow" />
          <ul>
            <li>
              <button type="button" role="menuitem" onClick={goProfile}>
                My Profile
              </button>
            </li>
            {user.isAdmin && (
              <li>
                <button type="button" role="menuitem" onClick={goAdmin}>
                  Admin Panel
                </button>
              </li>
            )}
            <li>
              <button type="button" role="menuitem" onClick={doLogout}>
                Logout
              </button>
            </li>
          </ul>
        </div>
      )}
    </div>
  );
}

export default AuthButtons;

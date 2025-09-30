// AdminHeader.jsx
import React, { useState, useRef, useEffect } from 'react';
import './styles/AdminHeader.css';
import { FaUserCircle, FaSignOutAlt, FaCog } from 'react-icons/fa';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import AdminRedirectionModal from '../modals/AdminRedirectionModal'
export default function AdminHeader() {
  const [isDropdownOpen, setIsDropdownOpen] = useState(false);
  const [isSettingsModalOn , setIsSettingsModalOn] = useState(false); 

  const [toast, setToast] = useState({ visible: false, text: '' });
  const timerRef = useRef(null);
  const dropdownRef = useRef();
  const { logout, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    const handleClickOutside = (e) => {
      if (dropdownRef.current && !dropdownRef.current.contains(e.target)) {
        setIsDropdownOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  useEffect(() => {
    return () => { if (timerRef.current) clearTimeout(timerRef.current); };
  }, []);

  const handleLogout = async () => {
    setIsDropdownOpen(false);
    setToast({ visible: true, text: `${user?.username ?? ''} Logging out…` });

    try {
      await logout(); 
      setToast({ visible: true, text: `${user?.username ?? ''} Logged out ` });

      timerRef.current = setTimeout(() => {
        setToast({ visible: false, text: '' });
        navigate('/');
      }, 900);
    } catch (e) {
      setToast({ visible: true, text: 'Logout failed. Try again.' });
      timerRef.current = setTimeout(() => setToast({ visible: false, text: '' }), 1500);
    }
  };


  return (
    <header className="admin-header">
      <div className="admin-header-left">
        <h1>  Moviefy Command Center</h1>
      </div>

      <div className="admin-header-right" ref={dropdownRef}>
        <div className="user-icon-wrapper" onClick={() => setIsDropdownOpen(!isDropdownOpen)}>
          <FaUserCircle size={28} className="user-icon" />
          <span className="admin-name">{user?.username}-Admin</span>
        </div>

        {isDropdownOpen && (
          <div className="admin-dropdown">
            <button onClick={()=>setIsSettingsModalOn(true)}><FaCog /> Settings</button>
            <button onClick={handleLogout}><FaSignOutAlt /> Logout</button>
          </div>
        )}
      </div>
        {isSettingsModalOn && (
              <AdminRedirectionModal onClose={() => setIsSettingsModalOn(false)} />
            )}
      {toast.visible && (
        <div className="logout-toast" role="status" aria-live="polite">
          <FaSignOutAlt /> {toast.text}
        </div>
        
      )}
    </header>
  );
}

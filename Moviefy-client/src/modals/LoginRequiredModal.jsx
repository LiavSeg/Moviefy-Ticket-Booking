import React, { useState } from 'react';
import './styles/LoginRequiredModal.css'; 
import { useLocation, useNavigate } from 'react-router-dom';


export default function LoginRequiredModal({ onClose,msg }) {
    const navigate = useNavigate();
    const location = useLocation();
    const [redirecting,setRedirecting] = useState(false);
    const [redirectMessage, setRedirectMessage] = useState('');

  const handleLoginClick = (button) => {
    const path = button === 'Login' ? '/sign-in' : '/register';
    setRedirectMessage(`🔄 Redirecting to ${button}...`);
    setRedirecting(true);

    setTimeout(() => {
      navigate(path, { state: { from: location.pathname } });
    }, 1500);
  };


  return (
    <div className="login-popup-overlay">
      <div className="login-popup">
        <h3> Login Required</h3>
        {redirecting ? (
          <p>{redirectMessage}</p>
        ) : (
          <>
            <p>You must be logged in to {msg}.</p>
            <div className="popup-buttons">
              <button onClick={() => handleLoginClick('Login')}>Login</button>
              <button onClick={() => handleLoginClick('Sign Up')}>Sign Up</button>
              <button className="cancel-btn" onClick={onClose}>Cancel</button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
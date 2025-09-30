import React, { useState } from 'react';
import './styles/AdminRedirectionModal.css'; 
import { useLocation, useNavigate } from 'react-router-dom';


export default function AdminRedirectionModal({onClose}) {
    const navigate = useNavigate();
    const location = useLocation();
    const [redirecting,setRedirecting] = useState(false);
    const [redirectMessage, setRedirectMessage] = useState('');

  const handleClick = (button) => {
    const path = button === 'Home' ? '/' : '/admin';
    if (path ==='/'){
        setRedirectMessage(`🔄 Redirecting to ${button}...`);
        setRedirecting(true);
        setTimeout(() => {
        navigate(path, { state: { from: location.pathname } });
        }, 1500);
    }
    else
        onClose();
  };


  return (
    <div className="admin-popup-overlay">
      <div className="redirect-popup">
        <h3>Stay here or go to homepage?</h3>
        {redirecting ? (
          <p>{redirectMessage}</p>
        ) : (
          <>
            <div className="popup-buttons">
              <button onClick={() => handleClick('Home')}>Go to homepage</button>
              <button onClick={() => handleClick('Stay')}>Stay here</button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
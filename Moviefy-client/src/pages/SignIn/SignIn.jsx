import React, { useState,useEffect } from 'react';
import GlobalHeader from '../Home/HomeHeader';
import './styles/SignIn.css';
import { useAuth } from '../../context/AuthContext';
import { useLocation, useNavigate } from 'react-router-dom';
import { FaEye, FaEyeSlash } from "react-icons/fa";
export default function SignIn() {
  const { user, loading, login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const redirectTo = location.state?.from || '/';

  const [formData, setFormData] = useState({ username: '', password: '' });
  const [message, setMessage] = useState('');
  const [showAdminChoice, setShowAdminChoice] = useState(false);
  const [showPassword, setShowPassword] = useState(false);



  
  const handleOnChange = (e) => {
    setFormData((prev) => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const resetForm = (type) => {
    if (type === 'error') {
      setFormData((prev) => ({ ...prev, password: '' }));
    } else {
      setFormData({ username: '', password: '' });
    }
  };
  

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const data = await login(formData); 
      const adminFlag = data?.isAdmin ?? data?.admin ?? false;

      localStorage.setItem('userId', data.userId);
      setMessage(`Login successful. Welcome, ${data.username}`);
      resetForm();

      if (adminFlag) {
        setShowAdminChoice(true);
      } else {
        navigate(redirectTo);
      }
    } catch (error) {
      console.error(error);
      resetForm('error');
      setMessage(
        'Login failed: ' +
          (error?.response?.data?.details || error?.message || 'Unknown error')
      );
    }
  };

  useEffect(() => {
    if (!loading && user) {
      const dest = user.isAdmin ? '/admin/dashboard' : redirectTo;
      navigate(dest, { replace: true });
    }
  }, [user, loading, redirectTo, navigate]);
  
  return (
    <div className="sign-in-page">
      <GlobalHeader />
      <div className="sign-in-box">
        <h1 className="sign-in-title">Welcome Back to Moviefy</h1>
        <p className="sign-in-subtitle">Sign in to book your next cinematic experience</p>

        <form className="sign-in-form" onSubmit={handleSubmit}>
          <input
            type="text"
            name="username"
            placeholder="Username*"
            value={formData.username}
            onChange={handleOnChange}
            required
            autoComplete="username"
          />
          <input
           type={showPassword ? "text" : "password"}
            name="password"
            placeholder="Password*"
            value={formData.password}
            onChange={handleOnChange}
            required
            autoComplete="current-password"
          />
                <span
              className="toggle-password-icon"
              onClick={() => setShowPassword((prev) => !prev)}
            >
              {showPassword ?<FaEye/> :<FaEyeSlash/>}
            </span>
          <button type="submit">Sign In</button>
          <p className="sign-in-hint">
            Don&apos;t have an account? <a href="/register">Register here</a>
          </p>
          {message && <p className="sign-in-message">{message}</p>}
        </form>
      </div>

      {showAdminChoice && (
        <div className="admin-choice-popup">
          <div className="admin-choice-box">
            <h3>Welcome, Admin</h3>
            <p>Where would you like to go?</p>
            <div className="admin-choice-buttons">
              <button onClick={() => navigate('/admin/dashboard')}>Go to Admin Panel</button>
              <button onClick={() => navigate('/')}>Continue to Site</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

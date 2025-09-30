import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import GlobalHeader from '../Home/HomeHeader';
import { useAuth } from '../../context/AuthContext';
import { userRegister } from '../../api/users';

import axios from 'axios'
/**
 * Register.jsx
 *
 * This component renders the registration form for new users.
 *
 * Functionality:
 * - Captures username, email, and password.
 * - Sends a POST request to /users/register to create a new account.
 * - Immediately logs the user in by calling login() from AuthContext.
 * - Redirects to home page after successful registration.
 *
 * Notes:
 * - Registration and login are decoupled; session is created only via login().
 * - Local user state and localStorage are updated via AuthContext.
 */
function Register() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [formData,setFormData] = useState({
      username: '',
      email: '',
      password: ''
  });

const handleOnChange = (e)=>{
  setFormData((prev)=>({
    ...prev,[e.target.name]:e.target.value
  }));
};

  const resetForm = (type) => {  
    if (type === 'error')
      setFormData((prev) => ({
      ...prev,
      password: ''
    }));
    else
        setFormData({
          username: '',
          email: '',
          password: ''
    });
  };


const handleSubmit = async (e) => {
  e.preventDefault();
  try {
    const user = await userRegister(formData);
    await login({ username: formData.username, password: formData.password });
    setMessage('Registration successful, Welcome: ' + user.username);
    resetForm();
    navigate(`/`);
  } catch (error) {
    console.error(error);
    resetForm('error');
    setMessage('Registration failed: ' + (error.response?.data?.details || error.message || 'Unknown error'));
  }
};


  const [message,setMessage] = useState('');
  return (
    <div className="sign-in-page">
      <GlobalHeader />

      <div className="sign-in-box">
        <h1 className="sign-in-title">Join now for free!</h1>
        <p className="sign-in-subtitle">Sign up for your next cinematic experience</p>

        <form className="sign-in-form" onSubmit={handleSubmit}>
          <input type="text" name='username' placeholder="Username*" value={formData.username} onChange={handleOnChange} required  />
          <input type="email" name="email" placeholder="Email address*" value={formData.email}  onChange={handleOnChange} required />
          <input type="password" name="password" placeholder="Password*" value={formData.password} onChange={handleOnChange} required/>
          <button type="submit" disabled={isSubmitting}>
            {isSubmitting ? 'Signing Up...' : 'Sign Up'}
          </button>          <p className="sign-in-hint">
            Already have an account? <a href="/sign-in">Sign in here</a>
          </p>
            {message && <p className="sign-in-message">{message}</p>}
        </form>
      </div>
    </div>
  );
}
export default Register;

// src/context/AuthContext.jsx
import React, { createContext, useContext, useEffect, useState } from 'react';
import {
  userLogin as apiLogin,
  userLogout as apiLogout,
  getCurrentUser,
} from '../api/users';

const AuthContext = createContext();

/**
 * AuthProvider
 * - Holds the authenticated user in React state.
 * - Hydrates on app load by calling GET /users/me to check if a session exists.
 * - login(): POST /users/login, then save user in state.
 * - logout(): POST /users/logout, then clear state.
 *
 * NOTE:
 * - Session is server-driven (JSESSIONID cookie).
 * - We optionally mirror the user object in localStorage so UI can render
 *   immediately after reload; server is still the source of truth on secure calls.
 */
export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);     // current logged-in user DTO
  const [loading, setLoading] = useState(true); // initial hydration state

useEffect(() => {
  const hydrate = async () => {
    try {
      const me = await getCurrentUser(); // validate JSESSIONID session
      setUser(me);
      localStorage.setItem('user', JSON.stringify(me)); // optional for UI preload
    } catch {
      setUser(null);
      localStorage.removeItem('user');
    } finally {
      setLoading(false);
    }
  };

  hydrate();
}, []);

  const login = async (credentials) => {
    const data = await apiLogin(credentials); // creates session on server
    setUser(data);
    
    localStorage.setItem('user', JSON.stringify(data)); // optional mirror for UX
    return data;
  };

  const logout = async () => {
    await apiLogout();      // invalidates server session + cookie
    setUser(null);          // clear client state
    localStorage.removeItem('user'); // optional mirror clear
  };

  return (
    <AuthContext.Provider value={{ user, loading, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

/** useAuth: returns { user, loading, login, logout } */
export const useAuth = () => useContext(AuthContext);

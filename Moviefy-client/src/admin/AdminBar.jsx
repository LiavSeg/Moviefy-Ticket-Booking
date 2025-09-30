// AdminBar.jsx
import React from 'react';
import { FaChartBar, FaFilm, FaClock, FaUsers } from 'react-icons/fa';
import { NavLink } from 'react-router-dom';
import './styles/AdminBar.css';
import MoviefyLogo from '../MoviefyLogo';

export default function AdminBar() {
  return (
    <aside className="admin-sidebar">
      <div className="admin-sidebar-logo">
        <MoviefyLogo />
        <span>Moviefy Admin</span>
      </div>

      <nav className="admin-sidebar-nav">
        <NavLink to="/admin" end className={({ isActive }) => `admin-nav-link ${isActive ? 'admin-active' : ''}`}>
          <FaChartBar /> <span>Dashboard</span>
        </NavLink>

        <NavLink to="/admin/movies" className={({ isActive }) => `admin-nav-link ${isActive ? 'admin-active' : ''}`}>
          <FaFilm /> <span>Movies</span>
        </NavLink>

        <NavLink to="/admin/showtimes/all" className={({ isActive }) => `admin-nav-link ${isActive ? 'admin-active' : ''}`}>
          <FaClock /> <span>Showtimes</span>
        </NavLink>

        <NavLink to="/admin/users" className={({ isActive }) => `admin-nav-link ${isActive ? 'admin-active' : ''}`}>
          <FaUsers /> <span>Users</span>
        </NavLink>
        
        <NavLink to="/admin/import" className={({ isActive }) => `admin-nav-link ${isActive ? 'admin-active' : ''}`}>
          <FaUsers /> <span>Import Data</span>
        </NavLink>
      </nav>
    </aside>
  );
}

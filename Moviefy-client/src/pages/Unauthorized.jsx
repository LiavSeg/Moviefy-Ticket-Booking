import React from 'react';
import { useLocation, Link } from 'react-router-dom';
import './Unauthorized.css';

export default function Unauthorized() {
  const location = useLocation();
  const from = location.state?.from || '/';

  return (
    <section className="unauth-wrapper">
      <div className="unauth-card">
        <h1>403 — Unauthorized</h1>
        <p>You do not have permission to access this page.</p>
        <div className="unauth-actions">
          <Link to="/" className="btn">Go to Home</Link>
          <Link to={from} className="btn btn-secondary">Go Back</Link>
          <Link to="/sign-in" className="btn btn-outline">Sign in as Admin</Link>
        </div>
      </div>
    </section>
  );
}
 
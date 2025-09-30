import React from 'react';
import { Navigate, Outlet, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

/**
 * Protects /admin/** routes.
 * - If not authenticated: redirect to /signin with return path.
 * - If authenticated but not admin: redirect to /unauthorized with source path.
 */
export default function AdminRoute() {
  const { user } = useAuth();
  const location = useLocation();

  // Not logged in
  if (!user) {
    return <Navigate to="/sign-in" replace state={{ from: location.pathname }} />;
  }
  // Logged in but not admin
  if (!user.isAdmin) {
    return <Navigate to="/unauthorized" replace state={{ from: location.pathname }} />;
  }
  // Authorized
  return <Outlet />;
}

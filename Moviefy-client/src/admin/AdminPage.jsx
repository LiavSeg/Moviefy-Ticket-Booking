// src/pages/admin/AdminPage.jsx
import React from 'react';
import { Outlet } from 'react-router-dom';
import './styles/AdminPage.css';
import AdminSidebar from './AdminBar';
import AdminHeader from './AdminHeader';

export default function AdminPage() {
  return (
    <div className="admin-layout">
      <AdminSidebar />
      <div className="admin-main">
        <AdminHeader />
        <div className="admin-content">
          <Outlet />
        </div>
      </div>
    </div>
  );
}

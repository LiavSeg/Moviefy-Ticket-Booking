/**
 * UserProfile.jsx
 *
 * Renders the authenticated user's profile page.
 * Features:
 * - Displays basic profile information
 * - Fetches and displays user's booking history
 * - Handles logout and protected route redirection
 *
 * Tech Stack:
 * - React with useEffect/useState
 * - AuthContext for session management
 * - Axios API wrappers for bookings & profile
 */

import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import { useNavigate,useParams } from 'react-router-dom';
import GlobalHeader from '../Home/HomeHeader';
import UserBookingsList from '../../components/booking/UserBookingsList';
import UserReviewList from '../../components/reviews/UserReviewList';
import {
  getUserProfile,
  updateUserProfile,
  changeUserPassword,
} from '../../api/users';
import { getUserBookings } from '../../api/bookings';

import './styles/UserProfile.css';

function UserProfile() {
  const { user,loading, logout } = useAuth();
  const navigate = useNavigate();
  const { section } = useParams(); 
  const [profile, setProfile] = useState(null);
  const [loadingProfile, setLoadingProfile] = useState(true);

  const [selectedSection, setSelectedSection] = useState(section || 'profile');

  const [bookings, setBookings] = useState([]);
  const [expandedBookingId, setExpandedBookingId] = useState(null);

  const [expandedReviewId, setExpandedReviewId] = useState(null);

  // Edit username/email
  const [isEditing, setIsEditing] = useState(false);
  const [editForm, setEditForm] = useState({ userName: '', email: '' });
  const [editSaving, setEditSaving] = useState(false);
  const [editError, setEditError] = useState('');

  // Change password
  const [showPwdForm, setShowPwdForm] = useState(false);
  const [pwdForm, setPwdForm] = useState({ currentPassword: '', newPassword: '' });
  const [pwdSaving, setPwdSaving] = useState(false);
  const [pwdError, setPwdError] = useState('');
  const [pwdMsg, setPwdMsg] = useState('');

  
  useEffect(() => {
    let us =localStorage.getItem('user');
    if (us)
      us = JSON.parse(us);
    if (loading) return;                 
    if (!user || !user.userId || us && us.userId!==user.userId){
      navigate('/', { replace: true });
    }
  }, [loading, user, navigate]);
 useEffect(() => {
  if (section && section !== selectedSection) {     setSelectedSection(section);  } }, [section]); 

  // Fetch profile
  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const data = await getUserProfile(user.userId);
        setProfile(data);
        setEditForm({
          userName: data.userName || '',
          email: data.email || '',
        });
      } catch (err) {
        console.error('Failed to fetch profile', err);
      } finally {
        setLoadingProfile(false);
      }
    };

    if (user?.userId) fetchProfile();
  }, [user?.userId]);

  // Fetch bookings
  useEffect(() => {
    const fetchBookings = async () => {
      try {
        const data = await getUserBookings(user.userId);
        setBookings(data.data);
      } catch (err) {
        console.error('Failed to fetch bookings', err);
      }
    };

    if (selectedSection === 'bookings' && user?.userId) {
      fetchBookings();
    }
  }, [selectedSection, user?.userId]);

  // Handlers
  const onEditChange = (e) => {
    const { name, value } = e.target;
    setEditForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSaveProfile = async () => {
    setEditError('');
    if (!editForm.userName.trim() || !editForm.email.trim()) {
      setEditError('Username and Email are required.');
      return;
    }
    setEditSaving(true);
    try {
      const updated = await updateUserProfile(user.userId, {
        userName: editForm.userName.trim(),
        email: editForm.email.trim(),
      });
      setProfile((p) => ({ ...p, ...updated }));
      setIsEditing(false);
    } catch (err) {
      setEditError(
        err?.response?.data?.details ||
          err?.response?.data?.message ||
          'Failed to update profile.'
      );
    } finally {
      setEditSaving(false);
    }
  };

  const handleCancelEdit = () => {
    setIsEditing(false);
    setEditError('');
    setEditForm({
      userName: profile.userName || '',
      email: profile.email || '',
    });
  };

  const handleChangePassword = async (e) => {
    e.preventDefault();
    setPwdError('');
    setPwdMsg('');
    if (!pwdForm.currentPassword || !pwdForm.newPassword) {
      setPwdError('Both fields are required.');
      return;
    }
    setPwdSaving(true);
    try {
      const response =await changeUserPassword(user.userId, {
        currentPassword: pwdForm.currentPassword,
        newPassword: pwdForm.newPassword,
      });
      setPwdMsg('Password updated successfully.');
      setPwdForm({ currentPassword: '', newPassword: '' });
    } catch (err) {
      setPwdError(
        err?.response?.data?.details ||
          err?.response?.data?.message ||
          'Failed to change password.'
      );
    } finally {
      setPwdSaving(false);
    }
  };

  if (loadingProfile) {
    return (
      <div>
        <GlobalHeader />
        <p className="loading">Loading profile...</p>
      </div>
    );
  }

  if (!profile) {
    return (
      <div>
        <GlobalHeader />
        <p className="redirect">Redirecting to home page...</p>
      </div>
    );
  }

  return (
    <div>
      <GlobalHeader />
      <div className="profile-layout">
        {/* Sidebar */}
        <aside className="profile-sidebar">
          <button
            className={selectedSection === 'profile' ? 'active' : ''}
            onClick={() => { setSelectedSection('profile'); navigate(`/users/profile/${user?.userId}`); }}
          >
            • Profile
          </button>
          <button
            className={selectedSection === 'bookings' ? 'active' : ''}
            onClick={() => { setSelectedSection('bookings'); navigate(`/users/profile/${user?.userId}/bookings`); }}
          >
            • Bookings
          </button>
          <button
            className={selectedSection === 'reviews' ? 'active' : ''}
            onClick={() => { setSelectedSection('reviews'); navigate(`/users/profile/${user?.userId}/reviews`); }}
          >
            • Reviews
          </button>
          <button
            onClick={() => {
              logout();
              setProfile(null);
              navigate('/');
            }}
          >
            • Logout
          </button>
        </aside>

        {/* Main */}
        <div className="profile-main">
          {selectedSection === 'profile' && (
            <section className="user-info">
              <h2>Welcome, {profile.userName}</h2>

        {isEditing ? (
         <div className="edit-form">
          <div className="field">
            <label htmlFor="userName">Username</label>
            <input
              id="userName"
              name="userName"
              value={editForm.userName}
              onChange={onEditChange}
              autoComplete="username"
              required
            />
          </div>

          <div className="field">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              value={editForm.email}
              onChange={onEditChange}
              autoComplete="email"
              required
            />
          </div>

          {editError && <p className="form-error grid-span">{editError}</p>}

          <div className="edit-actions grid-span">
            <button
              type="button"
              className="btn primary"
              onClick={handleSaveProfile}
              disabled={editSaving}
            >
              {editSaving ? 'Saving…' : 'Save'}
            </button>
            <button type="button" className="btn" onClick={handleCancelEdit}>
              Cancel
            </button>
          </div>
        </div>
      ) : (
        <>
          <p>Username: {profile.userName}</p>
          <p>Email: {profile.email}</p>

          <div style={{ display: 'flex', gap: 8, marginTop: 8 }}>
            <button type="button" onClick={() => setIsEditing(true)}>
              Edit Profile
            </button>
            <button type="button" onClick={() => setShowPwdForm(v => !v)}>
              {showPwdForm ? 'Close Password' : 'Change Password'}
            </button>
          </div>
        </>
      )}


      {showPwdForm && (
        <form onSubmit={handleChangePassword} className="pwd-form">
          <h3>Change Password</h3>

          <div className="field">
            <label htmlFor="currentPassword">Current Password</label>
            <input
              id="currentPassword"
              type="password"
              value={pwdForm.currentPassword}
              onChange={(e) => setPwdForm({ ...pwdForm, currentPassword: e.target.value })}
              autoComplete="current-password"
              required
            />
          </div>

          <div className="field">
            <label htmlFor="newPassword">New Password</label>
            <input
              id="newPassword"
              type="password"
              value={pwdForm.newPassword}
              onChange={(e) => setPwdForm({ ...pwdForm, newPassword: e.target.value })}
              autoComplete="new-password"
              required
            />
          </div>

          {pwdError && <p className="form-error grid-span">{pwdError}</p>}
          {pwdMsg && <p className="form-success grid-span">{pwdMsg}</p>}

          <button className="btn primary grid-span" type="submit" disabled={pwdSaving}>
            {pwdSaving ? 'Updating...' : 'Update Password'}
          </button>
        </form>
      )}
    </section>
  )}

          {selectedSection === 'bookings' && (
            <UserBookingsList
              bookings={bookings}
              expandedBookingId={expandedBookingId}
              setExpandedBookingId={setExpandedBookingId}
            />
          )}

          {selectedSection === 'reviews' && (
            <UserReviewList
              reviews={profile.reviewsList}
              expandedReviewId={expandedReviewId}
              setExpandedReviewId={setExpandedReviewId}
            />
          )}
        </div>
      </div>
    </div>
  );
}

export default UserProfile;
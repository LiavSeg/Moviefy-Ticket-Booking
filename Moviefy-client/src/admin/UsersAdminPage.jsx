import React, { useEffect, useState } from 'react';
import { getAllUsers, changeAdminPerms } from '../api/admin';
import './ShowtimesAdminPage.css'; 
import { useAuth } from '../context/AuthContext';

export default function UsersAdminPage() {
  const [users, setUsers] = useState([]);
  const { user} = useAuth();

  useEffect(() => {
    const fetch = async () => {
      const res = await getAllUsers();
      setUsers(res.data);
    };
    fetch();
  }, []);

  const handleToggleAdmin = async (userId, currentValue) => {
    try {
      if (userId===user.userId){
        alert('You cannot change your own permissions');
        return;
      }
      await changeAdminPerms(userId, !currentValue);
      setUsers((prev) =>
        prev.map((u) =>
          u.userId === userId ? { ...u, admin: !currentValue } : u
        )
      );
    } catch (err) {
      console.error('Failed to update admin flag', err);
      alert('Update failed');
    }
  };

  return (
    <section className="admin-section">
      <h2>Users Management</h2>

      <table className="admin-table">
        <thead>
          <tr>
            <th>Username</th>
            <th>Email</th>
            <th>Admin</th>
            <th>Toggle Admin</th> 
          </tr>
        </thead>
        <tbody>
          {users.map((u) => (
            <tr key={u.userId}>
              <td>{u.userName}</td>
              <td>{u.email}</td>
              <td>{u.admin ? 'Yes' : 'No'}</td>
              <td className="admin-actions">
                <button
                  onClick={() => handleToggleAdmin(u.userId, u.admin)}
                  className={u.admin ? 'toggle-btn on' : 'toggle-btn off'}
                  title={
                    u.userId === user.userId
                      ? "You cannot change your own permissions"
                      : u.admin
                      ? "Revoke admin"
                      : "Make admin"
                  }
                >
                  {u.admin ? 'Revoke Admin' : 'Make Admin'}
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </section>
  );
}

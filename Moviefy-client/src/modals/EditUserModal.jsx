import React, { useEffect, useMemo, useRef, useState } from "react";
import axios from "axios";
import "./styles/EditUserModal.css";

export default function EditUserModal({ isOpen, onClose, customerId, currentUser }) {
  const [form, setForm] = useState({ username: "", email: "", isAdmin: false });
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const isAdmin = !!currentUser?.isAdmin;

  const modalRef = useRef(null);
  const firstFieldRef = useRef(null);

  if (!isOpen || !isAdmin) return null;

  useEffect(() => {
    if (!customerId) return;
    setLoading(true);
    setError("");
    axios
      .get(`/admin/users/${customerId}`)
      .then((res) => {
        const { username, email, isAdmin } = res.data || {};
        setForm({
          username: (username || "").trim(),
          email: (email || "").trim(),
          isAdmin: !!isAdmin
        });
      })
      .catch(() => setError("Failed to load customer details"))
      .finally(() => setLoading(false));
  }, [customerId]);

  useEffect(() => {
    const onKeyDown = (e) => {
      if (e.key === "Escape") onClose();
      // Trap Tab focus within modal
      if (e.key === "Tab" && modalRef.current) {
        const focusable = modalRef.current.querySelectorAll(
          'button, [href], input, select, textarea, [tabindex]:not([tabindex="-1"])'
        );
        const list = Array.from(focusable);
        if (!list.length) return;
        const first = list[0];
        const last = list[list.length - 1];
        if (e.shiftKey && document.activeElement === first) {
          last.focus();
          e.preventDefault();
        } else if (!e.shiftKey && document.activeElement === last) {
          first.focus();
          e.preventDefault();
        }
      }
    };
    document.addEventListener("keydown", onKeyDown);
    return () => document.removeEventListener("keydown", onKeyDown);
  }, [onClose]);

  // Autofocus first field once loaded
  useEffect(() => {
    if (!loading && isOpen && firstFieldRef.current) firstFieldRef.current.focus();
  }, [loading, isOpen]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setForm((prev) => ({ ...prev, [name]: type === "checkbox" ? checked : value }));
  };

  const emailValid = useMemo(() => {
    if (!form.email) return false;
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email.trim());
  }, [form.email]);

  const usernameValid = useMemo(() => form.username.trim().length >= 2, [form.username]);

  const canSubmit = usernameValid && emailValid && !saving && !loading;

  const handleSave = async (e) => {
    e.preventDefault();
    if (!canSubmit) return;

    setSaving(true);
    setError("");
    try {
      await axios.put(`/admin/users/${customerId}`, {
        username: form.username?.trim(),
        email: form.email?.trim(),
        isAdmin: form.isAdmin
      });
      onClose();
    } catch (err) {
      setError(err?.response?.data || "Update failed. Please verify input and try again.");
    } finally {
      setSaving(false);
    }
  };

  const isSelfEdit = currentUser?.userId === customerId;

  const onBackdropClick = (e) => {
    if (e.target === e.currentTarget) onClose();
  };

  return (
    <div
      className="admin-edit-customer-backdrop"
      role="dialog"
      aria-modal="true"
      aria-labelledby="edit-customer-title"
      aria-describedby="edit-customer-desc"
      onClick={onBackdropClick}
    >
      <div className="admin-edit-customer-modal" ref={modalRef}>
        <header className="modal-header">
          <h3 id="edit-customer-title">Edit Customer</h3>
          <button className="icon-btn" onClick={onClose} aria-label="Close">×</button>
        </header>

        <main className="modal-body">
          <p id="edit-customer-desc" style={{ display: "none" }}>
            Edit username, email, and admin privileges for the selected customer.
          </p>

          {loading ? (
            <p>Loading…</p>
          ) : (
            <form onSubmit={handleSave} className="admin-edit-form" noValidate>
              <label>
                Username
                <input
                  type="text"
                  name="username"
                  value={form.username}
                  onChange={handleChange}
                  required
                  minLength={2}
                  ref={firstFieldRef}
                  aria-invalid={!usernameValid}
                />
              </label>

              <label>
                Email
                <input
                  type="email"
                  name="email"
                  value={form.email}
                  onChange={handleChange}
                  required
                  inputMode="email"
                  aria-invalid={!emailValid}
                />
              </label>

              <label className="checkbox-line">
                <input
                  type="checkbox"
                  name="isAdmin"
                  checked={form.isAdmin}
                  onChange={handleChange}
                  disabled={isSelfEdit}
                />
                Admin privileges
              </label>

              {!usernameValid && (
                <div className="form-error" role="alert">Username must be at least 2 characters.</div>
              )}
              {!emailValid && (
                <div className="form-error" role="alert">Please enter a valid email address.</div>
              )}
              {error && <div className="form-error" role="alert">{error}</div>}

              <div className="modal-actions">
                <button
                  type="button"
                  className="btn secondary"
                  onClick={onClose}
                  disabled={saving}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="btn primary"
                  disabled={!canSubmit}
                >
                  {saving ? "Saving…" : "Save"}
                </button>
              </div>
            </form>
          )}
        </main>
      </div>
    </div>
  );
}

import React, { useEffect, useState } from 'react';
import { updateShowtime } from '../api/admin';

export default function EditShowtimeModal({ open, showtime, onClose, onSaved }) {
  const [form, setForm] = useState({
    theater: '',
    startTime: '',
    endTime: '',
    price: ''
  });
  const [saving, setSaving] = useState(false);

  const toLocalInput = (iso) => {
    if (!iso) return '';
    return String(iso).substring(0, 16); // "YYYY-MM-DDTHH:mm"
  };

  const toServerIso = (dtLocal) => {
    if (!dtLocal) return null;
    return dtLocal.length === 16 ? `${dtLocal}:00` : dtLocal;
  };

  useEffect(() => {
    if (!open || !showtime) return;
    setForm({
      theater: showtime.theater ?? '',
      startTime: toLocalInput(showtime.startTime),
      endTime: toLocalInput(showtime.endTime),
      price: showtime.price ?? ''
    });
    setSaving(false);
  }, [open, showtime]);

  const onChange = (e) => {
    const { name, value } = e.target;
    setForm((p) => ({ ...p, [name]: value }));
  };

  const validate = () => {
    if (!form.theater.trim()) return 'Theater is required.';
    if (!form.startTime || !form.endTime) return 'Start and End times are required.';
    const start = new Date(form.startTime);
    const end = new Date(form.endTime);
    if (isNaN(start) || isNaN(end)) return 'Invalid date/time.';
    if (end <= start) return 'End time must be after start time.';
    const priceNum = Number(form.price);
    if (!Number.isFinite(priceNum) || priceNum < 0) return 'Price must be a non-negative number.';
    return null;
  };

  const onSave = async () => {
    const err = validate();
    if (err) {
      alert(err);
      return;
    }
    setSaving(true);
    try {
      const payload = {
        theater: form.theater.trim(),
        startTime: toServerIso(form.startTime),
        endTime: toServerIso(form.endTime),
        price: Number(form.price)
      };

      await updateShowtime(showtime.id, payload);

      onSaved?.(payload);
      onClose?.();
      alert('Showtime updated successfully.');
    } catch (e) {
      const status = e?.response?.status;
      const msg =
        e?.response?.data?.message ||
        e?.response?.data?.error ||
        e?.message ||
        'Update failed';
      if (status === 401 || status === 403) {
        alert('Your admin session has expired. Please log in again.');
      } else if (status === 404) {
        alert('Showtime not found.');
      } else {
        alert(`Could not update showtime: ${msg}`);
      }
      setSaving(false);
    }
  };

  if (!open || !showtime) return null;

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal-content" onClick={(e) => e.stopPropagation()}>
        <h3>Edit Showtime (ID: {showtime.id})</h3>

        <div className="form-row">
          <label>Theater</label>
          <input
            type="text"
            name="theater"
            value={form.theater}
            onChange={onChange}
            placeholder="Auditorium name/number"
          />
        </div>

        <div className="form-row">
          <label>Start Time</label>
          <input
            type="datetime-local"
            name="startTime"
            value={form.startTime}
            onChange={onChange}
          />
        </div>

        <div className="form-row">
          <label>End Time</label>
          <input
            type="datetime-local"
            name="endTime"
            value={form.endTime}
            onChange={onChange}
          />
        </div>

        <div className="form-row">
          <label>Price</label>
          <input
            type="number"
            name="price"
            step="0.01"
            min="0"
            value={form.price}
            onChange={onChange}
          />
        </div>

        <div className="modal-actions">
          <button className="secondary" onClick={onClose} disabled={saving}>
            Cancel
          </button>
          <button className="primary" onClick={onSave} disabled={saving}>
            {saving ? 'Saving…' : 'Save Changes'}
          </button>
        </div>
      </div>
    </div>
  );
}

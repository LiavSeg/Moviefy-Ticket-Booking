// src/pages/ContactUs.jsx
import React, { useState } from 'react';
import './styles/ContactUs.css';
import { FiMail, FiPhone, FiMapPin, FiSend } from 'react-icons/fi';
import GlobalHeader from '../Home/HomeHeader';

export default function ContactUs({ onSubmit }) {
  const [form, setForm] = useState({ name: '', email: '', message: '' });
  const [errors, setErrors] = useState({});
  const [status, setStatus] = useState({ type: '', text: '' });
  const [submitting, setSubmitting] = useState(false);

  const validate = () => {
    const e = {};
    if (!form.name.trim()) e.name = 'Name is required.';
    if (!form.email.trim()) {
      e.email = 'Email is required.';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
      e.email = 'Please enter a valid email.';
    }
    if (!form.message.trim()) e.message = 'Tell us what’s up.';
    setErrors(e);
    return Object.keys(e).length === 0;
  };

  const handleChange = (ev) => {
    setForm((prev) => ({ ...prev, [ev.target.name]: ev.target.value }));
  };

  const handleSubmit = async (ev) => {
    ev.preventDefault();
    if (!validate()) return;

    setSubmitting(true);
    setStatus({ type: '', text: '' });

    try {
      // No backend contract in the official doc — keep it frontend-only by default.
      // If a parent passes onSubmit(form), we call it. Otherwise, fallback to mailto.
      if (typeof onSubmit === 'function') {
        await onSubmit(form);
      } else {
        const subject = encodeURIComponent('Moviefy Contact');
        const body = encodeURIComponent(
          `Name: ${form.name}\nEmail: ${form.email}\n\nMessage:\n${form.message}`
        );
        window.location.href = `mailto:moviefyservice@gmail.com?subject=${subject}&body=${body}`;
      }

      setStatus({
        type: 'success',
        text: "Message sent (or your mail app just opened). We'll get back faster than a jump scare."
      });
      setForm({ name: '', email: '', message: '' });
    } catch (err) {
      setStatus({
        type: 'error',
        text: 'Something went wrong. Try again in a minute.'
      });
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <>
    <GlobalHeader/>
    <main className="contact-page" aria-labelledby="contact-title">
      <section className="contact-hero">
        <h1 id="contact-title">Let’s Talk Movies (and Other Serious Business)</h1>
        <p className="hero-tagline">
          Got a question? Lost your popcorn? Planning a 200‑seat cat birthday party?
          We’re here for it.
        </p>
      </section>

      <section className="contact-grid">
        <div className="contact-card">
          <div className="contact-meta">
            <div className="meta-item">
              <FiMail aria-hidden="true" />
              <div>
                <h3>Email</h3>
                <a href="mailto:moviefyservice@gmail.com" className="meta-link">
                  MoviefyService@gmail.com
                </a>
              </div>
            </div>

            <div className="meta-item">
              <FiPhone aria-hidden="true" />
              <div>
                <h3>Phone</h3>
                <p className="meta-text">+972-XXX-XXXX (Sun–Thu, 09:00–18:00)</p>
              </div>
            </div>

            <div className="meta-item">
              <FiMapPin aria-hidden="true" />
              <div>
                <h3>Address</h3>
                <p className="meta-text">42 Cinema Street, Tel Aviv</p>
              </div>
            </div>

            <p className="pro-tip">
              Pro tip: If your message starts with “This might sound weird, but…”, we’re already interested.
            </p>
          </div>
        </div>

        <form className="contact-form" onSubmit={handleSubmit} noValidate>
          <div className={`form-field ${errors.name ? 'has-error' : ''}`}>
            <label htmlFor="name">Name</label>
            <input
              id="name"
              name="name"
              value={form.name}
              onChange={handleChange}
              placeholder="Your full name"
              autoComplete="name"
            />
            {errors.name && <span className="error-text">{errors.name}</span>}
          </div>

          <div className={`form-field ${errors.email ? 'has-error' : ''}`}>
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              placeholder="you@domain.com"
              inputMode="email"
              autoComplete="email"
            />
            {errors.email && <span className="error-text">{errors.email}</span>}
          </div>

          <div className={`form-field ${errors.message ? 'has-error' : ''}`}>
            <label htmlFor="message">Message</label>
            <textarea
              id="message"
              name="message"
              value={form.message}
              onChange={handleChange}
              placeholder="What can we help with?"
              rows={6}
            />
            {errors.message && <span className="error-text">{errors.message}</span>}
          </div>

          <button className="submit-btn" type="submit" disabled={submitting}>
            <FiSend aria-hidden="true" />
            {submitting ? 'Sending…' : 'Send Message'}
          </button>

          {status.text && (
            <div
              role="status"
              className={`form-status ${status.type === 'error' ? 'is-error' : 'is-success'}`}
            >
              {status.text}
            </div>
          )}

          <p className="tiny-print">
            We won’t spam. No cliffhangers. No unnecessary sequels.
          </p>
        </form>
      </section>
    </main>
    </>
  );
}

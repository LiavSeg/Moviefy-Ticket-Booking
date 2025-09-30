// src/pages/PrivacyPolicy.jsx
import React from 'react';
import './styles/PrPolicy.css';
import GlobalHeader from '../Home/HomeHeader';

export default function PrPolicy() {
  return (
    <>
    <GlobalHeader/>
    <main className="privacy-page" aria-labelledby="privacy-title">
      <section className="privacy-hero">
        <h1 id="privacy-title">Moviefy Privacy Policy</h1>
        <p className="hero-tagline">
          We respect your privacy almost as much as we respect a spoiler-free movie experience.
        </p>
      </section>

      <section className="privacy-content">
        <h2>1. Information We Collect</h2>
        <p>
          When you sign up, book tickets, or leave reviews, we collect information like your name,
          email, and booking history. We don’t care about your popcorn preferences — unless you want to tell us.
        </p>

        <h2>2. How We Use Your Information</h2>
        <p>
          We use your info to manage bookings, send confirmations, show you relevant content, and make sure
          your experience runs smoother than a well-edited action scene.
        </p>

        <h2>3. Data Sharing</h2>
        <p>
          We don’t sell your data. Ever. The only people we might share it with are those who help us operate
          the platform (and they’re bound by confidentiality too).
        </p>

        <h2>4. Security</h2>
        <p>
          We use industry-standard measures to protect your data from unauthorized access.
          Sadly, we can’t protect you from bad remakes.
        </p>

        <h2>5. Cookies</h2>
        <p>
          Yes, we use cookies — the digital kind. They help us remember your preferences and improve our service.
          Sadly, they are not edible.
        </p>

        <h2>6. Your Rights</h2>
        <p>
          You can request a copy of your data, ask us to delete it, or update your details at any time.
          Just don’t ask us to delete “The Godfather” from cinema history — some things are sacred.
        </p>

        <h2>7. Changes to This Policy</h2>
        <p>
          We may update this Privacy Policy occasionally. We’ll let you know when we make changes,
          ideally without adding dramatic background music.
        </p>

        <h2>8. Contact Us</h2>
        <p>
          Questions? Concerns? Movie recommendations? Visit our <a href="/contact">Contact Us</a> page.
        </p>
      </section>

      <section className="tiny-print">
        <p>
          Last updated: {new Date().toLocaleDateString()}
        </p>
      </section>
    </main>
    </>
    );
}

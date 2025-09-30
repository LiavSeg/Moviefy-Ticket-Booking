// src/pages/TermsOfService.jsx
import React from 'react';
import './styles/TermsOfService.css';
import GlobalHeader from '../Home/HomeHeader';

export default function TermsOfService() {
  return (
    <>
    <GlobalHeader/>
    <main className="terms-page" aria-labelledby="terms-title">
      <section className="terms-hero">
        <h1 id="terms-title">Moviefy Terms of Service</h1>
        <p className="hero-tagline">
          Welcome to Moviefy — where booking a seat is easier than surviving a horror movie jump scare.
        </p>
      </section>

      <section className="terms-content">
        <h2>1. Acceptance of Terms</h2>
        <p>
          By accessing or using Moviefy, you agree to these Terms of Service. If you don’t agree,
          please refrain from using our platform (we’ll miss you, though).
        </p>

        <h2>2. Our Role</h2>
        <p>
          We provide a web-based movie ticket booking service. You provide the popcorn. Together,
          we make cinema magic.
        </p>

        <h2>3. User Accounts</h2>
        <p>
          You may need to create an account to access certain features, like booking tickets or leaving reviews.
          Keep your login details secret — we’re not responsible if your cat books 12 seats for “The Lion King.”
        </p>

        <h2>4. Booking & Payments</h2>
        <p>
          When you book, you agree to pay the price shown at checkout. No refunds for
          changing your mind after the movie starts — that’s on you.
        </p>

        <h2>5. Reviews</h2>
        <p>
          Feel free to share your thoughts, but keep it respectful. Offensive or spammy content will be removed
          faster than a bad scene in the director’s cut.
        </p>

        <h2>6. System Use</h2>
        <p>
          No hacking, reverse engineering, or exploiting the system. That’s not only uncool — it’s also illegal.
        </p>

        <h2>7. Changes to Terms</h2>
        <p>
          We may update these Terms occasionally. If we make significant changes, we’ll post a notice
          (probably without dramatic background music).
        </p>

        <h2>8. Contact Us</h2>
        <p>
          If you have questions, complaints, or movie suggestions, visit our <a href="/contact">Contact Us</a> page.
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

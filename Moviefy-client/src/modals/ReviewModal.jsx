import React from "react";
import "./ReviewModal.css";
import RatingBar from "../components/reviews/RatingBar"; 

export default function ReviewModal({
  isOpen,
  title = "Write a Review",
  rating,
  setRating,
  comment,
  setComment,
  onSubmit,
  onClose,
  isSubmitting = false,
}) {
  if (!isOpen) return null;

  const handleBackdrop = (e) => {
    if (e.target.classList.contains("review-modal-overlay")) onClose();
  };

  return (
    <div
      className="review-modal-overlay"
      role="dialog"
      aria-modal="true"
      aria-labelledby="review-modal-title"
      onMouseDown={handleBackdrop}
    >
      <div className={`review-modal ${isSubmitting ? "is-loading" : ""}`}>
        <div className="review-modal-header">
          <h2 id="review-modal-title" className="review-modal-title">
            {title}
          </h2>
          <button
            type="button"
            className="review-modal-close"
            aria-label="Close"
            onClick={onClose}
          >
            ✕
          </button>
        </div>

        <div className="review-stars">
          <RatingBar rating={parseInt(rating || 0)} setRating={setRating} />
        </div>

        <textarea
          className="review-textarea"
          placeholder="Write your review here..."
          value={comment}
          onChange={(e) => setComment(e.target.value)}
        />

        <div className="review-modal-actions">
          <button
            className="btn btn-ghost"
            type="button"
            onClick={onClose}
            disabled={isSubmitting}
          >
            Cancel
          </button>
          <button
            className="btn btn-primary"
            type="button"
            onClick={onSubmit}
            disabled={isSubmitting || !rating}
          >
            Submit
          </button>
        </div>
      </div>
    </div>
  );
}

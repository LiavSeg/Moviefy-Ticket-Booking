import React, { useState } from 'react';
import './UserReviewList.css';

function UserReviewList({
  reviews = [],
  expandedReviewId,
  setExpandedReviewId,
  onDeleteReview,    
  onEditReview,       
}) {
  const [busyId, setBusyId] = useState(null);

  const renderStars = (n = 0) =>
    Array.from({ length: 5 }, (_, i) => (
      <span key={i} className={`star ${i < n ? 'filled' : ''}`}>★</span>
    ));

  return (
    <section className="section-block">
      <h3>My Reviews</h3>

      {reviews.length === 0 ? (
        <p>No reviews yet.</p>
      ) : (
        <div className="review-list">
          {reviews.map((r) => (
            <div
              key={r.reviewId || `${r.username}-${r.movieTitle}-${r.createdAt}`}
              className={`review-card ${expandedReviewId === r.reviewId ? 'expanded' : ''}`}
              onClick={() =>
                setExpandedReviewId(
                  expandedReviewId === r.reviewId ? null : r.reviewId
                )
              }
            >
              <div className="review-header">
                {/* <img
                  src={r.imageUrl || '/placeholder.jpg'}
                  alt={r.movieTitle}
                  className="review-poster"
                /> */}
                <div className="review-info">
                  <h4>{r.movieTitle}</h4>
                  <div className="rating">{renderStars(Number(r.rating) || 0)}</div>
                  <p className="meta">
                    <strong>By:</strong> {r.username} ·{' '}
                    <strong>Date:</strong>{' '}
                    {r.createdAt
                      ? new Date(r.createdAt).toLocaleString()
                      : '—'}
                  </p>
                </div>
              </div>

              {expandedReviewId === r.reviewId && (
                <div className="review-extra" onClick={(e) => e.stopPropagation()}>
                  <p className="comment"><strong>Comment:</strong> {r.comment || '—'}</p>

                  <div className="review-actions">
                    {onEditReview && (
                      <button
                        className="btn ghost"
                        onClick={() => onEditReview(r)}
                        disabled={busyId === r.reviewId}
                      >
                        Edit
                      </button>
                    )}

                    {onDeleteReview && (
                      <button
                        className="btn danger"
                        onClick={async () => {
                          if (!window.confirm('Delete this review?')) return;
                          setBusyId(r.reviewId);
                          try {
                            await onDeleteReview(r.reviewId);
                          } finally {
                            setBusyId(null);
                          }
                        }}
                        disabled={busyId === r.reviewId}
                      >
                        {busyId === r.reviewId ? 'Deleting…' : 'Delete'}
                      </button>
                    )}
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </section>
  );
}

export default UserReviewList;

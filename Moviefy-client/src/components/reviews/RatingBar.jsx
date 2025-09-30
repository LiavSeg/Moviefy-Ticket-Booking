import React, { useState } from 'react';
import { FaStar, FaRegStar } from 'react-icons/fa';
import './RatingBar.css';

export default function RatingBar({ rating, setRating }) {
  const [hover, setHover] = useState(0);

  return (
    <div className="star-rating">
      {[...Array(10)].map((_, index) => {
        const starValue = index + 1;
        const isFilled = starValue <= (hover || rating);

        return (
          <span
            key={starValue}
            onClick={() => setRating(starValue)}
            onMouseEnter={() => setHover(starValue)}
            onMouseLeave={() => setHover(0)}
            className="star"
          >
            {isFilled ? <FaStar /> : <FaRegStar />}
          </span>
        );
      })}
    </div>
  );
}

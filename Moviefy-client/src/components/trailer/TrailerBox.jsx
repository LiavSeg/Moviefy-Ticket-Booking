// components/trailer/TrailerEmbed.jsx
import React from "react";
import "./TrailerBox.css";

export default function TrailerBox({ trailerUrl, posterUrl, title }) {
  if (!trailerUrl) return null;

  const getEmbed = (url) => {
    // YouTube
    const yt = url.match(
      /(?:youtu\.be\/|youtube\.com\/(?:watch\?v=|embed\/|shorts\/))([A-Za-z0-9_-]{6,})/
    );
    if (yt) return { src: `https://www.youtube.com/embed/${yt[1]}?rel=0&autoplay=1`, mp4: false };

    // Vimeo
    const vi = url.match(/vimeo\.com\/(?:video\/)?(\d+)/);
    if (vi) return { src: `https://player.vimeo.com/video/${vi[1]}?autoplay=1`, mp4: false };

    // MP4
    if (url.endsWith(".mp4")) return { src: url, mp4: true };

    return null;
  };

  const data = getEmbed(trailerUrl);
  if (!data) return null;

  return (
    <div className="trailer-embed" style={{ "--poster-url": `url(${posterUrl})` }}>
      {data.mp4 ? (
        <video className="trailer-media" src={data.src} controls playsInline autoPlay />
      ) : (
        <iframe
          className="trailer-media"
          src={data.src}
          title={`${title} Trailer`}
          allow="accelerometer; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
          allowFullScreen
        />
      )}
    </div>
  );
}

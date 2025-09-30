import React from 'react';
import logoDark from './assets/MoviefyTranspLogo.png';
import logoWhite from './assets/MoviefyTranspLogoWhite.png';

function MoviefyLogo() {
  const isDark = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches;
  const logoSrc = isDark? logoDark:logoWhite;
  return (
    <img 
      src={logoWhite} 
      alt="Moviefy" 
      
    />
  
  );
}

export default MoviefyLogo;

import React, { useEffect, useState } from 'react';


function SortBarHeader(){
    
const genres = [
  'Action Movies', 'Drama', 'Comedy Movies', 'Kids Movies',
  'Horror Movies', 'Romance Movies', 'Sci-Fi Movies', 'Animated Movies',
  'Documentaries', 'IMAX', '3D', 'Suspense',
  'Indie Movies', 'Foreign', 'Special Events', 'Western',
  'Spy Film', 'Historical Film', 'Classic Movies', 'War',
  'Dance', 'Film, TV & Radio', 'Music/Performing Arts'
];
  return(
    <div className='sortbar-header'>
      <h1 className='pick-type'>Movies by genre</h1>
      <ul className='sort-options'>
        <li className='now-in-theater'>Movies Now In Theater</li>
        <li className='coming-soon'>Coming Soon</li>
      </ul>
    </div>
  );
}

export default SortBarHeader;
const genres = [
  'Action', 'Drama', 'Comedy', 'Horror', 'Romance',
  'Sci-Fi','Suspense',
  'Indie', 'Foreign', 'Western',
  'Spy Film', 'Historical Film', 'Classic', 'War',
   'Film','Super Hero', 'Fantasy'
];
import './GenreFilterBar.css'
function GenreFilterBar({ onSelect, selected }) {
  return (
    <div className="genre-filter-bar">
      {genres.map((g, i) => (
        <button
          key={i}
          className={`genre-btn ${selected === g ? 'active' : ''}`}
          onClick={() => onSelect(g)}
        >
          {g.toUpperCase()}
        </button>
      ))}
    </div>
  );
}
export default GenreFilterBar;
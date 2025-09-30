import Header from "./HomeHeader.jsx";
import HeroBanner from "./HeroBanner.jsx";
import HomeSection from "./HomeSection.jsx"
import { useNavigate } from "react-router-dom";
import React, {useState ,useEffect} from 'react';
import { getMovieIdsByTitles } from "../../api/movies.jsx";
  const tabs = [
  'MOVIES IN THEATERS',
  'COMING SOON',
  'ALL TIMES FANS FAVORITES',
  'MOVIE GENRES',
  'AT HOME'
];

const bannerTitles = [
  "The Fantastic 4: First Steps",
  "Superman",
  "Happy Gilmore 2",
  "F1",
  "How To Train Your Dragon",
];

const bannerMeta = {
  "The Fantastic 4: First Steps": {
    poster:
      "https://image.tmdb.org/t/p/original/4b2mLlMMaMcZhvNvmPgwaFqme8Y.jpg",
    objectPosition: "50% 10%",
    textPosition: { top: "4%", left: "4%" },
    releaseYear: 2025,
    duration: 115,
    genre: "Sci-Fi",
  },
  Superman: {
    poster:
      "https://image.tmdb.org/t/p/original/f6bqshNexO1BvS4CMj7Q0YsFn6C.jpg",
    objectPosition: "30% 10%",
    textPosition: { top: "60%", left: "72%" },
    releaseYear: 2025,
    duration: 130,
    genre: "Action, Sci-Fi",
  },
  "Happy Gilmore 2": {
    poster:
      "https://image.tmdb.org/t/p/original/ynT06XivgBDkg7AtbDbX1dJeBGY.jpg",
    objectPosition: "10% 15%",
    textPosition: { top: "50%", left: "8%" },
    releaseYear: 2025,
    duration: 118,
    genre: "Comedy",
  },
  F1: {
    poster:
      "https://image.tmdb.org/t/p/original/6H6p82aWQFEKEuVUiZll6JxV8Ft.jpg",
    objectPosition: "40% 30%",
    textPosition: { top: "70%", left: "5%" },
    releaseYear: 2025,
    duration: 186,
    genre: "Action",
  },
  "How To Train Your Dragon": {
    poster:
      "https://image.tmdb.org/t/p/original/53dsJ3oEnBhTBVMigWJ9tkA5bzJ.jpg",
    objectPosition: "10% 55%",
    textPosition: { top: "14%", left: "5%" },
    releaseYear: 2025,
    duration: 125,
    genre: "Fantasy",
  },
};
function Home() {
  const [moviesBanner, setMoviesBanner] = useState([]);

  useEffect(() => {
    getMovieIdsByTitles(bannerTitles)
      .then(({ data: ids }) => {
        const banner = bannerTitles.map((title, i) => {
          const meta = bannerMeta[title];
          const id = ids[i];
          if (!meta || !id) return null;  
          return { id, title, ...meta };
        }).filter(Boolean);
        setMoviesBanner(banner);
      })
      .catch(err => console.error("Banner IDs fetch failed", err));
  }, []);


  return (
    <div className="home-dynamic">
      <Header />
      <HeroBanner movies={moviesBanner} />

      <HomeSection
        title="Now in Theaters"
        linkText="See All"
        linkUrl={`/movies?tab=${encodeURIComponent(tabs[0])}&genre=${encodeURIComponent('All')}`}
        tab={tabs[0]}
      />

      <HomeSection
        title="Coming Soon"
        linkText="Pre-Order"
        linkUrl={`/movies?tab=${encodeURIComponent(tabs[1])}&genre=${encodeURIComponent('All')}`}
        tab={tabs[1]}
      />

      <HomeSection
        title="Fan Favorites"
        linkText="Explore"
        linkUrl={`/movies?tab=${encodeURIComponent(tabs[2])}&genre=${encodeURIComponent('All')}`}
        tab={tabs[2]}
      />
    </div>
  );


}
export default Home;
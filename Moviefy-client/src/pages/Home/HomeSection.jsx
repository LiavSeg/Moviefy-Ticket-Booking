import SectionHeader  from './SectionHeader.jsx';
import MovieCarousel  from './MovieCarousel.jsx';
import './styles/HomeSection.css'


function HomeSection({title,linkText,linkUrl,tab}){
    return(
    <section className='home-section'>
        <SectionHeader title={title} linkText={linkText} linkUrl={linkUrl} />
        <MovieCarousel tab = {tab}/>
    </section>
    );
}
export default HomeSection
import { FaFacebook, FaInstagram, FaGithub } from 'react-icons/fa';
import './styles/Footer.css'; 
import logoWhite from './assets/MoviefyTranspLogoWhite.png';
import { Link } from "react-router-dom";

function Footer() {
  return (
    <footer className="footer-container">
      <div className="footer-top">
        <div className="footer-brand">
        <div className="logo">
        <img src={logoWhite} alt="Moviefy Logo" className="logo-img" />
        <span className="logo-text">Moviefy</span>
        </div>
          <p>&copy; {new Date().getFullYear()} Moviefy. All rights reserved.</p>
        </div>

        <div className="footer-links">
          <Link to="/pr-policy" target="_blank" rel="noopener noreferrer">Privacy</Link>
          <Link to="/terms">Terms of Service</Link>
          <Link to="/contact">Contact Us</Link>
        </div>

        <div className="footer-social">
          <a href="https://facebook.com" target="_blank" rel="noopener noreferrer"><FaFacebook /></a>
          <a href="https://instagram.com" target="_blank" rel="noopener noreferrer"><FaInstagram /></a>
          <a href="https://github.com/LiavSeg" target="_blank" rel="noopener noreferrer"><FaGithub /></a>
        </div>
      </div>
      <div className="footer-bottom">
        <p>Developed by <strong>Liav Segev</strong></p>
      </div>
    </footer>
  );
}
export default Footer;
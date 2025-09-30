import { Link } from 'react-router-dom';
import './styles/SectionHeader.css';

function SectionHeader({ title, linkText, linkUrl }) {
  return (
    <div className="section-header">
      <h2 className="section-title">{title}</h2>
      <Link className="section-link" to={linkUrl}>
        {linkText}
      </Link>
    </div>
  );
}

export default SectionHeader;

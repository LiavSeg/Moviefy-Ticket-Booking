import React, { useMemo } from 'react';
import "./ReportPreview.css";

/**
 * ReportPreview Component
 *
 * Responsibilities:
 * - Display report previews in HTML format for on-screen viewing.
 * - Provide XML download link for saving reports.
 * - Parse the HTML report to produce a summary section.
 *
 * Props:
 * @param {string|null} error - Error message to display if report generation failed.
 * @param {string|null} htmlPreview - HTML preview string (from XSLT transformation).
 * @param {string|null} xmlData - Raw XML string of the report data (for download only).
 */
export default function ReportPreview({ error, htmlPreview, xmlData }) {

  const summary = useMemo(() => {
    const computeSummary = (bookings) => {
      if (!bookings.length) return null;
      const totalIncome = bookings.reduce((sum, b) => sum + (b.price || 0), 0);
      const userCounts = {};
      const movieCounts = {};
      let totalSeats = 0;

      bookings.forEach(b => {
        userCounts[b.username] = (userCounts[b.username] || 0) + 1;
        movieCounts[b.movieTitle] = (movieCounts[b.movieTitle] || 0) + 1;
        totalSeats += b.seats.length;
      });

      const topUser = Object.entries(userCounts).sort((a, b) => b[1] - a[1])[0];
      const topMovie = Object.entries(movieCounts).sort((a, b) => b[1] - a[1])[0];

      return {
        totalIncome,
        totalBookings: bookings.length,
        totalSeats,
        topUser: { name: topUser?.[0], count: topUser?.[1] },
        topMovie: { title: topMovie?.[0], count: topMovie?.[1] }
      };
    };

    if (htmlPreview) {
      try {
        const doc = new DOMParser().parseFromString(htmlPreview, "text/html");
        const rows = Array.from(doc.querySelectorAll("table tr")).slice(1); // skip header
        const bookings = rows.map(row => {
          const cells = row.querySelectorAll("td");
          return {
            username: cells[2]?.textContent.trim() || "Unknown",
            movieTitle: cells[3]?.textContent.trim() || "Unknown",
            price: parseFloat(cells[7]?.textContent.trim()) || 0,
            seats: cells[6]?.textContent.split(",").map(s => s.trim()).filter(Boolean)
          };
        });
        return computeSummary(bookings);
      } catch {
        return null;
      }
    }

    return null;
  }, [htmlPreview]);

  return (
    <>
      {error && (
        <div className="export-error" role="alert">{error}</div>
      )}

      {htmlPreview && (
        <div className="export-preview export-html">
          <h4>Report Preview (HTML)</h4>
          <iframe
            title="Report Preview"
            srcDoc={htmlPreview}
            style={{
              width: '100%',
              minHeight: '500px',
              border: 'none',
              background: '#fff',
              borderRadius: '6px',
              boxShadow: '0 0 8px rgba(0,0,0,0.2)',
              marginBottom: '1.5rem'
            }}
          />
        </div>
      )}

      {xmlData && (
        <div className="export-actions">
          <a
            href={`data:text/xml;charset=utf-8,${encodeURIComponent(xmlData)}`}
            download="report.xml"
            className="download-link"
          >
            Download XML Report
          </a>
        </div>
      )}

      {summary && (
        <div className="report-summary">
          <h4>Report Summary</h4>
          <ul>
            <li><strong>Top Customer:</strong> {summary.topUser.name} ({summary.topUser.count} bookings)</li>
            <li><strong>Top Movie:</strong> {summary.topMovie.title} ({summary.topMovie.count} bookings)</li>
            <li><strong>Total Bookings:</strong> {summary.totalBookings}</li>
            <li><strong>Total Tickets Sold:</strong> {summary.totalSeats}</li>
            <li><strong>Total Income:</strong> ${summary.totalIncome.toFixed(2)}</li>
          </ul>
        </div>
      )}
    </>
  );
}

import React from 'react';
import "./DashboardControls.css"

export default function DashboardControls({
  startDate,
  endDate,
  chartType,
  exportFormat,
  exporting,
  onStartDateChange,
  onEndDateChange,
  onChartTypeChange,
  onExportFormatChange,
  onExportClick
}) {
  return (
    <div className="date-type-picker-bar">
      {/* Date pickers */}
      <div className="range-pickers">
        <label>Start Date:</label>
        <input type="date" value={startDate} onChange={e => onStartDateChange(e.target.value)} />

        <label>End Date:</label>
        <input type="date" value={endDate} onChange={e => onEndDateChange(e.target.value)} />
      </div>

      {/* Chart type */}
      <div className="type-picker-inline">
        <label htmlFor="chartType">Select Chart Type:</label>
        <select id="chartType" value={chartType} onChange={e => onChartTypeChange(e.target.value)}>
          <option value="bookings">Bookings by Range</option>
          <option value="popularMovies">Popular Movies</option>
          <option value="income">Income by Range</option>
        </select>
      </div>

      {/* Export */}
     {chartType !== "income" && (
  <div className="export-toolbar">
    <label htmlFor="exportFormat">Export:</label>
    <select
      id="exportFormat"
      value={exportFormat}
      onChange={(e) => onExportFormatChange(e.target.value)}
    >
      <option value="html">HTML</option>
      <option value="download">DOWNLOAD</option>
    </select>

    <button
      type="button"
      className="export-btn"
      onClick={onExportClick}
      disabled={exporting}
      title={`Export ${chartType} from ${startDate} to ${endDate}`}
    >
      {exporting ? 'Generating…' : 'Export Report'}
    </button>
  </div>
)}
    </div>
  );
}

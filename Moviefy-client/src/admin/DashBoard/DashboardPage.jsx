import React, { useEffect, useState } from 'react';
import './DashboardPage.css';
import { getChartData } from '../../api/admin';
import { exportReport, getFilenameFromContentDisposition } from '../../api/reports';

import DashboardControls from './DashboardControls';
import DashboardChart from './DashboardChart';
import ReportPreview from './ReportPreview';

const formatDate = (date) => date.toISOString().split('T')[0];
const toIsoWithoutMs = (date) => {
  const d = new Date(date);
  d.setSeconds(0, 0);
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}T${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}:${String(d.getSeconds()).padStart(2, '0')}`;
};

export default function DashboardPage() {
  const today = new Date();
  const startOfYear = new Date(today.getFullYear(), 0, 1);
  const endOfYear = new Date(today.getFullYear(), 11, 31, 23, 59, 59);

  const [monthlyData, setMonthlyData] = useState([]);
  const [startDate, setStartDate] = useState(formatDate(startOfYear));
  const [endDate, setEndDate] = useState(formatDate(endOfYear));
  const [chartType, setChartType] = useState("bookings");
  const [exportFormat, setExportFormat] = useState("html");
  const [exporting, setExporting] = useState(false);
  const [exportError, setExportError] = useState("");
  const [htmlPreview, setHtmlPreview] = useState("");
  const [xmlPreview, setXmlPreview] = useState("");

  // Added popularMovies endpoint
  const chartToUrl = {
    bookings: "bookings-per-range",
    income: "income-per-range",
    popularMovies: "popular-movie-range", 
  };

  const typeToHeader = {
    bookings: "Bookings in range ",
    income: "Income in range ",
    popularMovies: "Popular Movies in range "
  };

  const chartTypeToOpType = {
    bookings: 'BOOKINGS_REPORT',
    income: 'INCOME_REPORT',
    popularMovies: 'POPULAR_MOVIE_REPORT'
  };

  useEffect(() => {
    const endpoint = chartToUrl[chartType];

    if (!endpoint) return;
    
    getChartData(new Date(startDate), new Date(endDate), endpoint)
      .then(rawData => {
            console.log(chartType);

        if (chartType === 'popularMovies') {
          const formatted = rawData.map(item => ({
            label: item.movieTitle,
            count: item.totalIncome
          }));
          setMonthlyData(formatted);
        } else {
          setMonthlyData(rawData);
        }
      })
      .catch(err => console.error("Failed to load", chartType, err));
  }, [startDate, endDate, chartType]);

  const opType = chartTypeToOpType[chartType];

  const handleExport = async () => {
    setExporting(true);
    setExportError("");
    setHtmlPreview("");
    setXmlPreview("");

    try {
      const payload = {
        opType,
        startDate: toIsoWithoutMs(startDate),
        endDate: toIsoWithoutMs(endDate)
      };

      const res = await exportReport(payload, exportFormat);

      if (exportFormat === "html") {
        setHtmlPreview(res.data);
      } else if (exportFormat === "xml") {
        setXmlPreview(typeof res.data === "string" ? res.data : String(res.data));
      } else if (exportFormat === "download") {
        const cd = res.headers?.["content-disposition"];
        const filename =
          getFilenameFromContentDisposition(cd) ||
          `${opType.toLowerCase()}_${startDate}_to_${endDate}.xml`;

        const blobUrl = URL.createObjectURL(res.data);
        const a = document.createElement('a');
        a.href = blobUrl;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        a.remove();
        URL.revokeObjectURL(blobUrl);
      }
    } catch (err) {
      setExportError(
        err?.response?.data
          ? (typeof err.response.data === 'string'
            ? err.response.data
            : JSON.stringify(err.response.data))
          : err.message || 'Failed to generate report'
      );
    } finally {
      setExporting(false);
    }
  };

  return (
    <div className="dashboard-container">
      <h2>Admin Dashboard</h2>

      <DashboardControls
        startDate={startDate}
        endDate={endDate}
        chartType={chartType}
        exportFormat={exportFormat}
        exporting={exporting}
        onStartDateChange={setStartDate}
        onEndDateChange={setEndDate}
        onChartTypeChange={setChartType}
        onExportFormatChange={setExportFormat}
        onExportClick={handleExport}
      />

      <DashboardChart
        title={`${typeToHeader[chartType]}`}
        data={monthlyData}
      />

      <ReportPreview
        error={exportError}
        htmlPreview={htmlPreview}
        xmlPreview={xmlPreview}
      />
    </div>
  );
}

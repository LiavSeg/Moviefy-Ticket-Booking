import React from 'react';
import "./DashboardChart.css"
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer, CartesianGrid, Cell
} from 'recharts';

export default function DashboardChart({ title, data }) {
  // Define a palette of colors (will loop if there are more bars than colors)
  const colors = ["#FF5733", "#33B5FF", "#75FF33", "#FFD733", "#A633FF", "#FF8C42", "#6A4C93"];

  return (
    <div className="dashboard-chart">
      <h3>{title}</h3>
      <ResponsiveContainer width="100%" height={300}>
        <BarChart data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="label" tick={{ fill: '#fff', fontSize: 14 }} />
          <YAxis tick={{ fill: '#fff' }} allowDecimals={false} />
          <Tooltip
            contentStyle={{ backgroundColor: '#333', border: 'none' }}
            labelStyle={{ color: '#ffc857' }}
            itemStyle={{ color: '#fff' }}
          />
          <Bar dataKey="count" radius={[6, 6, 0, 0]} barSize={60}>
            {data.map((entry, index) => (
              <Cell
                key={`cell-${index}`}
                fill={colors[index % colors.length]}
              />
            ))}
          </Bar>
        </BarChart>
      </ResponsiveContainer>
    </div>
  );
}

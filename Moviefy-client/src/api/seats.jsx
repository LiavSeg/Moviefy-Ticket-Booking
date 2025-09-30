// src/api/seats.js
import axios from 'axios';

axios.defaults.baseURL = 'http://localhost:8080';


export const getSeatAvailability = async (showtimeId) => {
  if (!showtimeId) {
    throw new Error("showtimeId is required for seat availability request");
  }
  const res = await axios.get('/seats/availability', {
    params: { showtimeId }
  });
  return res.data;
};

export const holdSeats = async (showtimeId, seatLabels) => {
  await axios.post('/seats/hold', { showtimeId, seatLabels });
};

export const confirmSeats = async (showtimeId, seatLabels) => {
  await axios.post('/seats/confirm', { showtimeId, seatLabels });
};

import axios from 'axios';

const BASE_URL = 'http://localhost:8080'; 

export const fetchReservedSeats = async (showtimeId) => {
  const response = await axios.get(`${BASE_URL}/bookings/${showtimeId}/seats`);
  return response.data.reservedSeats;
};

export const fetchShowtimeById = async (showtimeId) => {
  const response = await axios.get(`${BASE_URL}/showtimes/${showtimeId}`);
  return response.data;
};

import axios from 'axios';

export const createBooking = async (bookingDto) =>
  await axios.post('http://localhost:8080/bookings', bookingDto);

export const getUserBookings = async (id) =>
  await axios.get(`http://localhost:8080/bookings/user/${id}`);

export const getAllBookingsForAdmin = async () =>
  await axios.get('http://localhost:8080/bookings/admin');


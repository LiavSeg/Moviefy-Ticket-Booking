import axios from 'axios';
axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.withCredentials = true;


export const fetchMovieById = (id) =>
  axios.get(`/movies/${id}`);
  
export const fetchReviewsByMovieId = (id) =>
  axios.get(`/reviews/by-movie/${id}`);

export const fetchShowtimesByMovieId = (id) =>
  axios.get(`/showtimes/by-movie/${id}`);

export const getAllMovies = () =>
  axios.get(`/movies/all`);

export const DeleteMovie = (movieId) =>
  axios.delete(`/movies/${movieId}`);

export function patchMovieField(movieId, field, value) {
  return axios.patch(`/movies/update/${movieId}`, null, {
    params: { field, value },
  });
}

export const getMovieIdsByTitles = (titles) =>
  axios.post(`movies/ids-for-banner`, titles);
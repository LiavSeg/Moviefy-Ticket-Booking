import axios from '../configs/axiosConfig.js';



export const getUserProfile = async (id) => {
  const res = await axios.get(`/users/profile/${id}`);
  return res.data;
};

export const userLogin = async (credentials) => {
  const res = await axios.post('/users/login', credentials);
  return res.data;
};


export const getCurrentUser = async () => {
  const res = await axios.get('/users/me');
  return res.data;
};

export const userLogout = async () => {
  await axios.post('/users/logout', {});
  return true;
};


export const userRegister = async (payload) => {
  const res = await axios.post('/users/register', payload);
  return res.data;
};


export const updateUserProfile = async (userId, payload) => {
  const res = await axios.patch(`/users/edit-profile/${userId}`, payload);
  return res.data;
};

export const changeUserPassword = async (userId, payload) => {
  const res = await axios.patch(`/users/${userId}/change-password`, payload);
  return res.data;
};
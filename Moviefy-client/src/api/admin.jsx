import axios from '../configs/axiosConfig.js';


export const getChartData = async (start,end,endpoint) =>{
  const url  = `http://localhost:8080/admin/stats/${endpoint}`;
  console.log(url);
  const params = {
    start: start.toISOString().slice(0, 19), //YYYY-MM-DDTHH:mm:ss
    end: end.toISOString().slice(0, 19),
  };
  
    const response = await axios.get(url, { params });
    return response.data;

}


export const getAllShowtimes = () =>
  axios.get('http://localhost:8080/showtimes/all'); 

export const getAllUsers = () =>
  axios.get('/users/all'); 
  
export const editMovie = async (movieDto) =>{

}

export const DeleteMovie = async (movieId) =>{
  axios.delete(`http://localhost:8080/movies/${movieId}`); 
}

export const changeAdminPerms = (userId, isAdmin) => {
  return axios.patch(`/users/${userId}/admin-perms`, isAdmin);
};

export const updateShowtime = (showtimeId, dto) =>
  axios.post(`/showtimes/update/${showtimeId}`, dto);    

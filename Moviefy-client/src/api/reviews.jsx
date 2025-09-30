export const getUserReviews = async (id) =>
  await axios.get(`http://localhost:8080/reviews/user/${id}`);

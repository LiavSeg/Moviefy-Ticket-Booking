import axios from 'axios';

const instance = axios.create({
  baseURL: 'http://localhost:8080',
  headers: { 'Content-Type': 'application/json' }, withCredentials: true,   
});

instance.defaults.headers.post['Content-Type']  = 'application/json';
instance.defaults.headers.put['Content-Type']   = 'application/json';
instance.defaults.headers.patch['Content-Type'] = 'application/json';
instance.defaults.withCredentials = true;
let isRedirecting = false;

function safeRedirectToHome() {
  if (isRedirecting) return;
  if (window.location.pathname === '/') return; // כבר בבית
  isRedirecting = true;
  sessionStorage.setItem('auth:skipHydrate', '1');
  localStorage.removeItem('user');
  window.location.replace('/');
}

instance.interceptors.response.use(
  r => r,
  err => {
    const s = err?.response?.status;
    if (s === 401 || s === 403) {
      safeRedirectToHome();
    }

    return Promise.reject(err);
  }
);

export default instance;

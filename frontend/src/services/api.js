import axios from 'axios';
import config from '@/config';
import store from '@/store';
import router from '@/router';

const api = axios.create({
  baseURL: config.apiUrl,
  timeout: config.timeout
});

// Request interceptor
api.interceptors.request.use(
  config => {
    const token = store.state.auth.token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

// Response interceptor
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      store.dispatch('auth/logout');
      router.push('/login');
    }
    return Promise.reject(error);
  }
);

export default api; 
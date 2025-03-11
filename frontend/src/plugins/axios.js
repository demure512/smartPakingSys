import axios from 'axios';
import store from '../store';
import router from '../router';
import config from '../config';

axios.defaults.baseURL = config.apiUrl;
axios.defaults.timeout = config.timeout;

axios.interceptors.request.use(
  config => {
    const token = store.state.auth.token;
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  error => Promise.reject(error)
);

axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      store.dispatch('auth/logout');
      router.push('/login');
    }
    return Promise.reject(error);
  }
);

export default axios; 
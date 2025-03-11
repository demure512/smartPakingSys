import Vue from 'vue';
import App from './App.vue';
import router from './router';
import axios from 'axios';
import moment from 'moment';

// Configure axios
axios.defaults.baseURL = process.env.VUE_APP_API_URL || '';
axios.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response.status === 401) {
      localStorage.removeItem('token');
      router.push('/login');
    }
    return Promise.reject(error);
  }
);

// Configure Vue
Vue.config.productionTip = false;

// Create Vue instance
new Vue({
  router,
  render: h => h(App)
}).$mount('#app'); 
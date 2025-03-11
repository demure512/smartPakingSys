import axios from 'axios';

export default {
  namespaced: true,
  
  state: {
    token: localStorage.getItem('token') || null,
    user: JSON.parse(localStorage.getItem('user')) || null
  },

  mutations: {
    SET_TOKEN(state, token) {
      state.token = token;
      if (token) {
        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      } else {
        localStorage.removeItem('token');
        delete axios.defaults.headers.common['Authorization'];
      }
    },
    SET_USER(state, user) {
      state.user = user;
      if (user) {
        localStorage.setItem('user', JSON.stringify(user));
      } else {
        localStorage.removeItem('user');
      }
    }
  },

  actions: {
    async login({ commit }, credentials) {
      try {
        const response = await axios.post('/api/auth/login', credentials);
        commit('SET_TOKEN', response.data.token);
        commit('SET_USER', response.data.user);
        return response.data;
      } catch (error) {
        throw error;
      }
    },

    async logout({ commit }) {
      commit('SET_TOKEN', null);
      commit('SET_USER', null);
    }
  },

  getters: {
    isAuthenticated: state => !!state.token,
    currentUser: state => state.user,
    hasRole: state => role => state.user?.roles.includes(role)
  }
}; 
import Vue from 'vue';
import Vuex from 'vuex';
import auth from './modules/auth';
import parking from './modules/parking';
import financial from './modules/financial';
import notifications from './modules/notifications';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    auth,
    parking,
    financial,
    notifications
  }
}); 
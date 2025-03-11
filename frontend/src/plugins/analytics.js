import Vue from 'vue';
import VueGtag from 'vue-gtag';
import config from '@/config';

if (process.env.NODE_ENV === 'production') {
  Vue.use(VueGtag, {
    config: { id: config.analytics.id }
  });
} 
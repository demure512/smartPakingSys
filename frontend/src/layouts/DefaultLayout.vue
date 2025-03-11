<template>
  <v-app>
    <Header @toggle-drawer="drawer = !drawer" />
    <Navigation v-model="drawer" />
    
    <v-main>
      <v-container fluid>
        <router-view></router-view>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
import Header from '@/components/layout/Header.vue';
import Navigation from '@/components/layout/Navigation.vue';

export default {
  name: 'DefaultLayout',
  components: {
    Header,
    Navigation
  },
  data() {
    return {
      drawer: true
    };
  }
};
</script> 
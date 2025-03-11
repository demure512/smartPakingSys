<template>
  <v-app-bar app color="primary" dark>
    <v-app-bar-nav-icon @click="$emit('toggle-drawer')"></v-app-bar-nav-icon>
    <v-toolbar-title>Smart Parking System</v-toolbar-title>
    <v-spacer></v-spacer>
    <v-btn icon @click="toggleNotifications">
      <v-badge
        :content="unreadNotifications"
        :value="unreadNotifications > 0"
        color="red"
      >
        <v-icon>mdi-bell</v-icon>
      </v-badge>
    </v-btn>
    <v-menu offset-y>
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          text
          v-bind="attrs"
          v-on="on"
        >
          {{ currentUser.name }}
          <v-icon right>mdi-chevron-down</v-icon>
        </v-btn>
      </template>
      <v-list>
        <v-list-item @click="goToProfile">
          <v-list-item-title>Profile</v-list-item-title>
        </v-list-item>
        <v-list-item @click="logout">
          <v-list-item-title>Logout</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';

export default {
  name: 'Header',
  computed: {
    ...mapGetters({
      currentUser: 'auth/currentUser',
      unreadNotifications: 'notifications/unreadCount'
    })
  },
  methods: {
    ...mapActions({
      logout: 'auth/logout'
    }),
    toggleNotifications() {
      this.$emit('toggle-notifications');
    },
    goToProfile() {
      this.$router.push('/profile');
    }
  }
};
</script> 
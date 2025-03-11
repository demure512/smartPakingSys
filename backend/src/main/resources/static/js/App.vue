<template>
  <div id="app">
    <nav v-if="isAuthenticated" class="navbar">
      <div class="navbar-brand">
        <router-link to="/" class="navbar-logo">
          Smart Parking
        </router-link>
      </div>
      <div class="navbar-menu">
        <router-link to="/dashboard" class="nav-item">Dashboard</router-link>
        <router-link to="/parking-lots" class="nav-item">Parking Lots</router-link>
        <router-link to="/vehicles" class="nav-item">Vehicles</router-link>
        <router-link to="/transactions" class="nav-item">Transactions</router-link>
        <router-link to="/reports" class="nav-item">Reports</router-link>
        <div class="nav-item" @click="logout">Logout</div>
      </div>
    </nav>

    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App',
  computed: {
    isAuthenticated() {
      return !!localStorage.getItem('token');
    }
  },
  methods: {
    logout() {
      localStorage.removeItem('token');
      this.$router.push('/login');
    }
  }
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
}

.navbar {
  background-color: #2c3e50;
  padding: 1rem 2rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
}

.navbar-logo {
  color: white;
  text-decoration: none;
  font-size: 1.5rem;
  font-weight: bold;
}

.navbar-menu {
  display: flex;
  gap: 1.5rem;
}

.nav-item {
  color: white;
  text-decoration: none;
  cursor: pointer;
}

.nav-item:hover {
  color: #3498db;
}

.router-link-active {
  color: #3498db;
}
</style> 
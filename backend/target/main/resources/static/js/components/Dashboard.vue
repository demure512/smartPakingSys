<template>
  <div class="dashboard">
    <div class="stats-container">
      <div class="stat-card">
        <h3>Total Vehicles</h3>
        <div class="stat-value">{{ stats.totalVehicles }}</div>
      </div>
      <div class="stat-card">
        <h3>Available Spaces</h3>
        <div class="stat-value">{{ stats.availableSpaces }}</div>
      </div>
      <div class="stat-card">
        <h3>Today's Revenue</h3>
        <div class="stat-value">${{ stats.todayRevenue }}</div>
      </div>
      <div class="stat-card">
        <h3>Active Parking</h3>
        <div class="stat-value">{{ stats.activeParking }}</div>
      </div>
    </div>

    <div class="recent-activity">
      <h2>Recent Activity</h2>
      <div class="activity-list">
        <div v-for="activity in recentActivity" :key="activity.id" class="activity-item">
          <div class="activity-time">{{ formatTime(activity.timestamp) }}</div>
          <div class="activity-details">
            <span class="activity-type">{{ activity.type }}</span>
            <span class="activity-description">{{ activity.description }}</span>
          </div>
        </div>
      </div>
    </div>

    <div class="parking-lots">
      <h2>Parking Lots Status</h2>
      <div class="parking-lots-grid">
        <div v-for="lot in parkingLots" :key="lot.id" class="parking-lot-card">
          <h3>{{ lot.name }}</h3>
          <div class="lot-stats">
            <div>Capacity: {{ lot.capacity }}</div>
            <div>Available: {{ lot.availableSpaces }}</div>
            <div>Occupancy: {{ calculateOccupancy(lot) }}%</div>
          </div>
          <div class="progress-bar">
            <div 
              class="progress" 
              :style="{ width: calculateOccupancy(lot) + '%', backgroundColor: getOccupancyColor(lot) }"
            ></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import moment from 'moment';

export default {
  name: 'Dashboard',
  data() {
    return {
      stats: {
        totalVehicles: 0,
        availableSpaces: 0,
        todayRevenue: 0,
        activeParking: 0
      },
      recentActivity: [],
      parkingLots: []
    };
  },
  async created() {
    await this.fetchDashboardData();
  },
  methods: {
    async fetchDashboardData() {
      try {
        const [statsResponse, activityResponse, lotsResponse] = await Promise.all([
          axios.get('/api/dashboard/stats'),
          axios.get('/api/dashboard/recent-activity'),
          axios.get('/api/parking-lots')
        ]);

        this.stats = statsResponse.data;
        this.recentActivity = activityResponse.data;
        this.parkingLots = lotsResponse.data;
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      }
    },
    formatTime(timestamp) {
      return moment(timestamp).fromNow();
    },
    calculateOccupancy(lot) {
      return Math.round(((lot.capacity - lot.availableSpaces) / lot.capacity) * 100);
    },
    getOccupancyColor(lot) {
      const occupancy = this.calculateOccupancy(lot);
      if (occupancy < 60) return '#28a745';
      if (occupancy < 80) return '#ffc107';
      return '#dc3545';
    }
  }
};
</script>

<style scoped>
.dashboard {
  padding: 2rem;
}

.stats-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-value {
  font-size: 2rem;
  font-weight: bold;
  color: #007bff;
}

.recent-activity {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  margin-bottom: 2rem;
}

.activity-item {
  display: flex;
  align-items: center;
  padding: 1rem 0;
  border-bottom: 1px solid #eee;
}

.activity-time {
  width: 120px;
  color: #666;
}

.activity-type {
  font-weight: bold;
  margin-right: 1rem;
}

.parking-lots-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 1.5rem;
}

.parking-lot-card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.lot-stats {
  margin: 1rem 0;
}

.progress-bar {
  height: 8px;
  background-color: #eee;
  border-radius: 4px;
  overflow: hidden;
}

.progress {
  height: 100%;
  transition: width 0.3s ease;
}
</style> 
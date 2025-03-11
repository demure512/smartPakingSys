<template>
  <div class="loading-overlay" v-if="loading">
    <v-progress-circular
      indeterminate
      color="primary"
      size="64"
    ></v-progress-circular>
    <span class="loading-text">{{ message }}</span>
  </div>
</template>

<script>
export default {
  name: 'LoadingSpinner',
  props: {
    loading: {
      type: Boolean,
      default: false
    },
    message: {
      type: String,
      default: 'Loading...'
    }
  }
};
</script>

<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 9999;
}

.loading-text {
  margin-top: 16px;
  font-size: 1.2em;
  color: #666;
}
</style> 
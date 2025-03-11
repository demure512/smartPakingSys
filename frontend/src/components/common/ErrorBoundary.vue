<template>
  <div>
    <template v-if="error">
      <div class="error-container">
        <v-alert type="error" prominent>
          <v-row align="center">
            <v-col class="grow">
              {{ errorMessage }}
            </v-col>
            <v-col class="shrink">
              <v-btn @click="retry">
                Retry
              </v-btn>
            </v-col>
          </v-row>
        </v-alert>
      </div>
    </template>
    <template v-else>
      <slot></slot>
    </template>
  </div>
</template>

<script>
export default {
  name: 'ErrorBoundary',
  props: {
    error: {
      type: [Error, null],
      default: null
    },
    errorMessage: {
      type: String,
      default: 'An error occurred. Please try again.'
    }
  },
  methods: {
    retry() {
      this.$emit('retry');
    }
  }
};
</script>

<style scoped>
.error-container {
  padding: 16px;
}
</style> 
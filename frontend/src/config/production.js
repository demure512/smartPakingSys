export default {
  apiUrl: process.env.VUE_APP_API_URL || '/api',
  timeout: 30000,
  sentry: {
    dsn: process.env.VUE_APP_SENTRY_DSN,
    environment: 'production'
  },
  analytics: {
    id: process.env.VUE_APP_ANALYTICS_ID
  }
}; 
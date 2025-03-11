const config = {
  development: {
    apiUrl: 'http://localhost:8080/api',
    timeout: 10000
  },
  production: {
    apiUrl: '/api',
    timeout: 10000
  }
};

export default config[process.env.NODE_ENV || 'development']; 
// API Configuration
const API_CONFIG = {
  baseUrl: window.location.origin,
  // Use environment variable or fallback to Render backend
  get apiBaseUrl() {
    // For now, update this to your Render backend URL after backend is deployed
    const defaultBackend = 'https://seatallocation.onrender.com';
    return typeof process !== 'undefined' && process.env.REACT_APP_API_URL 
      ? process.env.REACT_APP_API_URL 
      : defaultBackend;
  }
};

// Export for use in modules
if (typeof module !== 'undefined' && module.exports) {
  module.exports = API_CONFIG;
}

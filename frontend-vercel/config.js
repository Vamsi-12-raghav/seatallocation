// API Configuration
const API_CONFIG = {
  baseUrl: window.location.origin,
  // Backend deployed on Render
  get apiBaseUrl() {
    // Use environment variable or fallback to Render backend
    return process.env.REACT_APP_API_URL || 'https://seatallocation.onrender.com';
  }
};

// Export for use in modules
if (typeof module !== 'undefined' && module.exports) {
  module.exports = API_CONFIG;
}

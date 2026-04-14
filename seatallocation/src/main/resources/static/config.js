// API Configuration
const API_CONFIG = {
  baseUrl: window.location.origin,
  // In production, Vercel will set this via environment variables
  // or the backend will be accessible via relative URLs since they'll be on the same origin
  get apiBaseUrl() {
    // If deployed separately on different domains, uncomment and set:
    // return 'https://seatallocation.onrender.com';
    
    // For same-origin deployment (static files served from Spring Boot):
    return this.baseUrl;
  }
};

// Export for use in modules
if (typeof module !== 'undefined' && module.exports) {
  module.exports = API_CONFIG;
}

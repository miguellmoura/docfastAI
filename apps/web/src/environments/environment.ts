const isProd = typeof window !== 'undefined' && window.location.hostname !== 'localhost';

export const environment = {
  production: isProd,
  apiUrl: isProd
    ? 'https://docfastai-production.up.railway.app/api'
    : 'http://localhost:8080/api'
};

import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api/auth',
    headers: {
      'Content-Type': 'application/json'
    }
  });

export default {
  async login(credenciales) {
    return await apiClient.post('/login', credenciales);
  },
  async registrar(data) {
    return await apiClient.post('/registro', data);
  }
};
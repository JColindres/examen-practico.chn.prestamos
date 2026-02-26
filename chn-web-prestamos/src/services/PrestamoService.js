import apiClient from './api';

export default {
  async getAprobados(id) {
    return await apiClient.get(`/prestamos/cliente/${id}`);
  },
  async realizarPago(id, data) {
    return await apiClient.post(`/prestamos/${id}/pagos`, data);
  },
  async getPagos(id) {
    return await apiClient.get(`/prestamos/${id}/listapagos`);
  }
};
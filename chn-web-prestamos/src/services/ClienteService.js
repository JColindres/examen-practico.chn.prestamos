import apiClient from './api';

export default {
  async getClientes() {
    return await apiClient.get('/clientes');
  },
  async crearCliente(data) {
    return await apiClient.post('/clientes', data);
  },
  async modificarCliente(id, data) {
    return await apiClient.put(`/clientes/${id}`, data);
  },
  async eliminarCliente(id) {
    return await apiClient.delete(`/clientes/${id}`);
  }
};
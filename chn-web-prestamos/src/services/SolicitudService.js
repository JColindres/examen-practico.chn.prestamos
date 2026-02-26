import apiClient from './api';

export default {
  async getSolicitudesPendientes() {
    return await apiClient.get('/solicitudes');
  },
  async getSolicitudesXCliente(id) {
    return await apiClient.get(`/solicitudes/cliente/${id}`);
  },
  async crearSolicitud(data) {
    return await apiClient.post('/solicitudes', data);
  },
  async aprobarSolicitud(id, data) {
    return await apiClient.put(`/solicitudes/${id}/aprobar`, null, {
        params: {
          comentario: data
        }
      });
  },
  async rechazarSolicitud(id, data) {
    return await apiClient.put(`/solicitudes/${id}/rechazar`, null, {
        params: {
          comentario: data
        }
      });
  }
};
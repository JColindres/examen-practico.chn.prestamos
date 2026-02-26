<template>
    <div class="gestion-container">
      <div class="header-actions">
        <h2>Solicitudes de Préstamos</h2>
        <select v-model="filtroSolicitudes" @change="consultaPorId" required class="slc">
            <option value="0" selected>Únicamente pendientes</option>
            <option v-for="cliente in clientes" :key="cliente.idCliente" :value="cliente.idCliente">
                {{ cliente.numeroIdentificacion }} - {{ cliente.nombre }} {{ cliente.apellido }}
            </option>
        </select>
        <button @click="mostrarFormulario = true" class="btn-primary">+ Nueva Solicitud</button>
      </div>
  
      <div v-if="mostrarFormulario" class="modal-overlay">
        <div class="modal-content">
          <h3>Nueva Solicitud de Préstamo</h3>
          <form @submit.prevent="crearSolicitud">
            <div class="form-group">
            <label>Cliente:</label>
              <select v-model="nuevaSolicitud.idCliente" required class="slc">
                <option value="" disabled selected>Seleccione un cliente</option>
                <option v-for="cliente in clientes" :key="cliente.idCliente" :value="cliente.idCliente">
                    {{ cliente.numeroIdentificacion }} - {{ cliente.nombre }} {{ cliente.apellido }}
                </option>
            </select>
            </div>
            <div class="form-group">
              <label>Monto (Q):</label>
              <input v-model.number="nuevaSolicitud.montoSolicitado" type="number" step="0.01" required placeholder="0.00">
            </div>
            <div class="form-group">
              <label>Plazo (Meses):</label>
              <input v-model.number="nuevaSolicitud.plazoMeses" type="number" required placeholder="Ej. 12">
            </div>
            <div class="form-actions">
              <button type="button" @click="cancelar" class="btn-cancel">Cancelar</button>
              <button type="submit" class="btn-save">Enviar Solicitud</button>
            </div>
          </form>
        </div>
      </div>
  
      <div v-if="solicitudGestion" class="modal-overlay">
        <div class="modal-content">
          <h3>Gestionar Solicitud #{{ solicitudGestion.idSolicitud }}</h3>
          <p><strong>Cliente:</strong> {{ clienteSeleccionado.numeroIdentificacion }} - {{ clienteSeleccionado.nombre }} {{ clienteSeleccionado.apellido }}</p>
          <p><strong>Monto:</strong> Q{{ solicitudGestion.montoSolicitado }} | <strong>Plazo:</strong> {{ solicitudGestion.plazoMeses }} meses</p>
          
          <div class="form-group">
            <label>Comentario / Observaciones:</label>
            <p v-if="solicitudGestion.estado != 'PENDIENTE'">{{ solicitudGestion.comentarioResolucion }}</p>
            <textarea v-if="solicitudGestion.estado === 'PENDIENTE'" v-model="comentario" rows="3" placeholder="Escriba el motivo de la decisión..."></textarea>
          </div>
  
          <div class="form-actions gestion-btns">
            <button @click="solicitudGestion = null" class="btn-cancel">Cerrar</button>
            <button v-if="solicitudGestion.estado === 'PENDIENTE'" @click="procesarAccion('RECHAZADA')" class="btn-delete">Rechazar</button>
            <button v-if="solicitudGestion.estado === 'PENDIENTE'" @click="procesarAccion('APROBADA')" class="btn-save">Aprobar</button>
          </div>
        </div>
      </div>
  
      <div class="table-card">
        <table class="data-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Monto</th>
              <th>Plazo</th>
              <th>Estado</th>
              <th>Fecha</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="sol in solicitudes" :key="sol.idSolicitud">
              <td>{{ sol.idSolicitud }}</td>
              <td>Q{{ sol.montoSolicitado }}</td>
              <td>{{ sol.plazoMeses }} meses</td>
              <td><span :class="['badge', { 
                                'pendiente': sol.estado === 'PENDIENTE', 
                                'aprobado': sol.estado === 'APROBADO',
                                'rechazado': sol.estado === 'RECHAZADO' 
                                }]">{{ sol.estado }}</span></td>
              <td>{{ sol.fechaSolicitud || 'Reciente' }}</td>
              <td>
                <button @click="abrirGestion(sol)" class="btn-manage">Gestionar</button>
              </td>
            </tr>
            <tr v-if="solicitudes.length === 0">
              <td colspan="6" style="text-align: center;">No hay solicitudes pendientes.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </template>
  
  <script>
  import SolicitudService from '@/services/SolicitudService';
  import ClienteService from '@/services/ClienteService';

  export default {
    data() {
      return {
        solicitudes: [],
        mostrarFormulario: false,
        solicitudGestion: null, 
        comentario: '',
        clientes: [],
        clienteSeleccionado: null,
        filtroSolicitudes: "0",
        nuevaSolicitud: {
          idCliente: null,
          montoSolicitado: null,
          plazoMeses: null
        }
      };
    },
    mounted() {
      this.cargarClientes();
      this.cargarPendientes();
    },
    methods: {
      async cargarClientes() {
        try {
          const response = await ClienteService.getClientes();
          this.clientes = response.data;
        } catch (error) {
          console.error("Error al traer los datos:", error);
        }
      },
      async cargarPendientes() {
        try {
          const response = await SolicitudService.getSolicitudesPendientes();
          this.solicitudes = response.data;
        } catch (error) {
          console.error("Error al cargar solicitudes:", error);
        }
      },
      async consultaPorId() {
        try {
            if (this.filtroSolicitudes === "0") {
                this.cargarPendientes();
            } else {
                const response = await SolicitudService.getSolicitudesXCliente(this.filtroSolicitudes);
                this.solicitudes = response.data;
            }
        } catch (error) {
            console.error("Error: ", error)            
        }
      },
      async crearSolicitud() {
        try {
          await SolicitudService.crearSolicitud(this.nuevaSolicitud);
          this.cargarPendientes();
          this.cancelar();
        } catch (error) {
          console.error("Error:", error);
          alert("Error al crear la solicitud");
        }
      },
      abrirGestion(solicitud) {
        this.solicitudGestion = solicitud;
        this.clienteSeleccionado = this.clientes.find(c => c.idCliente === solicitud.idCliente);
        this.comentario = '';
      },
      async procesarAccion(nuevoEstado) {
        if (!this.comentario && nuevoEstado === 'RECHAZADA') {
          alert("Debe incluir un comentario para rechazar.");
          return;
        }
  
        try {
            if (nuevoEstado === 'RECHAZADA')
            {
                await SolicitudService.rechazarSolicitud(this.solicitudGestion.idSolicitud, this.comentario);
            } else {
                await SolicitudService.aprobarSolicitud(this.solicitudGestion.idSolicitud, this.comentario);
            }
            this.solicitudGestion = null;
            this.clienteSeleccionado = null;
            this.cargarPendientes();
        } catch (error) {
            alert("Error al procesar la solicitud");
        }
      },
      cancelar() {
        this.mostrarFormulario = false;
        this.nuevaSolicitud = { idCliente: null, montoSolicitado: null, plazoMeses: null };
      }
    }
  };
  </script>
  
  <style scoped>
  .gestion-container { background: white; padding: 20px; border-radius: 8px; }
  .header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
  
  .table-card { border: 1px solid #eee; border-radius: 8px; overflow: hidden; }
  .data-table { width: 100%; border-collapse: collapse; }
  .data-table th, .data-table td { padding: 12px; border-bottom: 1px solid #eee; text-align: left; }
  .data-table th { background-color: #f8f9fa; }
  
  .badge { padding: 4px 8px; border-radius: 12px; font-size: 12px; font-weight: bold; }
  .pendiente { background: #fef9e7; color: #f1c40f; border: 1px solid #f1c40f; }
  .aprobado { background:rgb(204, 250, 211); color:rgb(38, 185, 9); border: 1px solid rgb(38, 185, 9); }
  .rechazado { background:rgb(248, 206, 205); color:rgb(241, 15, 15); border: 1px solid rgb(241, 15, 15); }
  
  .btn-primary { background: #2c3e50; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; }
  .btn-manage { background: #3498db; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; }
  
  .modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
  .modal-content { background: white; padding: 25px; border-radius: 8px; width: 450px; }
  .form-group { margin-bottom: 15px; }
  .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
  .form-group input, .form-group textarea { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; }
  
  .form-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
  .gestion-btns { justify-content: space-between; }
  .btn-save { background: #27ae60; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; }
  .btn-delete { background: #e74c3c; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; }
  .btn-cancel { background: #95a5a6; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; }
  .slc { background: white; color: black; border: 1px solid; padding: 8px 15px; border-radius: 4px; cursor: pointer; }
  </style>
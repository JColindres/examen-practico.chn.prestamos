<template>
  <div class="gestion-container">
    <div class="header-actions">
      <h2>Gestión de Clientes</h2>
      <button @click="prepararNuevo" class="btn-primary">+ Nuevo Cliente</button>
    </div>

    <div v-if="mostrarFormulario" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ editando ? 'Editar Cliente' : 'Nuevo Cliente' }}</h3>
        <form @submit.prevent="guardarCliente">
          <div class="form-group">
            <label>Nombre:</label>
            <input v-model="clienteActual.nombre" required placeholder="Ej. Juan">
          </div>
          <div class="form-group">
            <label>Apellido:</label>
            <input v-model="clienteActual.apellido" required placeholder="Ej. Pérez">
          </div>
          <div class="form-group">
            <label>DPI:</label>
            <input v-model="clienteActual.numeroIdentificacion" type="number" :disabled="editando" placeholder="1234567890101">
          </div>
          <div class="form-group">
            <label>Fecha Nacimiento:</label>
            <input v-model="clienteActual.fechaNacimiento" type="date" required input-format="yyyy-mm-dd">
          </div>
          <div class="form-group">
            <label>Direccion:</label>
            <input v-model="clienteActual.direccion" placeholder="Ciudad">
          </div>
          <div class="form-group">
            <label>Correo:</label>
            <input v-model="clienteActual.correo" type="email" placeholder="juan@correo.com">
          </div>
          <div class="form-group">
            <label>Teléfono:</label>
            <input v-model="clienteActual.telefono" type="number" placeholder="12345678">
          </div>
          <div class="form-actions">
            <button type="button" @click="cancelar" class="btn-cancel">Cancelar</button>
            <button type="submit" class="btn-save">{{ editando ? 'Actualizar' : 'Guardar' }}</button>
          </div>
        </form>
      </div>
    </div>

    <table class="data-table">
      <thead>
        <tr>
          <th>DPI</th>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in elementos" :key="item.idCliente">
          <td>{{ item.numeroIdentificacion }}</td>
          <td>{{ item.nombre }}</td>
          <td>{{ item.apellido }}</td>
          <td>
            <button @click="prepararEdicion(item)" class="btn-edit">✏️</button>
            <button @click="eliminarCliente(item.idCliente)" class="btn-delete">🗑️</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import ClienteService from '@/services/ClienteService';

export default {
  data() {
    return {
      elementos: [],
      mostrarFormulario: false,
      editando: false,
      clienteActual: { idCliente: null, nombre: '', apellido: '', numeroIdentificacion: '', fechaNacimiento: '', direccion: '', correo: '', telefono: '', fechaCreacion: '' }
    };
  },
  mounted() {
    this.cargarClientes();
  },
  methods: {
    async cargarClientes() {
      try {
        const response = await ClienteService.getClientes();
        this.elementos = response.data;
      } catch (error) {
        console.error("Error al traer los datos:", error);
      }
    },
    prepararNuevo() {
      this.editando = false;
      this.clienteActual = { idCliente: null, nombre: '', apellido: '', numeroIdentificacion: '', fechaNacimiento: '', direccion: '', correo: '', telefono: '', fechaCreacion: '' }
      this.mostrarFormulario = true;
    },
    prepararEdicion(cliente) {
      this.editando = true;
      this.clienteActual = { ...cliente };
      this.mostrarFormulario = true;
    },
    async guardarCliente() {
      try {
        if (this.editando) {
          await ClienteService.modificarCliente(this.clienteActual.idCliente, this.clienteActual);
        } else {
          await ClienteService.crearCliente(this.clienteActual);
        }
        this.cargarClientes();
        this.cancelar();
      } catch (error) {
        alert("Error al guardar el cliente");
      }
    },
    async eliminarCliente(id) {
      if (confirm("¿Estás seguro de eliminar este cliente?")) {
        try {
          await ClienteService.eliminarCliente(id);
          this.cargarClientes();
        } catch (error) {
          alert("No se pudo eliminar el cliente");
        }
      }
    },
    cancelar() {
      this.mostrarFormulario = false;
      this.clienteActual = { idCliente: null, nombre: '', apellido: '', numeroIdentificacion: '', fechaNacimiento: '', direccion: '', correo: '', telefono: '', fechaCreacion: '' }
    }
  }
};
</script>

<style scoped>
.gestion-container { background: white; padding: 20px; border-radius: 8px; }
.header-actions { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }

.data-table { width: 100%; border-collapse: collapse; margin-top: 10px; }
.data-table th, .data-table td { padding: 12px; border-bottom: 1px solid #eee; text-align: left; }
.data-table th { background-color: #f8f9fa; color: #333; }

.btn-primary { background: #3498db; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; }
.btn-edit { background: #f1c40f; border: none; padding: 5px 10px; border-radius: 4px; margin-right: 5px; cursor: pointer; }
.btn-delete { background: #e74c3c; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer; }

.modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal-content { background: white; padding: 30px; border-radius: 8px; width: 400px; }
.form-group { margin-bottom: 15px; }
.form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-group input { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; }
.form-actions { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
.btn-save { background: #27ae60; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; }
.btn-cancel { background: #95a5a6; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; }
</style>
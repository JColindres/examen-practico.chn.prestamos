<template>
    <div class="gestion-container">
      <div class="header-actions">
        <h2>Préstamos Activos (Aprobados)</h2>
        <select v-model="filtroAprobados" @change="cargarPrestamos" required class="slc">
            <option value="0" selected>Seleccione una opción</option>
            <option v-for="cliente in clientes" :key="cliente.idCliente" :value="cliente.idCliente">
                {{ cliente.numeroIdentificacion }} - {{ cliente.nombre }} {{ cliente.apellido }}
            </option>
        </select>
      </div>
  
      <div v-if="!prestamoSeleccionado" class="table-card">
        <table class="data-table">
          <thead>
            <tr>
              <th>#</th>
              <th>Monto Aprobado</th>
              <th>Tasa (%)</th>
              <th>Saldo Pendiente</th>
              <th>Pagos Realizados</th>
              <th>Fecha Aprob.</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="p in prestamos" :key="p.idPrestamo">
              <td>{{ p.idPrestamo }}</td>
              <td>Q{{ p.montoAprobado.toLocaleString() }}</td>
              <td>{{ p.tasaInteres }}%</td>
              <td class="text-bold text-danger">Q{{ p.saldoPendiente.toLocaleString() }}</td>
              <td>{{ p.totalPagos }}</td>
              <td>{{ p.fechaInicio }}</td>
              <td>
                <button @click="verDetalle(p)" class="btn-detail">Ver Pagos / Pagar</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
  
      <div v-else class="detalle-container">
        <button @click="prestamoSeleccionado = null" class="btn-back">← Volver al listado</button>
        
        <div class="info-prestamo-card">
          <div class="info-header">
            <h3>Detalle del Préstamo #{{ prestamoSeleccionado.idPrestamo }}</h3>
            <span class="saldo-big">Saldo Actual: Q{{ prestamoSeleccionado.saldoPendiente.toLocaleString() }}</span>
          </div>
  
          <div class="pago-form">
            <h4>Registrar Nuevo Pago</h4>
            <div class="form-inline">
              <input v-model.number="nuevoPago.montoPago" type="number" placeholder="Monto a pagar (Q)" step="0.01">
              <button @click="registrarPago" class="btn-save">Procesar Pago</button>
            </div>
          </div>
  
          <h4>Historial de Pagos Realizados</h4>
          <table class="data-table">
            <thead>
              <tr>
                <th>Monto Pagado</th>
                <th>Fecha de Pago</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(pago) in historialPagos" :key="pago.idPago">
                <td>Q{{ pago.montoPago.toLocaleString() }}</td>
                <td>{{ pago.fechaPago }}</td>
              </tr>
              <tr v-if="historialPagos.length === 0">
                <td colspan="5" style="text-align: center;">No hay pagos registrados aún.</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import PrestamoService from '@/services/PrestamoService';
  import ClienteService from '@/services/ClienteService';
  
  export default {
    data() {
      return {
        prestamos: [],
        prestamoSeleccionado: null,
        historialPagos: [],
        clientes: [],
        filtroAprobados: '0',
        nuevoPago: {
          monto: null
        }
      };
    },
    mounted() {
      this.cargarClientes();
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
      async cargarPrestamos() {
        try {
          const response = await PrestamoService.getAprobados(this.filtroAprobados);
          this.prestamos = response.data;
        } catch (error) {
          console.error("Error cargando préstamos:", error);
        }
      },
      async verDetalle(prestamo) {
        this.prestamoSeleccionado = prestamo;
        try {
          const response = await PrestamoService.getPagos(prestamo.idPrestamo);
          this.historialPagos = response.data;
        } catch (error) {
          console.error("Error cargando pagos:", error);
        }
      },
      async registrarPago() {
        if (!this.nuevoPago.montoPago || this.nuevoPago.montoPago <= 0) {
          alert("Ingrese un monto válido");
          return;
        }
  
        try {
          await PrestamoService.realizarPago(this.prestamoSeleccionado.idPrestamo, this.nuevoPago);
          alert("Pago registrado con éxito");
          
          await this.verDetalle(this.prestamoSeleccionado);
          await this.cargarPrestamos(); 
          this.nuevoPago.montoPago = null;
        } catch (error) {
          alert("Error al procesar el pago");
        }
      }
    }
  };
  </script>
  
  <style scoped>
  .header-actions { margin-bottom: 20px; border-bottom: 2px solid #eee; padding-bottom: 10px; }
  .text-bold { font-weight: bold; }
  .text-danger { color: #e74c3c; }
  
  .btn-detail { background: #16a085; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; }
  .btn-back { background: #95a5a6; color: white; border: none; padding: 8px 15px; border-radius: 4px; margin-bottom: 15px; cursor: pointer; }
  
  .info-prestamo-card { background: #fff; padding: 20px; border: 1px solid #ddd; border-radius: 8px; }
  .info-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; background: #f9f9f9; padding: 15px; border-radius: 5px; }
  .saldo-big { font-size: 1.2rem; font-weight: bold; color: #2c3e50; }
  
  .pago-form { background: #ebf5fb; padding: 20px; border-radius: 8px; margin-bottom: 30px; border: 1px solid #d4e6f1; }
  .form-inline { display: flex; gap: 10px; margin-top: 10px; }
  .form-inline input { flex: 1; padding: 10px; border: 1px solid #ccc; border-radius: 4px; }
  
  .data-table { width: 100%; border-collapse: collapse; }
  .data-table th, .data-table td { padding: 12px; border-bottom: 1px solid #eee; text-align: left; }
  .btn-save { background: #27ae60; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; }
  .slc { background: white; color: black; border: 1px solid; padding: 8px 15px; border-radius: 4px; cursor: pointer; }
  </style>
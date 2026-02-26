<template>
    <div class="dashboard-layout">
      <aside class="sidebar">
        <div class="logo">
          <h2>CHN Préstamos</h2>
        </div>
        
        <nav class="menu">
          <button 
            :class="{ active: currentView === 'clientes' }" 
            @click="currentView = 'clientes'"
          >
            <i class="icon">👥</i> Gestión de Clientes
          </button>
          
          <button 
            :class="{ active: currentView === 'solicitudes' }" 
            @click="currentView = 'solicitudes'"
          >
            <i class="icon">📄</i> Solicitudes de Préstamos
          </button>
          
          <button 
            :class="{ active: currentView === 'prestamos' }" 
            @click="currentView = 'prestamos'"
          >
            <i class="icon">💰</i> Gestión de Préstamos
          </button>
        </nav>
  
        <div class="sidebar-footer">
          <button @click="logout" class="btn-logout">Cerrar Sesión</button>
        </div>
      </aside>
  
      <main class="main-content">
        <header class="top-bar">
          <h1>{{ viewTitle }}</h1>
        </header>
  
        <section class="view-container">
          <GestionCliente v-if="currentView === 'clientes'" />
          <SolicitudPrestamo v-else-if="currentView === 'solicitudes'" />
          <GestionPrestamo v-else-if="currentView === 'prestamos'" />
        </section>
      </main>
    </div>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'
  import GestionCliente from '@/components/GestionOperador/GestionCliente.vue'
  import SolicitudPrestamo from '@/components/GestionOperador/SolicitudPrestamo.vue'
  import GestionPrestamo from '@/components/GestionOperador/GestionPrestamo.vue'
  
  const currentView = ref('clientes')
  
  const viewTitle = computed(() => {
    const titles = {
      clientes: 'Gestión de Clientes',
      solicitudes: 'Solicitudes de Préstamos',
      prestamos: 'Gestión de Préstamos'
    }
    return titles[currentView.value]
  })
  
  const logout = () => {
    localStorage.removeItem('token')
    window.location.reload()
  }
  </script>
  
  <style scoped>
  .dashboard-layout {
    display: flex;
    background-color: #f4f7f6;
  }
  
  .sidebar {
    width: 260px;
    background-color: #2c3e50;
    color: white;
    display: flex;
    flex-direction: column;
    position: fixed;
    height: 100%;
  }
  
  .logo {
    padding: 20px;
    text-align: center;
    background-color: #1a252f;
  }
  
  .menu {
    flex: 1;
  }
  
  .menu button {
    width: 100%;
    padding: 15px 25px;
    background: none;
    border: none;
    color: #bdc3c7;
    text-align: left;
    cursor: pointer;
    font-size: 16px;
    transition: 0.3s;
  }
  
  .menu button:hover, .menu button.active {
    background-color: #34495e;
    color: white;
    border-left: 4px solid #3498db;
  }
  
  .main-content {
    margin-left: 260px; 
    flex: 1;
    display: flex;
    flex-direction: column;
  }
  
  .top-bar {
    background-color: white;
    padding: 15px 30px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  }
  
  .view-container {
    padding: 30px;
    overflow-y: auto;
  }
  
  .btn-logout {
    width: 100%;
    padding: 25px;
    background-color: #e74c3c;
    color: white;
    border: none;
    cursor: pointer;
  }
  </style>
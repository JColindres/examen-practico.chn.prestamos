<template>
  <div id="app">
    <LoginRegistro 
      v-if="currentPage === 'login'" 
      @login-op-success="navegarA('dashboardoperador')" 
    />
    
    <DashBoardOperador 
      v-else-if="currentPage === 'dashboardoperador'" 
      @logout="navegarA('login')"
    />

    <div v-else>
      <h2>Página no encontrada</h2>
      <button @click="navegarA('login')">Ir al Login</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import LoginRegistro from './components/Inicio/LoginRegistro.vue'
import DashBoardOperador from './components/DashBoardOperador.vue'

const currentPage = ref('login')

const navegarA = (page) => {
  currentPage.value = page
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
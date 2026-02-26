<template>
    <div class="auth-container">
      <div class="auth-card">
        <header class="auth-header">
          <h1>{{ isLogin ? 'Iniciar Sesión' : 'Crear Cuenta' }}</h1>
          <p>{{ isLogin ? 'Bienvenido de nuevo' : 'Regístrate para comenzar' }}</p>
        </header>
  
        <form @submit.prevent="handleAuth" class="auth-form">
          <div class="form-group">
            <label>Usuario</label>
            <input v-model="user.username" type="text" placeholder="Nombre de usuario" required />
          </div>
  
          <div class="form-group">
            <label>Contraseña</label>
            <input v-model="user.password" type="password" placeholder="••••••••" required />
          </div>

          <div class="form-group">
            <label>Rol</label>
            <select v-model="user.rol" required>
                <option value="" disabled selected>Selecciona un rol</option>
                <!-- <option value="CLIENTE">Cliente</option> -->
                <option value="OPERADOR">Operador</option>
            </select>
          </div>
  
          <div v-if="errorMessage" class="error-badge">{{ errorMessage }}</div>
  
            <button :disabled="loading" @click="handleAuth">
                {{ loading ? 'Procesando...' : (isLogin ? 'Entrar' : 'Registrarse') }}
            </button>
        </form>
  
        <footer class="auth-footer">
          <button @click="isLogin = !isLogin" class="btn-link">
            {{ isLogin ? '¿No tienes cuenta? Regístrate aquí' : '¿Ya tienes cuenta? Inicia sesión' }}
          </button>
        </footer>
      </div>
    </div>
  </template>
  
<script setup>
import { ref, reactive, defineEmits } from 'vue'
import AuthService from '@/services/AuthService'

const emit = defineEmits(['login-op-success', 'login-cli-success'])
const isLogin = ref(true)
const loading = ref(false)
const errorMessage = ref('')
const user = reactive({
  username: '',
  rol: '', 
  password: ''
})

const handleAuth = async () => {
  loading.value = true
  errorMessage.value = ''
  
  try {
    const request = {
        username: user.username,
        password: user.password,
        rol: user.rol
    }
    if (isLogin.value) {
        
      const response = await AuthService.login(user)
      console.log('Login exitoso:', response.data)
      
      localStorage.setItem('token', response.data.jwt);

      if(response.data.rol === 'CLIENTE'){
        emit('login-cli-success');
      } else {
        emit('login-op-success');
      }
    } else {
      console.log('Envio: ', request)
      const response = await AuthService.registrar(request)
      console.log('Registro exitoso:', response.data)
      isLogin.value = true
    }
  } catch (error) {
    console.error("Error en la petición:", error)
    errorMessage.value = error.response?.data || 'Ocurrió un error inesperado'
  } finally {
    loading.value = false
  }
}
</script>

  
  <style scoped>
  :emphasis {
    --primary-color: #4f46e5;
    --primary-hover: #4338ca;
    --bg-color: #f3f4f6;
    --text-main: #1f2937;
    --text-muted: #6b7280;
    --white: #ffffff;
  }
  
  .auth-container {
    display: flex;
    align-items: center;
    justify-content: center;
    min-height: 100vh;
    background-color: var(--bg-color);
    font-family: sans-serif;
  }
  
  .auth-card {
    background: var(--white);
    padding: 2.5rem;
    border-radius: 12px;
    box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 400px;
  }
  
  .auth-header {
    text-align: center;
    margin-bottom: 2rem;
  }
  
  .auth-header h1 {
    margin: 0;
    color: var(--text-main);
    font-size: 1.8rem;
  }
  
  .auth-header p {
    color: var(--text-muted);
    margin-top: 0.5rem;
  }
  
  .auth-form {
    display: flex;
    flex-direction: column;
    gap: 1.2rem;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
    gap: 0.4rem;
  }
  
  .form-group label {
    font-size: 0.9rem;
    font-weight: 600;
    color: var(--text-main);
  }
  
  .form-group input {
    padding: 0.75rem;
    border: 1px solid #d1d5db;
    border-radius: 6px;
    font-size: 1rem;
    transition: border-color 0.2s;
  }
  
  .form-group input:focus {
    outline: none;
    border-color: var(--primary-color);
    box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
  }
  
  .btn-primary {
    background-color: gray;
    color: white;
    padding: 0.75rem;
    border: none;
    border-radius: 6px;
    font-size: 1rem;
    font-weight: 600;
    cursor: pointer;
    transition: background 0.2s;
    margin-top: 1rem;
  }
  
  .btn-primary:hover {
    background-color: var(--primary-hover);
  }
  
  .auth-footer {
    margin-top: 1.5rem;
    text-align: center;
  }
  
  .btn-link {
    background: none;
    border: none;
    color: var(--primary-color);
    font-size: 0.875rem;
    cursor: pointer;
    text-decoration: underline;
  }
  
  .btn-link:hover {
    color: var(--primary-hover);
  }
  </style>
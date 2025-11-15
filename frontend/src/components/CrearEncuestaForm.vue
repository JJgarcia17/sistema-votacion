<script setup>
import { ref } from 'vue';

// --- 1. Estado Interno del Formulario ---
const titulo = ref('');
// Empezamos con dos opciones por defecto
const opciones = ref(['', '']); 

// Definimos el evento que este componente "emitirá" al padre
const emit = defineEmits(['encuestaCreada']);

// --- 2. Lógica del Formulario ---

// Añade un nuevo campo de opción vacío
function agregarOpcion() {
  opciones.value.push('');
}

// Elimina un campo de opción por su índice
function quitarOpcion(index) {
  // Nos aseguramos de que siempre queden al menos 2 opciones
  if (opciones.value.length > 2) {
    opciones.value.splice(index, 1);
  }
}

// Se llama al enviar el formulario
function enviarFormulario() {
  // 1. Validamos que el título no esté vacío
  if (!titulo.value.trim()) {
    alert('Por favor, ingresa un título para la encuesta.');
    return;
  }

  // 2. Filtramos opciones vacías y las convertimos al formato que espera la API
  const opcionesPayload = opciones.value
    .map(texto => texto.trim()) // Quitamos espacios en blanco
    .filter(texto => texto.length > 0) // Filtramos las que quedaron vacías
    .map(texto => ({ textoOpcion: texto })); // Las convertimos al objeto esperado

  // 3. Validamos que haya al menos 2 opciones válidas
  if (opcionesPayload.length < 2) {
    alert('Debes ingresar al menos dos opciones válidas.');
    return;
  }

  // 4. Creamos el objeto final (payload)
  const payload = {
    titulo: titulo.value,
    opciones: opcionesPayload
  };

  // 5. Emitimos el evento al padre (App.vue) con los datos
  emit('encuestaCreada', payload);

  // 6. Reseteamos el formulario
  titulo.value = '';
  opciones.value = ['', ''];
}
</script>

<template>
  <form @submit.prevent="enviarFormulario" class="form-crear-encuesta">
    <h3>Crear Nueva Encuesta</h3>
    
    <div class="form-group">
      <label for="titulo">Título de la Encuesta:</label>
      <input 
        type="text" 
        id="titulo" 
        v-model="titulo" 
        placeholder="Ej: ¿Cuál es tu color favorito?"
        required
      />
    </div>

    <div class="form-group" v-for="(opcion, index) in opciones" :key="index">
      <label :for="`opcion-${index}`">Opción {{ index + 1 }}:</label>
      <div class="opcion-input">
        <input 
          type="text" 
          :id="`opcion-${index}`" 
          v-model="opciones[index]"
          placeholder="Texto de la opción"
        />
        <button 
          type="button" 
          @click="quitarOpcion(index)" 
          v-if="opciones.length > 2"
          class="btn-quitar"
        >
          &times;
        </button>
      </div>
    </div>

    <div class="form-actions">
      <button type="button" @click="agregarOpcion" class="btn-secundario">
        Añadir Opción
      </button>
      <button type="submit" class="btn-principal">
        Guardar Encuesta
      </button>
    </div>
  </form>
</template>

<style scoped>
/* --- 4. Estilos (Scoped) --- */
.form-crear-encuesta {
  background-color: #fff;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 2rem;
  border: 1px solid #e0e0e0;
}

.form-crear-encuesta h3 {
  margin-top: 0;
  text-align: center;
}

.form-group {
  margin-bottom: 1rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
}

.form-group input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-sizing: border-box; /* Asegura que el padding no afecte el ancho */
}

.opcion-input {
  display: flex;
  align-items: center;
}

.opcion-input input {
  flex-grow: 1;
}

.btn-quitar {
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 50%;
  width: 28px;
  height: 28px;
  margin-left: 0.5rem;
  cursor: pointer;
  font-size: 1.2rem;
  line-height: 1;
  padding: 0;
}
.btn-quitar:hover {
  background-color: #c0392b;
}

.form-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 1.5rem;
}

.btn-principal, .btn-secundario {
  border: none;
  border-radius: 5px;
  padding: 10px 15px;
  cursor: pointer;
  font-weight: bold;
}

.btn-principal {
  background-color: #28a745;
  color: white;
}
.btn-principal:hover {
  background-color: #218838;
}

.btn-secundario {
  background-color: #6c757d;
  color: white;
}
.btn-secundario:hover {
  background-color: #5a6268;
}
</style>
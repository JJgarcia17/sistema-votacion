<script setup>
import { ref, computed } from 'vue'

// --- 1. Estado Interno del Formulario ---
const titulo = ref('')
// Empezamos con dos opciones por defecto
const opciones = ref(['', ''])

// Definimos el evento que este componente "emitirá" al padre
const emit = defineEmits(['encuestaCreada', 'cancel'])

const opcionesValidas = computed(() => opciones.value.map(t => t.trim()).filter(Boolean).length)
const formularioValido = computed(() => titulo.value.trim().length > 0 && opcionesValidas.value >= 2)

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
  <form @submit.prevent="enviarFormulario" class="card-surface p-6 relative">
    <button type="button" @click="emit('cancel')" aria-label="Cerrar" class="absolute top-3 right-3 inline-flex items-center justify-center w-8 h-8 rounded-full bg-gray-100 hover:bg-gray-200">✕</button>
    <h3 class="text-lg font-semibold mb-2 text-primary-700 text-center">Crear Nueva Encuesta</h3>
    <p class="text-xs text-gray-500 text-center mb-4">Agrega al menos 2 opciones. Opciones válidas: {{ opcionesValidas }}</p>

    <div class="mb-4">
      <label for="titulo" class="block text-sm font-medium text-gray-700 mb-1">Título de la Encuesta:</label>
      <input
        type="text"
        id="titulo"
        v-model="titulo"
        placeholder="Ej: ¿Cuál es tu color favorito?"
        required
        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:ring-primary-500 focus:border-primary-500"
      />
    </div>

    <div v-for="(opcion, index) in opciones" :key="index" class="mb-3">
      <label :for="`opcion-${index}`" class="block text-sm font-medium text-gray-700 mb-1">Opción {{ index + 1 }}:</label>
      <div class="flex gap-2 items-center">
        <input
          type="text"
          :id="`opcion-${index}`"
          v-model="opciones[index]"
          placeholder="Texto de la opción"
          class="flex-1 rounded-md border-gray-300 shadow-sm focus:ring-primary-500 focus:border-primary-500"
        />
        <button
          type="button"
          @click="quitarOpcion(index)"
          v-if="opciones.length > 2"
          class="inline-flex items-center justify-center w-8 h-8 rounded-full bg-danger text-white hover:bg-red-600"
        >
          &times;
        </button>
      </div>
    </div>

    <div class="flex justify-between mt-4 items-center">
      <button type="button" @click="agregarOpcion" class="px-3 py-2 rounded-md bg-gray-100 text-gray-800 border border-gray-200 hover:bg-gray-200">Añadir Opción</button>
      <button type="submit" :disabled="!formularioValido" :class="formularioValido ? 'btn-primary' : 'px-4 py-2 rounded-md bg-gray-200 text-gray-400 cursor-not-allowed'">Guardar Encuesta</button>
    </div>
  </form>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  encuesta: { type: Object, required: true }
})
const emit = defineEmits(['votoRealizado'])

const totalVotos = computed(() => props.encuesta.opciones.reduce((s, o) => s + (o.contadorVotos || 0), 0))

function porcentaje(opcion) {
  if (totalVotos.value === 0) return 0
  return Math.round(((opcion.contadorVotos || 0) / totalVotos.value) * 100)
}

function votar(opcionId) {
  emit('votoRealizado', opcionId)
}
</script>

<template>
  <article class="card-surface p-4 card-appear">
    <h3 class="text-md font-semibold text-primary-700 mb-3">{{ props.encuesta.titulo }}</h3>

    <ul>
      <li v-for="opcion in props.encuesta.opciones" :key="opcion.id" class="py-3">
        <div class="flex items-center justify-between mb-2">
          <span class="text-gray-800 font-medium">{{ opcion.textoOpcion }}</span>
          <div class="flex items-center gap-3">
            <small class="text-sm font-semibold text-gray-600">{{ opcion.contadorVotos ?? 0 }}</small>
            <button @click="votar(opcion.id)" class="px-3 py-1 rounded-md bg-primary-600 text-white hover:bg-primary-700">Votar</button>
          </div>
        </div>

        <div class="w-full bg-gray-100 h-3 rounded overflow-hidden">
          <div class="h-3 bg-gradient-to-r from-primary-500 to-accent" :style="{ width: porcentaje(opcion) + '%' }"></div>
        </div>
        <div class="flex justify-between text-xs text-gray-500 mt-1">
          <span>{{ porcentaje(opcion) }}%</span>
          <span>Total: {{ totalVotos }}</span>
        </div>
      </li>
    </ul>
  </article>
</template>

<style scoped>
/* --- 4. Estilos (CSS) ---
   La palabra 'scoped' significa que estos estilos SÓLO se aplican
   a este componente (EncuestaCard.vue) y no afectarán al resto de la app.
   Esto es crucial para la mantenibilidad. */

.encuesta-card {
  background-color: #ffffff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.encuesta-card h3 {
  margin-top: 0;
  color: #0056b3;
}

.encuesta-card ul {
  list-style-type: none;
  padding: 0;
}

.encuesta-card li {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0.75rem 0;
  border-bottom: 1px solid #eee;
}

.encuesta-card li:last-child {
  border-bottom: none;
}

.encuesta-card button {
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 8px 12px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.2s ease;
}

.encuesta-card button:hover {
  background-color: #0056b3;
}

.encuesta-card small {
  font-weight: bold;
  color: #555;
  min-width: 70px;
  text-align: right;
}
</style>
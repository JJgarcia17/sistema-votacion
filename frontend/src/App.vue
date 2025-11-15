<script setup>
import { ref, onMounted } from 'vue'
import apiService from './services/apiService'
import EncuestaCard from './components/EncuestaCard.vue'
import CrearEncuestaForm from './components/CrearEncuestaForm.vue'

const encuestas = ref([])
const cargando = ref(true)
const errorCarga = ref(null)
const mostrarForm = ref(false)

async function cargarEncuestas(reintentos = 3) {
  cargando.value = true
  errorCarga.value = null

  for (let intento = 1; intento <= reintentos; intento++) {
    try {
      const response = await apiService.getEncuestas()
      const d = response.data

      // Manejo robusto de distintas formas de respuesta
      if (Array.isArray(d)) {
        encuestas.value = d
      } else if (d && Array.isArray(d.content)) {
        encuestas.value = d.content
      } else if (d && Array.isArray(d.encuestas)) {
        encuestas.value = d.encuestas
      } else {
        // Si la forma no es la esperada, lo registramos y vaciamos
        console.warn('Respuesta inesperada al cargar encuestas:', d)
        encuestas.value = []
      }

      // éxito -> salimos
      errorCarga.value = null
      break
    } catch (error) {
      console.error(`Error en intento ${intento} al cargar encuestas:`, error)
      errorCarga.value = 'Error al cargar encuestas. Reintentando...'
      // espera breve antes de reintentar (backoff simple)
      await new Promise(r => setTimeout(r, 500 * intento))
    }
  }

  cargando.value = false
}

onMounted(() => {
  cargarEncuestas()
})

async function handleVoto(opcionId) {
  try {
    const response = await apiService.votarPorOpcion(opcionId)
    const opcionVotada = response.data

    for (const encuesta of encuestas.value) {
      const opcion = encuesta.opciones.find(o => o.id === opcionId)
      if (opcion) {
        opcion.contadorVotos = opcionVotada.contadorVotos
        break
      }
    }
  } catch (error) {
    console.error('Error al registrar el voto:', error)
  }
}

async function handleCrearEncuesta(payload) {
  try {
    const response = await apiService.crearEncuesta(payload)
    encuestas.value.push(response.data)
    mostrarForm.value = false
  } catch (error) {
    console.error('Error al crear la encuesta:', error)
    alert('Hubo un error al crear la encuesta. Intenta de nuevo.')
  }
}

function toggleForm() {
  mostrarForm.value = !mostrarForm.value
}
</script>

<template>
  <div class="container mx-auto mt-8">
    <header class="flex items-center justify-between mb-6">
      <h1 class="text-2xl font-extrabold text-primary-700">🗳️ Sistema de Votación</h1>
      <div>
        <button
          @click="toggleForm"
          :class="mostrarForm ? 'bg-primary-600 text-white' : 'bg-white text-primary-600 border border-primary-600'"
          class="px-4 py-2 rounded-md font-semibold shadow-sm transition-colors"
        >
          {{ mostrarForm ? 'Cerrar' : 'Crear Encuesta' }}
        </button>
      </div>
    </header>

    <main>
      <div v-if="errorCarga" class="mb-4 text-red-600 font-medium">{{ errorCarga }}</div>
      <transition name="fade">
        <div v-if="mostrarForm" class="fixed inset-0 z-50 flex items-start justify-center py-12 overflow-auto">
          <div class="fixed inset-0 bg-black/50"></div>
          <div class="relative z-50 w-full max-w-2xl mx-4 max-h-[90vh] overflow-auto">
            <CrearEncuestaForm @encuestaCreada="handleCrearEncuesta" @cancel="toggleForm" />
          </div>
        </div>
      </transition>

      <section>
        <h2 class="text-lg font-semibold mb-4">Encuestas Disponibles</h2>

        <div v-if="cargando" class="text-gray-600">Cargando encuestas...</div>

        <div v-else>
          <p v-if="encuestas.length === 0" class="text-gray-600">No hay encuestas disponibles. ¡Crea la primera!</p>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-6 mt-4">
            <EncuestaCard
              v-for="encuesta in encuestas"
              :key="encuesta.id"
              :encuesta="encuesta"
              @votoRealizado="handleVoto"
            />
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<style>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0
}

/* Estilo para el separador (no usado actualmente) */
.separador {
  border: none;
  border-top: 1px solid #eee;
  margin: 2.5rem 0;
}
</style>
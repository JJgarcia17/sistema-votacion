<script setup>
// --- 1. Importaciones ---
import { ref, onMounted } from 'vue'; 
import apiService from './services/apiService';
import EncuestaCard from './components/EncuestaCard.vue'; 
// ¡Importamos nuestro nuevo formulario!
import CrearEncuestaForm from './components/CrearEncuestaForm.vue'; 

// --- 2. Lógica (El "Cerebro") ---

const encuestas = ref([]); 
const cargando = ref(true); 

// Lógica de Carga (Sin cambios)
onMounted(async () => {
  try {
    const response = await apiService.getEncuestas();
    encuestas.value = response.data;
  } catch (error) {
    console.error('Error al cargar las encuestas:', error);
  } finally {
    cargando.value = false;
  }
});

// Lógica de Votación (Sin cambios)
async function handleVoto(opcionId) {
  try {
    const response = await apiService.votarPorOpcion(opcionId);
    const opcionVotada = response.data;

    for (const encuesta of encuestas.value) {
      const opcion = encuesta.opciones.find(o => o.id === opcionId);
      if (opcion) {
        opcion.contadorVotos = opcionVotada.contadorVotos;
        break; 
      }
    }
  } catch (error) {
    console.error('Error al registrar el voto:', error);
  }
}

// --- NUEVA LÓGICA DE CREACIÓN ---
// Esta función se activa cuando el formulario emite el evento 'encuestaCreada'
async function handleCrearEncuesta(payload) {
  try {
    // 1. Llamamos a la API con los datos del formulario
    const response = await apiService.crearEncuesta(payload);
    
    // 2. Añadimos la nueva encuesta a nuestra lista reactiva
    // La UI se actualizará automáticamente ¡sin recargar la página!
    encuestas.value.push(response.data);
    
  } catch (error) {
    console.error('Error al crear la encuesta:', error);
    alert('Hubo un error al crear la encuesta. Intenta de nuevo.');
  }
}
</script>

<template>
  <header>
    <h1>🗳️ Sistema de Votación Simple</h1>
  </header>

  <main>
    <CrearEncuestaForm @encuestaCreada="handleCrearEncuesta" />

    <hr class="separador">

    <h2>Encuestas Disponibles</h2>
    
    <div v-if="cargando">
      <p>Cargando encuestas...</p>
    </div>
    
    <div v-else class="lista-encuestas">
      <p v-if="encuestas.length === 0">
        No hay encuestas disponibles. ¡Crea la primera!
      </p>
      
      <EncuestaCard
        v-for="encuesta in encuestas"
        :key="encuesta.id"
        :encuesta="encuesta" 
        @votoRealizado="handleVoto" 
      />
    </div>
  </main>
</template>

<style>
/* --- 4. Estilos (Globales) --- */
body {
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  background-color: #f4f7f6;
  color: #333;
  margin: 0;
  padding: 2rem;
}

header {
  text-align: center;
  border-bottom: 2px solid #ddd;
  padding-bottom: 1rem;
}

main {
  max-width: 800px;
  margin: 2rem auto;
}

/* Estilo para el separador */
.separador {
  border: none;
  border-top: 1px solid #eee;
  margin: 2.5rem 0;
}
</style>
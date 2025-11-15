<script setup>
// --- 1. Importaciones ---
import { ref, onMounted } from 'vue'; 
import apiService from './services/apiService';
// ¡Importamos nuestro nuevo componente!
import EncuestaCard from './components/EncuestaCard.vue'; 

// --- 2. Lógica (El "Cerebro") ---

// Estado reactivo: la lista de encuestas
const encuestas = ref([]); 
const cargando = ref(true); // Estado para saber si estamos cargando

// Lógica de Carga: Se ejecuta al montar el componente
onMounted(async () => {
  try {
    const response = await apiService.getEncuestas();
    encuestas.value = response.data;
  } catch (error) {
    console.error('Error al cargar las encuestas:', error);
  } finally {
    cargando.value = false; // Terminamos de cargar
  }
});

// Lógica de Votación: Esta función la llamará el evento del hijo
async function handleVoto(opcionId) {
  try {
    const response = await apiService.votarPorOpcion(opcionId);
    const opcionVotada = response.data;

    // Actualiza el estado local para reflejar el voto
    // (Esta lógica se queda aquí, en el "cerebro")
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
</script>

<template>
  <header>
    <h1>🗳️ Sistema de Votación Simple</h1>
  </header>

  <main>
    <h2>Encuestas Disponibles</h2>
    
    <div v-if="cargando">
      <p>Cargando encuestas...</p>
    </div>
    
    <div v-else class="lista-encuestas">
      
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
/* --- 4. Estilos (CSS) ---
   Estos son estilos GLOBALES.
   Los estilos específicos de la tarjeta ya están en EncuestaCard.vue (scoped). */
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
</style>
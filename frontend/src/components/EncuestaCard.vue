<script setup>
// --- 1. Definición de Contrato ---

// 'defineProps' le dice a Vue qué datos (props) espera este componente de su padre.
// Espera un objeto 'encuesta' que es obligatorio.
defineProps({
  encuesta: {
    type: Object,
    required: true
  }
});

// 'defineEmits' define los eventos que este componente puede "emitir" (enviar) a su padre.
// Enviaremos un evento llamado 'votoRealizado' con el ID de la opción.
const emit = defineEmits(['votoRealizado']);

// --- 2. Lógica Local ---

// Esta función se llama cuando se hace clic en un botón de votar.
// Su única responsabilidad es emitir el evento al padre.
function votar(opcionId) {
  emit('votoRealizado', opcionId);
}
</script>

<template>
  <article class="encuesta-card">
    
    <h3>{{ encuesta.titulo }}</h3>
    
    <ul>
      <li v-for="opcion in encuesta.opciones" :key="opcion.id">
        
        <span>{{ opcion.textoOpcion }}</span>
        
        <button @click="votar(opcion.id)">
          Votar
        </button>
        
        <small>Votos: {{ opcion.contadorVotos }}</small>
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
import axios from 'axios';

// Creamos una instancia de axios.
// Base URL configurable via Vite env var `VITE_API_BASE_URL`.
// En desarrollo puede ser 'http://localhost:8080/api'.
// En Docker: usa el nombre del servicio backend (p. ej. 'http://backend:8080/api') o una URL absoluta.
const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'

const apiClient = axios.create({
    baseURL,
    headers: {
        'Content-Type': 'application/json'
    }
});

// Exportamos los métodos que nuestro frontend usará
export default {
    // GET /api/encuestas
    getEncuestas() {
        return apiClient.get('/encuestas');
    },

    // POST /api/encuestas
    crearEncuesta(encuestaData) {
        return apiClient.post('/encuestas', encuestaData);
    },

    // POST /api/opciones/{id}/votar
    votarPorOpcion(opcionId) {
        return apiClient.post(`/opciones/${opcionId}/votar`);
    }
};
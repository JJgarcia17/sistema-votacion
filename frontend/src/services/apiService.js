import axios from 'axios';

// Creamos una instancia de axios.
// NOTA: 'http://localhost:8080/api' es la URL de nuestro backend.
const apiClient = axios.create({
    baseURL: 'http://localhost:8080/api',
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
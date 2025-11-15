# ----- Etapa 1: Build (Construcción) -----
# Usamos una imagen ligera de Node.js (Alpine es ideal)
FROM node:18-alpine AS builder

# Establecemos el directorio de trabajo
WORKDIR /app

# Copiamos primero el package.json y el package-lock.json
# Esto aprovecha la caché de Docker. Si no cambian, no reinstala todo.
COPY package*.json ./
RUN npm install

# Copiamos el resto del código fuente del frontend
COPY . .

# Ejecutamos el script de 'build' de Vue (definido en package.json)
# Esto compilará todo nuestro código Vue/JS en archivos estáticos (HTML, CSS, JS)
# en la carpeta '/dist'
RUN npm run build

# ----- Etapa 2: Serve (Servicio) -----
# Usamos una imagen de Nginx (un servidor web) súper ligera
FROM nginx:1.25-alpine

# Copiamos los archivos estáticos construidos en la etapa 'builder'
# desde la carpeta '/app/dist' del builder...
# ...hacia la carpeta '/usr/share/nginx/html' del contenedor Nginx.
# Esta es la carpeta por defecto donde Nginx sirve archivos.
COPY --from=builder /app/dist /usr/share/nginx/html

# (Opcional pero recomendado) Copiamos una configuración personalizada de Nginx
# para asegurarnos de que nuestra SPA (Single Page Application) funcione bien.
# Crearemos este archivo 'nginx.conf' a continuación.
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Exponemos el puerto 80, que es el puerto estándar de Nginx
EXPOSE 80
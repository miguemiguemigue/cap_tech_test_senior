# Usa la imagen oficial de MongoDB
FROM mongo:latest

# Copia el script de inicialización al contenedor
COPY mongo-init.js /docker-entrypoint-initdb.d/

# Establece las variables de entorno para el usuario y la contraseña
ENV MONGO_INITDB_ROOT_USERNAME=root
ENV MONGO_INITDB_ROOT_PASSWORD=12345678
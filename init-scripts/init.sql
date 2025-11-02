CREATE DATABASE IF NOT EXISTS auth_service;
CREATE DATABASE IF NOT EXISTS administration_service;
CREATE DATABASE IF NOT EXISTS mascotas_service;
CREATE DATABASE IF NOT EXISTS citas_service;
CREATE DATABASE IF NOT EXISTS historias_service;
CREATE DATABASE IF NOT EXISTS inventario_service;
CREATE DATABASE IF NOT EXISTS facturas_service;

GRANT ALL PRIVILEGES ON `auth_service`.* TO 'CuervoAdmin'@'%';
GRANT ALL PRIVILEGES ON `administration_service`.* TO 'CuervoAdmin'@'%';
GRANT ALL PRIVILEGES ON `mascotas_service`.* TO 'CuervoAdmin'@'%';
GRANT ALL PRIVILEGES ON `citas_service`.* TO 'CuervoAdmin'@'%';
GRANT ALL PRIVILEGES ON `historias_service`.* TO 'CuervoAdmin'@'%';
GRANT ALL PRIVILEGES ON `inventario_service`.* TO 'CuervoAdmin'@'%';
GRANT ALL PRIVILEGES ON `facturas_service`.* TO 'CuervoAdmin'@'%';

-- Asegurar que el usuario CuervoAdmin exista y use caching_sha2_password (MySQL 8)
CREATE USER IF NOT EXISTS 'CuervoAdmin'@'%' IDENTIFIED WITH caching_sha2_password BY '${DB_PASSWORD}';
-- Si el usuario existía con otro plugin, forzar alteración al plugin recomendado
ALTER USER 'CuervoAdmin'@'%' IDENTIFIED WITH caching_sha2_password BY '${DB_PASSWORD}';

FLUSH PRIVILEGES;
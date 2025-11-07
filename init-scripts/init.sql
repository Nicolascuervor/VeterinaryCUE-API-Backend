CREATE DATABASE IF NOT EXISTS auth_service;
CREATE DATABASE IF NOT EXISTS administration_service;
CREATE DATABASE IF NOT EXISTS mascotas_service;
CREATE DATABASE IF NOT EXISTS citas_service;
CREATE DATABASE IF NOT EXISTS historias_service;
CREATE DATABASE IF NOT EXISTS inventario_service;
CREATE DATABASE IF NOT EXISTS facturas_service;

-- Crear el usuario con mysql_native_password (compatible con el conector)
CREATE USER IF NOT EXISTS 'Scabezas'@'%' IDENTIFIED WITH mysql_native_password BY '371211';

-- Otorgar permisos
GRANT ALL PRIVILEGES ON auth_service.* TO 'Scabezas'@'%';
GRANT ALL PRIVILEGES ON administration_service.* TO 'Scabezas'@'%';
GRANT ALL PRIVILEGES ON mascotas_service.* TO 'Scabezas'@'%';
GRANT ALL PRIVILEGES ON citas_service.* TO 'Scabezas'@'%';
GRANT ALL PRIVILEGES ON historias_service.* TO 'Scabezas'@'%';
GRANT ALL PRIVILEGES ON inventario_service.* TO 'Scabezas'@'%';
GRANT ALL PRIVILEGES ON facturas_service.* TO 'Scabezas'@'%';

FLUSH PRIVILEGES;
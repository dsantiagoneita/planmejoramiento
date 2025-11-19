# Guía de Despliegue - Sistema de Citas Barbería Neita

Este documento proporciona instrucciones detalladas para desplegar el sistema en diferentes entornos.

## Requisitos Previos

- JDK 21 instalado
- Maven 3.6+ instalado
- MySQL 8.0+ instalado y ejecutándose
- Acceso a la base de datos MySQL con privilegios de administrador

## Despliegue en Entorno Local

### 1. Preparar la Base de Datos

```sql
-- Conectarse a MySQL como root
mysql -u root -p

-- La base de datos se creará automáticamente al ejecutar la aplicación
-- Si deseas crearla manualmente:
CREATE DATABASE IF NOT EXISTS Barberia_Neita CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 2. Configurar la Aplicación

Edita el archivo `src/main/resources/application.properties` si necesitas cambiar las credenciales de la base de datos:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/Barberia_Neita?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=America/Bogota
spring.datasource.username=root
spring.datasource.password=root
```

### 3. Compilar el Proyecto

```bash
cd sistema-citas
mvn clean package -DskipTests
```

### 4. Ejecutar la Aplicación

```bash
java -jar target/sistema-citas-0.0.1-SNAPSHOT.jar
```

O usando Maven:

```bash
mvn spring-boot:run
```

### 5. Verificar el Despliegue

Abre tu navegador y accede a:

```
http://localhost:8080
```

Inicia sesión con las credenciales por defecto:
- Email: admin@barberia.com
- Contraseña: admin123

## Despliegue en Servidor Linux

### 1. Preparar el Servidor

```bash
# Actualizar el sistema
sudo apt update && sudo apt upgrade -y

# Instalar JDK 21
sudo apt install openjdk-21-jdk -y

# Verificar instalación
java -version

# Instalar MySQL
sudo apt install mysql-server -y

# Configurar MySQL
sudo mysql_secure_installation
```

### 2. Configurar MySQL

```bash
# Conectarse a MySQL
sudo mysql -u root -p

# Crear base de datos
CREATE DATABASE Barberia_Neita CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Crear usuario (opcional, para mayor seguridad)
CREATE USER 'barberia_user'@'localhost' IDENTIFIED BY 'tu_contraseña_segura';
GRANT ALL PRIVILEGES ON Barberia_Neita.* TO 'barberia_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 3. Transferir el Proyecto

```bash
# Clonar desde GitHub
git clone https://github.com/danieloez314-arch/sistema-citas.git
cd sistema-citas

# O transferir el JAR compilado
scp target/sistema-citas-0.0.1-SNAPSHOT.jar usuario@servidor:/opt/sistema-citas/
```

### 4. Configurar como Servicio Systemd

Crear archivo de servicio:

```bash
sudo nano /etc/systemd/system/sistema-citas.service
```

Contenido del archivo:

```ini
[Unit]
Description=Sistema de Citas Barbería Neita
After=syslog.target network.target mysql.service

[Service]
User=ubuntu
Type=simple
WorkingDirectory=/opt/sistema-citas
ExecStart=/usr/bin/java -jar /opt/sistema-citas/sistema-citas-0.0.1-SNAPSHOT.jar
Restart=on-failure
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=sistema-citas

[Install]
WantedBy=multi-user.target
```

Habilitar y ejecutar el servicio:

```bash
# Recargar systemd
sudo systemctl daemon-reload

# Habilitar el servicio para que inicie con el sistema
sudo systemctl enable sistema-citas

# Iniciar el servicio
sudo systemctl start sistema-citas

# Verificar el estado
sudo systemctl status sistema-citas

# Ver logs
sudo journalctl -u sistema-citas -f
```

### 5. Configurar Nginx como Proxy Inverso (Opcional)

```bash
# Instalar Nginx
sudo apt install nginx -y

# Crear configuración
sudo nano /etc/nginx/sites-available/sistema-citas
```

Contenido del archivo:

```nginx
server {
    listen 80;
    server_name tu-dominio.com;

    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

Activar la configuración:

```bash
# Crear enlace simbólico
sudo ln -s /etc/nginx/sites-available/sistema-citas /etc/nginx/sites-enabled/

# Probar configuración
sudo nginx -t

# Reiniciar Nginx
sudo systemctl restart nginx
```

### 6. Configurar SSL con Let's Encrypt (Opcional)

```bash
# Instalar Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obtener certificado SSL
sudo certbot --nginx -d tu-dominio.com

# Renovación automática (ya configurada por defecto)
sudo certbot renew --dry-run
```

## Despliegue en Docker

### 1. Crear Dockerfile

```dockerfile
FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY target/sistema-citas-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### 2. Crear docker-compose.yml

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: barberia-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: Barberia_Neita
    ports:
      - "3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - barberia-network

  app:
    build: .
    container_name: barberia-app
    depends_on:
      - mysql
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/Barberia_Neita?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=America/Bogota
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    networks:
      - barberia-network

networks:
  barberia-network:
    driver: bridge

volumes:
  mysql-data:
```

### 3. Ejecutar con Docker Compose

```bash
# Compilar el proyecto
mvn clean package -DskipTests

# Construir y ejecutar contenedores
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener contenedores
docker-compose down
```

## Configuración de Producción

### Variables de Entorno Recomendadas

```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/Barberia_Neita
export SPRING_DATASOURCE_USERNAME=barberia_user
export SPRING_DATASOURCE_PASSWORD=contraseña_segura
export SERVER_PORT=8080
```

### Archivo application-prod.properties

Crear `src/main/resources/application-prod.properties`:

```properties
# Configuración de producción
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# Logging
logging.level.root=INFO
logging.level.com.neita.sistemacitas=INFO
logging.file.name=/var/log/sistema-citas/application.log

# Seguridad
server.error.include-message=never
server.error.include-stacktrace=never
```

## Mantenimiento

### Backup de Base de Datos

```bash
# Backup manual
mysqldump -u root -p Barberia_Neita > backup_$(date +%Y%m%d_%H%M%S).sql

# Restaurar backup
mysql -u root -p Barberia_Neita < backup_20250118_100000.sql

# Backup automático con cron
# Editar crontab
crontab -e

# Agregar línea para backup diario a las 2 AM
0 2 * * * /usr/bin/mysqldump -u root -p'tu_password' Barberia_Neita > /backups/barberia_$(date +\%Y\%m\%d).sql
```

### Actualización de la Aplicación

```bash
# Detener el servicio
sudo systemctl stop sistema-citas

# Hacer backup de la base de datos
mysqldump -u root -p Barberia_Neita > backup_antes_actualizacion.sql

# Actualizar el código
cd /opt/sistema-citas
git pull origin main
mvn clean package -DskipTests

# Iniciar el servicio
sudo systemctl start sistema-citas

# Verificar logs
sudo journalctl -u sistema-citas -f
```

### Monitoreo

```bash
# Ver logs en tiempo real
sudo journalctl -u sistema-citas -f

# Ver logs de las últimas 100 líneas
sudo journalctl -u sistema-citas -n 100

# Ver logs de hoy
sudo journalctl -u sistema-citas --since today

# Ver uso de recursos
htop
```

## Solución de Problemas

### La aplicación no inicia

```bash
# Verificar logs
sudo journalctl -u sistema-citas -n 50

# Verificar que MySQL esté ejecutándose
sudo systemctl status mysql

# Verificar conectividad a MySQL
mysql -u root -p -e "SELECT 1"

# Verificar puerto 8080
sudo netstat -tlnp | grep 8080
```

### Error de conexión a base de datos

```bash
# Verificar credenciales en application.properties
cat src/main/resources/application.properties

# Probar conexión manual
mysql -h localhost -u root -p Barberia_Neita

# Verificar permisos de usuario
mysql -u root -p -e "SHOW GRANTS FOR 'root'@'localhost';"
```

### Problemas de memoria

```bash
# Aumentar memoria heap de Java
# Editar el servicio systemd
sudo nano /etc/systemd/system/sistema-citas.service

# Modificar ExecStart
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar /opt/sistema-citas/sistema-citas-0.0.1-SNAPSHOT.jar

# Recargar y reiniciar
sudo systemctl daemon-reload
sudo systemctl restart sistema-citas
```

## Seguridad en Producción

### Recomendaciones

1. **Cambiar credenciales por defecto** inmediatamente después del primer despliegue
2. **Usar HTTPS** con certificado SSL válido
3. **Configurar firewall** para permitir solo puertos necesarios
4. **Actualizar regularmente** el sistema operativo y dependencias
5. **Implementar backups automáticos** de la base de datos
6. **Monitorear logs** regularmente para detectar actividad sospechosa
7. **Usar contraseñas fuertes** para MySQL y usuarios del sistema
8. **Limitar acceso SSH** solo a IPs autorizadas
9. **Implementar rate limiting** en Nginx si se usa como proxy
10. **Mantener logs de auditoría** de todas las operaciones críticas

## Contacto y Soporte

Para asistencia con el despliegue, contacta al equipo de desarrollo.

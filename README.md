# Guía de Ejecución - Sistema de Biblioteca (Docker)

Siga estos pasos en orden para levantar y ejecutar la aplicación en cualquier computadora.

---

## 📋 Requisitos Previos

Antes de ejecutar los comandos, asegúrese de tener configurado en la máquina host (Windows/Linux/Mac):

1. **Docker Desktop** iniciado y en ejecución.
2. **MySQL Server** corriendo localmente en el puerto `3306`.
3. Base de datos creada con el nombre `dbbiblab` e importadas las tablas y registros correspondientes.
   - **Usuario por defecto:** `root`
   - **Contraseña por defecto:** `root`

---

## 🚀 Pasos de Instalación y Despliegue

### Paso 1: Clonar el repositorio
Abra una terminal (PowerShell, Git Bash o CMD) y clone el proyecto:
```bash
git clone https://github.com/Enzitoxxx/contenedor.git
cd contenedor
```

### Paso 2: Construir la imagen de Docker
Genere la imagen local a partir del `Dockerfile`, las configuraciones de Payara y el archivo `.war` empaquetado:
```bash
docker build -t biblioteca-jsf:1.0 .
```
*Verificación:* La terminal finalizará mostrando:
`naming to docker.io/library/biblioteca-jsf:1.0 done`

### Paso 3: Iniciar el contenedor
Levante el contenedor mapeando el puerto `8080`:
```bash
docker run -d -p 8080:8080 --name app-biblioteca biblioteca-jsf:1.0
```

---

## 🔍 Paso 4: Esperar el despliegue y verificar

Aguarde entre **15 y 20 segundos** para que Payara Server complete la inicialización y el despliegue de la aplicación.

1. **Comprobar que el contenedor permanece activo:**
```bash
docker ps
```
*(Debe figurar `app-biblioteca` con estado `Up`)*

2. **Revisar los logs de despliegue:**
```bash
docker logs --tail 25 app-biblioteca
```
*(Debe mostrar el mensaje: `ROOT was successfully deployed in ... milliseconds`)*

---

## 🌐 Paso 5: Acceso a la Aplicación

Abra su navegador web e ingrese a cualquiera de las siguientes rutas:

- **Catálogo de Libros:** http://localhost:8080/ROOT/libros.xhtml
- **Página de Inicio:** http://localhost:8080/ROOT/index.xhtml

---

## ⚠️ ¿Qué pasa si su Base de Datos o Credenciales son Diferentes?

Por defecto, la imagen está configurada para conectarse a una base de datos MySQL con los siguientes valores fijos:
- **Base de datos:** `dbbiblab`
- **Usuario:** `root`
- **Contraseña:** `root`
- **Host:** `host.docker.internal:3306`

### ¿Qué sucederá si su base de datos tiene otro nombre?
El contenedor iniciará sin errores visibles, pero al intentar consultar datos desde el navegador web (`libros.xhtml`), Payara arrojará una excepción interna:
```text
java.sql.SQLSyntaxErrorException: Unknown database 'dbbiblab'
```
La página no listará libros o devolverá un error HTTP 500.

### Solución A (Recomendada y directa - Sin tocar archivos)
Cree una base de datos con el nombre esperado `dbbiblab` en su motor MySQL local e importe allí las tablas:
```sql
CREATE DATABASE dbbiblab;
USE dbbiblab;
-- Importar tablas y datos aquí
```

### Solución B (Ajustar la configuración al nombre propio)
Si debe utilizar sí o sí su propia base de datos o contraseñas:
1. Abra el archivo `post-boot-commands.asadmin` antes de construir la imagen.
2. Localice la propiedad: `DatabaseName=dbbiblab`
3. Cámbiela por el nombre de su base de datos (por ejemplo, `DatabaseName=mi_biblioteca`).
4. Reconstruya la imagen y vuelva a lanzar el contenedor:
```bash
docker rm -f app-biblioteca
docker build -t biblioteca-jsf:1.0 .
docker run -d -p 8080:8080 --name app-biblioteca biblioteca-jsf:1.0
```

---

## 🛠️ Comandos Útiles de Mantenimiento

- **Ver logs en tiempo real:**
```bash
docker logs -f app-biblioteca
```
- **Detener el contenedor:**
```bash
docker stop app-biblioteca
```
- **Volver a iniciar el contenedor:**
```bash
docker start app-biblioteca
```
- **Eliminar el contenedor (para reiniciar limpio):**
```bash
docker rm -f app-biblioteca
```
# Préstamos

Este proyecto está compuesto por:

* Base de datos con script de inicialización
* Servicio REST desarrollado con **Java + Spring Boot + Maven**
* Aplicación Web desarrollada con **Vue.js**
* Orquestación mediante **Docker Compose**

---

## Estructura del Proyecto

```bash
.
├── docker-compose.yml
├── init.sql
├── Diagrama ER.png
├── prestamos/              # Servicio REST (Spring Boot + Maven)
└── chn-web-prestamos/      # Aplicación Web (Vue.js)
```

### Componentes

* **Base de datos**

  * Script de creación e inicialización: `./init.sql`
  * Diagrama Entidad-Relación: `./Diagrama ER.png`

* **Servicio REST**

  * Ubicación: `./prestamos`
  * Tecnología: Java + Spring Boot + Maven

* **Aplicación Web**

  * Ubicación: `./chn-web-prestamos`
  * Tecnología: Vue.js

---

## Requisitos Previos

Antes de ejecutar el proyecto asegúrate de tener instalado:

* Docker
* Docker Compose (incluido en Docker Desktop)

Verifica la instalación con:

```bash
docker --version
docker compose version
```

---

## Cómo Ejecutar el Proyecto

Desde la raíz del proyecto (donde se encuentra el archivo `docker-compose.yml`), ejecutar:

```bash
docker compose up --build
```

Esto realizará:

1. Construcción de la imagen del servicio REST (Spring Boot).
2. Construcción de la imagen de la aplicación Vue.js.
3. Creación del contenedor de base de datos.
4. Ejecución automática del script `./init.sql` para inicializar la base de datos.
5. Levantamiento de todos los servicios definidos en el `docker-compose.yml`.

---

## Documentación de la API (Swagger)

Una vez levantado el proyecto, puedes acceder a la documentación interactiva de los endpoints en:

```
http://localhost:8080/swagger-ui/index.html#/
```

Desde allí podrás:

* Visualizar todos los endpoints disponibles
* Probar peticiones directamente desde el navegador
* Consultar los modelos de request y response
* Ver los códigos de respuesta HTTP

---

## Variables de Entorno y Configuración

Por motivos prácticos y para facilitar la ejecución del proyecto, **la contraseña de la base de datos y el secret del JWT fueron definidos directamente en el archivo `docker-compose.yml`**.

**Esto NO es una buena práctica para ambientes productivos.**

Se recomienda utilizar un archivo `.env` para manejar variables sensibles.

### Ejemplo de archivo `.env`

```env
DB_HOST=database
DB_PORT=3306
DB_NAME=prestamos
DB_USER=root
DB_PASSWORD=password

JWT_SECRET=jwt
```

Luego, en el `docker-compose.yml`, las variables pueden referenciarse así:

```yaml
environment:
  - DB_PASSWORD=${DB_PASSWORD}
  - JWT_SECRET=${JWT_SECRET}
```

Y ejecutar normalmente:

```bash
docker compose --env-file .env up --build
```

---

## Detener los Contenedores

Para detener los servicios:

```bash
docker compose down
```

Si deseas eliminar volúmenes asociados:

```bash
docker compose down -v
```

---

## Base de Datos

* El script de inicialización se encuentra en:

```
./init.sql
```

* El diagrama Entidad-Relación está disponible en:

```
./Diagrama ER.png
```

Este archivo puede utilizarse como referencia para entender la estructura de tablas y relaciones.

---

## Servicio REST (Spring Boot)

Ubicación:

```
./prestamos
```

Tecnologías utilizadas:

* Java
* Spring Boot
* Maven

El servicio se compila automáticamente al ejecutar `docker compose up --build`.

---

## Aplicación Web (Vue.js)

Ubicación:

```
./chn-web-prestamos
```

Tecnología:

* Vue.js

La aplicación web se construye y ejecuta automáticamente mediante Docker Compose.

---

## Notas Finales

* Asegúrate de que los puertos definidos en `docker-compose.yml` no estén siendo utilizados por otros servicios.
* Para cambios en el código, reconstruir con:

```bash
docker compose up --build
```


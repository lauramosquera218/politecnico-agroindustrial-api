# Politécnico Agroindustrial — API REST (módulo Estudiantes)

**Evidencia:** GA7-220501096-AA2-EV01 — Codificación de módulos del software
**Programa:** Tecnólogo en Análisis y Desarrollo de Software — SENA (Ficha 3336162)

Módulo **Estudiantes** del sistema de gestión académica del Politécnico Agroindustrial, desarrollado en Java con conexión a MySQL mediante **JDBC**. Expone un CRUD completo (insertar, consultar, actualizar y eliminar) como API REST con servidor HTTP embebido (`com.sun.net.httpserver`) y respuestas en JSON.

El módulo se apoya en los artefactos anteriores del proyecto: la base de datos `politecnicoagroindustrial` (14 tablas, script en `database/`), la vista *Estudiantes* del prototipo front-end, el informe de herramientas de versionamiento (Git + GitLab) y el informe de estándares de codificación.

## Tecnologías

| Tecnología | Uso |
|---|---|
| Java 21 (LTS) | Lenguaje de desarrollo |
| Maven | Gestión de dependencias y ciclo de construcción (`pom.xml`) |
| JDBC + MySQL Connector/J 8.3.0 | Conexión con MySQL |
| Gson 2.11.0 | Conversión objetos Java ↔ JSON |
| JUnit 5 | Pruebas unitarias |
| Git + GitLab | Versionamiento y repositorio remoto |

## Estructura del proyecto

```
politecnico-agroindustrial-api/
├── pom.xml
├── README.md
├── ENLACE_REPOSITORIO.txt
├── database/
│   └── Politecnico.sql                  → script de la base de datos (tablas y datos de prueba)
└── src/
    ├── main/java/co/edu/sena/politecnico/
    │   ├── Main.java                    → verifica la conexión y arranca el servidor
    │   ├── controllers/
    │   │   └── EstudiantesHandler.java  → recibe las peticiones HTTP y responde en JSON
    │   ├── models/
    │   │   └── Estudiante.java          → entidad (atributos, getters y setters)
    │   ├── services/
    │   │   └── EstudianteService.java   → CRUD con JDBC (PreparedStatement y transacciones)
    │   └── utils/
    │       ├── DatabaseConnection.java  → conexión JDBC centralizada
    │       ├── ClaveUtil.java           → hash SHA-256 de contraseñas
    │       └── ValidadorEstudiante.java → reglas de validación de datos
    └── test/java/co/edu/sena/politecnico/utils/
        ├── ClaveUtilTest.java
        └── ValidadorEstudianteTest.java
```

Flujo: **Cliente → Main → EstudiantesHandler → EstudianteService → DatabaseConnection → MySQL**.

## Requisitos

- JDK 21
- Maven (incluido en IntelliJ IDEA)
- MySQL 8 o superior en ejecución
- IntelliJ IDEA (recomendado) y Postman para las pruebas

## Puesta en marcha

### 1. Crear la base de datos

Ejecute el script en MySQL (Workbench o consola):

```bash
mysql -u root -p < database/Politecnico.sql
```

Esto crea la base `politecnicoagroindustrial` con sus 14 tablas y datos de prueba.

### 2. Configurar la conexión

Las credenciales **no están escritas en el código**; se leen de variables de entorno:

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `DB_URL` | Cadena JDBC | `jdbc:mysql://localhost:3306/politecnicoagroindustrial?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC` |
| `DB_USER` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contraseña de MySQL | *(vacía)* |
| `PORT` | Puerto del servidor | `8080` |

En IntelliJ: *Run → Edit Configurations → Main → Environment variables* y agregue, por ejemplo, `DB_PASSWORD=su_clave`.
En la terminal de Windows (PowerShell): `$env:DB_PASSWORD="su_clave"`.

### 3. Ejecutar

- **IntelliJ IDEA:** abra la carpeta como proyecto Maven, sincronice el `pom.xml` (clic derecho → *Maven → Sync Project*) y ejecute `Main` con el botón verde ▶.
- **Terminal:** `mvn compile exec:java`

Si todo está bien se verá:

```
¡Conexión a la base de datos establecida correctamente!
Servidor iniciado en http://localhost:8080/estudiantes
```

### 4. Pruebas unitarias

```bash
mvn test
```

## Endpoints

| Operación | Método | Ruta | Descripción | Respuesta |
|---|---|---|---|---|
| Consulta | GET | `/estudiantes` | Lista todos los estudiantes | 200 |
| Consulta | GET | `/estudiantes/{id}` | Busca un estudiante por id | 200 / 404 |
| Inserción | POST | `/estudiantes` | Registra un estudiante (y su cuenta de usuario) | 201 |
| Actualización | PUT | `/estudiantes/{id}` | Actualiza los datos del estudiante | 200 / 404 |
| Eliminación | DELETE | `/estudiantes/{id}` | Elimina al estudiante y su cuenta | 200 / 404 / 409 |

Otros códigos: **400** (datos inválidos o JSON mal formado), **405** (método no permitido), **409** (documento/correo duplicado, o el estudiante tiene matrículas/calificaciones asociadas), **500** (error interno).

### Ejemplo — POST `http://localhost:8080/estudiantes` (Body → raw → JSON)

```json
{
  "idPrograma": 1,
  "nombres": "Camila Andrea",
  "apellidos": "Rojas Peña",
  "documento": "1098765432",
  "telefono": "3104567890",
  "direccion": "Calle 10 # 20-15",
  "fechaNacimiento": "2005-04-18",
  "correo": "camila.rojas@correo.com",
  "contrasena": "Segura123*"
}
```

### Ejemplo — PUT `http://localhost:8080/estudiantes/31`

```json
{
  "idPrograma": 2,
  "nombres": "Camila Andrea",
  "apellidos": "Rojas Peña",
  "documento": "1098765432",
  "telefono": "3201112233",
  "direccion": "Carrera 5 # 8-12",
  "fechaNacimiento": "2005-04-18",
  "correo": "camila.actualizada@correo.com"
}
```

### Eliminar — DELETE `http://localhost:8080/estudiantes/31` (sin body)

> Los estudiantes de los datos de prueba tienen matrículas y calificaciones, por lo que la base de datos rechaza su eliminación (409). Para probar `DELETE` con éxito, elimine un estudiante recién creado con `POST`.

## Reglas de validación

- `nombres`, `apellidos`: obligatorios (máx. 100 caracteres).
- `documento`: 6 a 12 dígitos; único en la base de datos.
- `idPrograma`: obligatorio y debe existir en la tabla `programa`.
- `correo`: obligatorio al crear, formato válido y único.
- `contrasena`: obligatoria al crear (mínimo 8 caracteres); se guarda como hash SHA-256 y nunca se devuelve en las respuestas.
- `telefono` (opcional): 7 a 10 dígitos. `fechaNacimiento` (opcional): formato `AAAA-MM-DD`, no futura.

## Estándares de codificación aplicados

| Elemento | Convención | Ejemplo en el proyecto |
|---|---|---|
| Paquetes | minúsculas, nombre de dominio invertido | `co.edu.sena.politecnico.services` |
| Clases | PascalCase, sustantivo | `EstudianteService`, `Estudiante` |
| Métodos | camelCase, verbo + sustantivo | `obtenerPorId()`, `validarCreacion()` |
| Variables | camelCase, nombres descriptivos | `idEstudiante`, `fechaNacimiento` |
| Constantes | MAYÚSCULAS_CON_GUION_BAJO | `SQL_INSERTAR_USUARIO`, `CODIGO_OK` |
| Tablas y campos de BD | snake_case en español | `id_estudiante`, `fecha_nacimiento` |

Además: indentación de 4 espacios (convención de Java), comentarios Javadoc en español, `PreparedStatement` en todas las consultas y `try-with-resources` para cerrar conexiones.

## Control de versiones (Git)

El proyecto se desarrolló con Git siguiendo un flujo por ramas: la rama `main` contiene la versión estable y el módulo se construyó en la rama `feature/modulo-estudiantes`, integrada luego con un merge. Para ver el historial:

```bash
git log --oneline --graph
```

Para publicarlo en GitLab (crear antes un proyecto vacío y privado):

```bash
git remote add origin https://gitlab.com/<usuario>/politecnico-agroindustrial-api.git
git push -u origin --all
```

Pegue la URL del repositorio en `ENLACE_REPOSITORIO.txt`.

## Notas de seguridad

- Las credenciales de la base de datos se leen de variables de entorno y el archivo `.gitignore` excluye archivos `.env`.
- Los datos de prueba del script SQL traen contraseñas en texto plano; las cuentas creadas por este módulo se guardan con hash SHA-256. En un sistema en producción se recomienda un algoritmo con sal como bcrypt o Argon2.

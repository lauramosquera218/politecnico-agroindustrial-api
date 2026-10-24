package co.edu.sena.politecnico.controllers;

import co.edu.sena.politecnico.models.Estudiante;
import co.edu.sena.politecnico.services.EstudianteService;
import co.edu.sena.politecnico.utils.ValidadorEstudiante;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador HTTP del módulo Estudiantes. Atiende las rutas:
 * <ul>
 *   <li>{@code GET /estudiantes} - lista todos los estudiantes.</li>
 *   <li>{@code GET /estudiantes/{id}} - consulta un estudiante.</li>
 *   <li>{@code POST /estudiantes} - registra un estudiante nuevo.</li>
 *   <li>{@code PUT /estudiantes/{id}} - actualiza un estudiante.</li>
 *   <li>{@code DELETE /estudiantes/{id}} - elimina un estudiante.</li>
 * </ul>
 * Todas las respuestas son JSON.
 */
public class EstudiantesHandler implements HttpHandler {

    private static final Logger REGISTRO = Logger.getLogger(EstudiantesHandler.class.getName());

    private static final int CODIGO_OK = 200;
    private static final int CODIGO_CREADO = 201;
    private static final int CODIGO_SIN_CONTENIDO = 204;
    private static final int CODIGO_PETICION_INVALIDA = 400;
    private static final int CODIGO_NO_ENCONTRADO = 404;
    private static final int CODIGO_METODO_NO_PERMITIDO = 405;
    private static final int CODIGO_CONFLICTO = 409;
    private static final int CODIGO_ERROR_INTERNO = 500;

    // Códigos de error de MySQL/MariaDB
    private static final int ERROR_MYSQL_DUPLICADO = 1062;
    private static final int ERROR_MYSQL_HIJO_REFERENCIADO = 1451;
    private static final int ERROR_MYSQL_REFERENCIA_INEXISTENTE = 1452;

    private final EstudianteService servicio = new EstudianteService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange intercambio) throws IOException {
        try {
            agregarEncabezadosCors(intercambio);
            String metodo = intercambio.getRequestMethod().toUpperCase();

            if ("OPTIONS".equals(metodo)) {
                intercambio.sendResponseHeaders(CODIGO_SIN_CONTENIDO, -1);
                return;
            }

            Integer idEstudiante = extraerId(intercambio.getRequestURI().getPath());

            switch (metodo) {
                case "GET" -> atenderConsulta(intercambio, idEstudiante);
                case "POST" -> atenderCreacion(intercambio, idEstudiante);
                case "PUT" -> atenderActualizacion(intercambio, idEstudiante);
                case "DELETE" -> atenderEliminacion(intercambio, idEstudiante);
                default -> enviarError(intercambio, CODIGO_METODO_NO_PERMITIDO,
                        "Método no permitido. Use GET, POST, PUT o DELETE.");
            }
        } catch (IllegalArgumentException | JsonSyntaxException e) {
            String mensaje = (e instanceof JsonSyntaxException)
                    ? "El cuerpo de la petición no es un JSON válido." : e.getMessage();
            enviarError(intercambio, CODIGO_PETICION_INVALIDA, mensaje);
        } catch (SQLException e) {
            atenderErrorSql(intercambio, e);
        } finally {
            intercambio.close();
        }
    }

    // ------------------------------------------------------------------
    // Atención de cada método HTTP
    // ------------------------------------------------------------------

    private void atenderConsulta(HttpExchange intercambio, Integer idEstudiante) throws IOException, SQLException {
        if (idEstudiante == null) {
            List<Estudiante> estudiantes = servicio.listar();
            enviarJson(intercambio, CODIGO_OK, estudiantes);
            return;
        }
        Estudiante estudiante = servicio.obtenerPorId(idEstudiante);
        if (estudiante == null) {
            enviarError(intercambio, CODIGO_NO_ENCONTRADO, "No existe un estudiante con id " + idEstudiante + ".");
        } else {
            enviarJson(intercambio, CODIGO_OK, estudiante);
        }
    }

    private void atenderCreacion(HttpExchange intercambio, Integer idEstudiante) throws IOException, SQLException {
        if (idEstudiante != null) {
            enviarError(intercambio, CODIGO_METODO_NO_PERMITIDO, "Para crear use POST /estudiantes (sin id).");
            return;
        }
        Estudiante nuevo = leerCuerpo(intercambio);
        ValidadorEstudiante.validarCreacion(nuevo);

        int idCreado = servicio.crear(nuevo);
        enviarJson(intercambio, CODIGO_CREADO, servicio.obtenerPorId(idCreado));
    }

    private void atenderActualizacion(HttpExchange intercambio, Integer idEstudiante) throws IOException, SQLException {
        if (idEstudiante == null) {
            enviarError(intercambio, CODIGO_PETICION_INVALIDA, "Debe indicar el id en la ruta: PUT /estudiantes/{id}.");
            return;
        }
        Estudiante datos = leerCuerpo(intercambio);
        datos.setIdEstudiante(idEstudiante);
        ValidadorEstudiante.validarActualizacion(datos);

        if (servicio.actualizar(datos)) {
            enviarJson(intercambio, CODIGO_OK, servicio.obtenerPorId(idEstudiante));
        } else {
            enviarError(intercambio, CODIGO_NO_ENCONTRADO, "No existe un estudiante con id " + idEstudiante + ".");
        }
    }

    private void atenderEliminacion(HttpExchange intercambio, Integer idEstudiante) throws IOException, SQLException {
        if (idEstudiante == null) {
            enviarError(intercambio, CODIGO_PETICION_INVALIDA, "Debe indicar el id en la ruta: DELETE /estudiantes/{id}.");
            return;
        }
        if (servicio.eliminar(idEstudiante)) {
            enviarJson(intercambio, CODIGO_OK, Map.of("mensaje", "Estudiante " + idEstudiante + " eliminado correctamente."));
        } else {
            enviarError(intercambio, CODIGO_NO_ENCONTRADO, "No existe un estudiante con id " + idEstudiante + ".");
        }
    }

    // ------------------------------------------------------------------
    // Utilidades de la petición y la respuesta
    // ------------------------------------------------------------------

    /** Devuelve el id de la ruta ({@code /estudiantes/5} -> 5) o {@code null} si no trae id. */
    private Integer extraerId(String ruta) {
        String[] partes = ruta.split("/");
        if (partes.length <= 2) {
            return null;
        }
        if (partes.length > 3) {
            throw new IllegalArgumentException("Ruta no válida. Use /estudiantes o /estudiantes/{id}.");
        }
        try {
            int id = Integer.parseInt(partes[2]);
            if (id <= 0) {
                throw new NumberFormatException();
            }
            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El id debe ser un número entero positivo.");
        }
    }

    private Estudiante leerCuerpo(HttpExchange intercambio) throws IOException {
        String cuerpo = new String(intercambio.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (cuerpo.isBlank()) {
            throw new IllegalArgumentException("El cuerpo de la petición está vacío. Envíe un JSON.");
        }
        Estudiante estudiante = gson.fromJson(cuerpo, Estudiante.class);
        if (estudiante == null) {
            throw new IllegalArgumentException("El cuerpo de la petición no es un JSON válido.");
        }
        return estudiante;
    }

    private void atenderErrorSql(HttpExchange intercambio, SQLException e) throws IOException {
        switch (e.getErrorCode()) {
            case ERROR_MYSQL_DUPLICADO -> enviarError(intercambio, CODIGO_CONFLICTO,
                    "Ya existe un registro con ese documento o correo electrónico.");
            case ERROR_MYSQL_HIJO_REFERENCIADO -> enviarError(intercambio, CODIGO_CONFLICTO,
                    "No se puede eliminar: el estudiante tiene registros asociados (matrículas, calificaciones, etc.).");
            case ERROR_MYSQL_REFERENCIA_INEXISTENTE -> enviarError(intercambio, CODIGO_PETICION_INVALIDA,
                    "El programa indicado (idPrograma) no existe.");
            default -> {
                REGISTRO.log(Level.SEVERE, "Error de base de datos", e);
                enviarError(intercambio, CODIGO_ERROR_INTERNO, "Error interno al acceder a la base de datos.");
            }
        }
    }

    private void agregarEncabezadosCors(HttpExchange intercambio) {
        intercambio.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        intercambio.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        intercambio.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
    }

    private void enviarError(HttpExchange intercambio, int codigo, String mensaje) throws IOException {
        enviarJson(intercambio, codigo, Map.of("error", mensaje));
    }

    private void enviarJson(HttpExchange intercambio, int codigo, Object cuerpo) throws IOException {
        byte[] bytes = gson.toJson(cuerpo).getBytes(StandardCharsets.UTF_8);
        intercambio.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        intercambio.sendResponseHeaders(codigo, bytes.length);
        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(bytes);
        }
    }
}

package co.edu.sena.politecnico.services;

import co.edu.sena.politecnico.models.Estudiante;
import co.edu.sena.politecnico.utils.ClaveUtil;
import co.edu.sena.politecnico.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * Lógica de acceso a datos (JDBC) del módulo Estudiantes.
 *
 * <p>Implementa las cuatro operaciones CRUD sobre las tablas {@code estudiante} y
 * {@code usuario}. Registrar y eliminar un estudiante afectan ambas tablas, por lo que se
 * ejecutan dentro de una transacción (commit / rollback).</p>
 */
public class EstudianteService {

    private static final String SQL_CONSULTA_BASE =
            "SELECT e.id_estudiante, e.id_usuario, e.id_programa, e.nombres, e.apellidos, "
                    + "e.documento, e.telefono, e.direccion, e.fecha_nacimiento, "
                    + "u.correo, p.nombre AS nombre_programa "
                    + "FROM estudiante e "
                    + "JOIN usuario u ON u.id_usuario = e.id_usuario "
                    + "JOIN programa p ON p.id_programa = e.id_programa ";

    private static final String SQL_INSERTAR_USUARIO =
            "INSERT INTO usuario (id_rol, nombre, correo, contrasena, estado) "
                    + "VALUES ((SELECT id_rol FROM rol WHERE nombre = 'Estudiante'), ?, ?, ?, 'Activo')";

    private static final String SQL_INSERTAR_ESTUDIANTE =
            "INSERT INTO estudiante (id_usuario, id_programa, nombres, apellidos, documento, "
                    + "telefono, direccion, fecha_nacimiento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_ACTUALIZAR_ESTUDIANTE =
            "UPDATE estudiante SET id_programa = ?, nombres = ?, apellidos = ?, documento = ?, "
                    + "telefono = ?, direccion = ?, fecha_nacimiento = ? WHERE id_estudiante = ?";

    private static final String SQL_ACTUALIZAR_USUARIO =
            "UPDATE usuario u JOIN estudiante e ON e.id_usuario = u.id_usuario "
                    + "SET u.nombre = ?, u.correo = COALESCE(?, u.correo) WHERE e.id_estudiante = ?";

    private static final String SQL_BUSCAR_ID_USUARIO =
            "SELECT id_usuario FROM estudiante WHERE id_estudiante = ?";

    private static final String SQL_ELIMINAR_ESTUDIANTE = "DELETE FROM estudiante WHERE id_estudiante = ?";

    private static final String SQL_ELIMINAR_USUARIO = "DELETE FROM usuario WHERE id_usuario = ?";

    /** CONSULTA: devuelve todos los estudiantes ordenados por apellidos. */
    public List<Estudiante> listar() throws SQLException {
        List<Estudiante> estudiantes = new ArrayList<>();
        String sql = SQL_CONSULTA_BASE + "ORDER BY e.apellidos, e.nombres";

        try (Connection conexion = DatabaseConnection.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                estudiantes.add(mapearEstudiante(resultado));
            }
        }
        return estudiantes;
    }

    /** CONSULTA: busca un estudiante por su id. Devuelve {@code null} si no existe. */
    public Estudiante obtenerPorId(int idEstudiante) throws SQLException {
        String sql = SQL_CONSULTA_BASE + "WHERE e.id_estudiante = ?";

        try (Connection conexion = DatabaseConnection.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, idEstudiante);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? mapearEstudiante(resultado) : null;
            }
        }
    }

    /**
     * INSERCIÓN: registra un estudiante nuevo junto con su cuenta de usuario (rol Estudiante).
     * Ambas inserciones se hacen en una sola transacción.
     *
     * @return id del estudiante creado
     */
    public int crear(Estudiante estudiante) throws SQLException {
        try (Connection conexion = DatabaseConnection.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                int idUsuario = insertarUsuario(conexion, estudiante);
                int idEstudiante = insertarEstudiante(conexion, estudiante, idUsuario);
                conexion.commit();
                return idEstudiante;
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        }
    }

    /**
     * ACTUALIZACIÓN: modifica los datos del estudiante y sincroniza nombre y correo de su
     * cuenta de usuario.
     *
     * @return {@code true} si el estudiante existía y fue actualizado; {@code false} si no existe
     */
    public boolean actualizar(Estudiante estudiante) throws SQLException {
        try (Connection conexion = DatabaseConnection.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                int filasEstudiante;
                try (PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR_ESTUDIANTE)) {
                    sentencia.setInt(1, estudiante.getIdPrograma());
                    sentencia.setString(2, estudiante.getNombres());
                    sentencia.setString(3, estudiante.getApellidos());
                    sentencia.setString(4, estudiante.getDocumento());
                    asignarTextoOpcional(sentencia, 5, estudiante.getTelefono());
                    asignarTextoOpcional(sentencia, 6, estudiante.getDireccion());
                    asignarFechaOpcional(sentencia, 7, estudiante.getFechaNacimiento());
                    sentencia.setInt(8, estudiante.getIdEstudiante());
                    filasEstudiante = sentencia.executeUpdate();
                }
                if (filasEstudiante == 0) {
                    conexion.rollback();
                    return false;
                }

                try (PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR_USUARIO)) {
                    sentencia.setString(1, estudiante.getNombres() + " " + estudiante.getApellidos());
                    asignarTextoOpcional(sentencia, 2, estudiante.getCorreo());
                    sentencia.setInt(3, estudiante.getIdEstudiante());
                    sentencia.executeUpdate();
                }
                conexion.commit();
                return true;
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        }
    }

    /**
     * ELIMINACIÓN: borra al estudiante y su cuenta de usuario en una transacción. Si el
     * estudiante tiene matrículas, calificaciones u otros registros asociados, la base de
     * datos rechaza la operación (llave foránea) y se revierte todo.
     *
     * @return {@code true} si fue eliminado; {@code false} si no existe
     */
    public boolean eliminar(int idEstudiante) throws SQLException {
        try (Connection conexion = DatabaseConnection.obtenerConexion()) {
            conexion.setAutoCommit(false);
            try {
                int idUsuario = buscarIdUsuario(conexion, idEstudiante);
                if (idUsuario == 0) {
                    conexion.rollback();
                    return false;
                }
                ejecutarEliminacion(conexion, SQL_ELIMINAR_ESTUDIANTE, idEstudiante);
                ejecutarEliminacion(conexion, SQL_ELIMINAR_USUARIO, idUsuario);
                conexion.commit();
                return true;
            } catch (SQLException e) {
                conexion.rollback();
                throw e;
            }
        }
    }

    // ------------------------------------------------------------------
    // Métodos auxiliares privados
    // ------------------------------------------------------------------

    private int insertarUsuario(Connection conexion, Estudiante estudiante) throws SQLException {
        try (PreparedStatement sentencia =
                     conexion.prepareStatement(SQL_INSERTAR_USUARIO, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, estudiante.getNombres() + " " + estudiante.getApellidos());
            sentencia.setString(2, estudiante.getCorreo());
            sentencia.setString(3, ClaveUtil.generarHash(estudiante.getContrasena()));
            sentencia.executeUpdate();
            return leerIdGenerado(sentencia);
        }
    }

    private int insertarEstudiante(Connection conexion, Estudiante estudiante, int idUsuario) throws SQLException {
        try (PreparedStatement sentencia =
                     conexion.prepareStatement(SQL_INSERTAR_ESTUDIANTE, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setInt(1, idUsuario);
            sentencia.setInt(2, estudiante.getIdPrograma());
            sentencia.setString(3, estudiante.getNombres());
            sentencia.setString(4, estudiante.getApellidos());
            sentencia.setString(5, estudiante.getDocumento());
            asignarTextoOpcional(sentencia, 6, estudiante.getTelefono());
            asignarTextoOpcional(sentencia, 7, estudiante.getDireccion());
            asignarFechaOpcional(sentencia, 8, estudiante.getFechaNacimiento());
            sentencia.executeUpdate();
            return leerIdGenerado(sentencia);
        }
    }

    private int buscarIdUsuario(Connection conexion, int idEstudiante) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_ID_USUARIO)) {
            sentencia.setInt(1, idEstudiante);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? resultado.getInt("id_usuario") : 0;
            }
        }
    }

    private void ejecutarEliminacion(Connection conexion, String sql, int id) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setInt(1, id);
            sentencia.executeUpdate();
        }
    }

    private int leerIdGenerado(PreparedStatement sentencia) throws SQLException {
        try (ResultSet claves = sentencia.getGeneratedKeys()) {
            if (claves.next()) {
                return claves.getInt(1);
            }
        }
        throw new SQLException("No se pudo obtener el id generado por la base de datos.");
    }

    private void asignarTextoOpcional(PreparedStatement sentencia, int posicion, String valor) throws SQLException {
        if (valor == null) {
            sentencia.setNull(posicion, Types.VARCHAR);
        } else {
            sentencia.setString(posicion, valor);
        }
    }

    private void asignarFechaOpcional(PreparedStatement sentencia, int posicion, String fecha) throws SQLException {
        if (fecha == null) {
            sentencia.setNull(posicion, Types.DATE);
        } else {
            sentencia.setDate(posicion, Date.valueOf(fecha));
        }
    }

    private Estudiante mapearEstudiante(ResultSet resultado) throws SQLException {
        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(resultado.getInt("id_estudiante"));
        estudiante.setIdUsuario(resultado.getInt("id_usuario"));
        estudiante.setIdPrograma(resultado.getInt("id_programa"));
        estudiante.setNombres(resultado.getString("nombres"));
        estudiante.setApellidos(resultado.getString("apellidos"));
        estudiante.setDocumento(resultado.getString("documento"));
        estudiante.setTelefono(resultado.getString("telefono"));
        estudiante.setDireccion(resultado.getString("direccion"));
        Date fechaNacimiento = resultado.getDate("fecha_nacimiento");
        estudiante.setFechaNacimiento(fechaNacimiento == null ? null : fechaNacimiento.toString());
        estudiante.setCorreo(resultado.getString("correo"));
        estudiante.setNombrePrograma(resultado.getString("nombre_programa"));
        return estudiante;
    }
}

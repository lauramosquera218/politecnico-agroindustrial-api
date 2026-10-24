package co.edu.sena.politecnico.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria que centraliza la conexión JDBC con MySQL.
 *
 * <p>Los datos de conexión se leen de variables de entorno para no dejar credenciales
 * escritas en el código ni subirlas al repositorio:</p>
 * <ul>
 *   <li>{@code DB_URL}: cadena de conexión JDBC.</li>
 *   <li>{@code DB_USER}: usuario de MySQL.</li>
 *   <li>{@code DB_PASSWORD}: contraseña de MySQL.</li>
 * </ul>
 */
public final class DatabaseConnection {

    private static final String URL_POR_DEFECTO =
            "jdbc:mysql://localhost:3306/politecnicoagroindustrial"
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USUARIO_POR_DEFECTO = "root";
    private static final String CLAVE_POR_DEFECTO = "";

    private DatabaseConnection() {
        // Clase utilitaria: no se instancia
    }

    /**
     * Abre una nueva conexión con la base de datos. Quien la solicite debe cerrarla
     * (idealmente con try-with-resources).
     *
     * @return conexión JDBC activa
     * @throws SQLException si no es posible conectarse
     */
    public static Connection obtenerConexion() throws SQLException {
        String url = leerVariable("DB_URL", URL_POR_DEFECTO);
        String usuario = leerVariable("DB_USER", USUARIO_POR_DEFECTO);
        String clave = leerVariable("DB_PASSWORD", CLAVE_POR_DEFECTO);
        return DriverManager.getConnection(url, usuario, clave);
    }

    private static String leerVariable(String nombre, String valorPorDefecto) {
        String valor = System.getenv(nombre);
        return (valor == null || valor.isBlank()) ? valorPorDefecto : valor;
    }
}

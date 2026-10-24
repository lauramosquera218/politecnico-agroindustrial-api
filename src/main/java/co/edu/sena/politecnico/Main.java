package co.edu.sena.politecnico;

import co.edu.sena.politecnico.controllers.EstudiantesHandler;
import co.edu.sena.politecnico.utils.DatabaseConnection;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;

/**
 * Punto de entrada de la aplicación: verifica la conexión JDBC con MySQL y levanta el
 * servidor HTTP embebido con el contexto {@code /estudiantes}.
 */
public class Main {

    private static final int PUERTO_POR_DEFECTO = 8080;
    private static final int HILOS_DEL_SERVIDOR = 8;

    public static void main(String[] args) throws IOException {
        verificarConexionBaseDatos();

        int puerto = leerPuerto();
        HttpServer servidor = HttpServer.create(new InetSocketAddress(puerto), 0);
        servidor.createContext("/estudiantes", new EstudiantesHandler());
        servidor.setExecutor(Executors.newFixedThreadPool(HILOS_DEL_SERVIDOR));
        servidor.start();

        System.out.println("Servidor iniciado en http://localhost:" + puerto + "/estudiantes");
    }

    private static void verificarConexionBaseDatos() {
        try (Connection conexion = DatabaseConnection.obtenerConexion()) {
            System.out.println("¡Conexión a la base de datos establecida correctamente!");
        } catch (SQLException e) {
            System.err.println("No se pudo conectar a la base de datos: " + e.getMessage());
            System.err.println("Revise que MySQL esté encendido y las variables DB_URL, DB_USER y DB_PASSWORD.");
            System.exit(1);
        }
    }

    private static int leerPuerto() {
        String valor = System.getenv("PORT");
        try {
            return (valor == null || valor.isBlank()) ? PUERTO_POR_DEFECTO : Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return PUERTO_POR_DEFECTO;
        }
    }
}

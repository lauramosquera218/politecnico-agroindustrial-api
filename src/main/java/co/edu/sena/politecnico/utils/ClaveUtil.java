package co.edu.sena.politecnico.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Utilidad para proteger las contraseñas antes de guardarlas en la base de datos.
 *
 * <p>Se usa SHA-256 por estar incluido en el JDK (sin dependencias adicionales). En un
 * sistema en producción se recomienda un algoritmo con sal como bcrypt o Argon2.</p>
 */
public final class ClaveUtil {

    private static final String ALGORITMO = "SHA-256";

    private ClaveUtil() {
        // Clase utilitaria: no se instancia
    }

    /**
     * Calcula el hash SHA-256 de una contraseña.
     *
     * @param clave contraseña en texto plano
     * @return hash en hexadecimal (64 caracteres)
     */
    public static String generarHash(String clave) {
        try {
            MessageDigest resumen = MessageDigest.getInstance(ALGORITMO);
            byte[] bytesHash = resumen.digest(clave.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytesHash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(ALGORITMO + " no está disponible en esta JVM", e);
        }
    }
}

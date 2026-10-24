package co.edu.sena.politecnico.utils;

import co.edu.sena.politecnico.models.Estudiante;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Reglas de validación de los datos de un estudiante antes de llegar a la base de datos.
 * Todas las validaciones lanzan {@link IllegalArgumentException} con un mensaje claro.
 */
public final class ValidadorEstudiante {

    private static final Pattern PATRON_CORREO = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    private static final Pattern PATRON_DOCUMENTO = Pattern.compile("^\\d{6,12}$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^\\d{7,10}$");

    private static final int LONGITUD_MAXIMA_NOMBRE = 100;
    private static final int LONGITUD_MAXIMA_CORREO = 100;
    private static final int LONGITUD_MAXIMA_DIRECCION = 150;
    private static final int LONGITUD_MINIMA_CLAVE = 8;

    private ValidadorEstudiante() {
        // Clase utilitaria: no se instancia
    }

    /** Valida los datos necesarios para registrar un estudiante nuevo (incluye cuenta de usuario). */
    public static void validarCreacion(Estudiante estudiante) {
        normalizar(estudiante);
        validarDatosComunes(estudiante);

        if (estudiante.getCorreo() == null) {
            throw new IllegalArgumentException("El correo electrónico es obligatorio.");
        }
        String clave = estudiante.getContrasena();
        if (clave == null || clave.length() < LONGITUD_MINIMA_CLAVE) {
            throw new IllegalArgumentException(
                    "La contraseña es obligatoria y debe tener mínimo " + LONGITUD_MINIMA_CLAVE + " caracteres.");
        }
    }

    /** Valida los datos para actualizar un estudiante existente (el correo es opcional). */
    public static void validarActualizacion(Estudiante estudiante) {
        normalizar(estudiante);
        validarDatosComunes(estudiante);
    }

    /** Elimina espacios sobrantes y convierte en {@code null} los textos opcionales vacíos. */
    public static void normalizar(Estudiante estudiante) {
        estudiante.setNombres(recortar(estudiante.getNombres()));
        estudiante.setApellidos(recortar(estudiante.getApellidos()));
        estudiante.setDocumento(recortar(estudiante.getDocumento()));
        estudiante.setTelefono(vacioANulo(estudiante.getTelefono()));
        estudiante.setDireccion(vacioANulo(estudiante.getDireccion()));
        estudiante.setFechaNacimiento(vacioANulo(estudiante.getFechaNacimiento()));
        estudiante.setCorreo(vacioANulo(estudiante.getCorreo()));
    }

    private static void validarDatosComunes(Estudiante estudiante) {
        validarTextoObligatorio(estudiante.getNombres(), "Los nombres", LONGITUD_MAXIMA_NOMBRE);
        validarTextoObligatorio(estudiante.getApellidos(), "Los apellidos", LONGITUD_MAXIMA_NOMBRE);

        if (estudiante.getDocumento() == null || !PATRON_DOCUMENTO.matcher(estudiante.getDocumento()).matches()) {
            throw new IllegalArgumentException("El documento debe tener entre 6 y 12 dígitos numéricos.");
        }
        if (estudiante.getIdPrograma() <= 0) {
            throw new IllegalArgumentException("Debe indicar el idPrograma del estudiante.");
        }
        if (estudiante.getCorreo() != null
                && (estudiante.getCorreo().length() > LONGITUD_MAXIMA_CORREO
                || !PATRON_CORREO.matcher(estudiante.getCorreo()).matches())) {
            throw new IllegalArgumentException("El correo electrónico no tiene un formato válido.");
        }
        if (estudiante.getTelefono() != null && !PATRON_TELEFONO.matcher(estudiante.getTelefono()).matches()) {
            throw new IllegalArgumentException("El teléfono debe tener entre 7 y 10 dígitos numéricos.");
        }
        if (estudiante.getDireccion() != null && estudiante.getDireccion().length() > LONGITUD_MAXIMA_DIRECCION) {
            throw new IllegalArgumentException(
                    "La dirección no puede superar los " + LONGITUD_MAXIMA_DIRECCION + " caracteres.");
        }
        validarFechaNacimiento(estudiante.getFechaNacimiento());
    }

    private static void validarTextoObligatorio(String valor, String nombreCampo, int longitudMaxima) {
        if (valor == null || valor.isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " son obligatorios.");
        }
        if (valor.length() > longitudMaxima) {
            throw new IllegalArgumentException(nombreCampo + " no pueden superar los " + longitudMaxima + " caracteres.");
        }
    }

    private static void validarFechaNacimiento(String fechaNacimiento) {
        if (fechaNacimiento == null) {
            return;
        }
        LocalDate fecha;
        try {
            fecha = LocalDate.parse(fechaNacimiento);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("La fecha de nacimiento debe tener el formato AAAA-MM-DD.");
        }
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
    }

    private static String recortar(String texto) {
        return texto == null ? null : texto.trim();
    }

    private static String vacioANulo(String texto) {
        String recortado = recortar(texto);
        return (recortado == null || recortado.isEmpty()) ? null : recortado;
    }
}

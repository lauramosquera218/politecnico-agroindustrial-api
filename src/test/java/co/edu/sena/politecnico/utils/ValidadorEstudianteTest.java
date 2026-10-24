package co.edu.sena.politecnico.utils;

import co.edu.sena.politecnico.models.Estudiante;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidadorEstudianteTest {

    private Estudiante crearEstudianteValido() {
        Estudiante estudiante = new Estudiante();
        estudiante.setIdPrograma(1);
        estudiante.setNombres("Camila Andrea");
        estudiante.setApellidos("Rojas Peña");
        estudiante.setDocumento("1098765432");
        estudiante.setTelefono("3104567890");
        estudiante.setDireccion("Calle 10 # 20-15");
        estudiante.setFechaNacimiento("2005-04-18");
        estudiante.setCorreo("camila.rojas@correo.com");
        estudiante.setContrasena("Segura123*");
        return estudiante;
    }

    @Test
    void validarCreacion_conDatosCorrectosNoLanzaExcepcion() {
        assertDoesNotThrow(() -> ValidadorEstudiante.validarCreacion(crearEstudianteValido()));
    }

    @Test
    void validarCreacion_rechazaDocumentoConLetras() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setDocumento("ABC123");
        assertThrows(IllegalArgumentException.class, () -> ValidadorEstudiante.validarCreacion(estudiante));
    }

    @Test
    void validarCreacion_rechazaCorreoSinFormatoValido() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setCorreo("correo-invalido");
        assertThrows(IllegalArgumentException.class, () -> ValidadorEstudiante.validarCreacion(estudiante));
    }

    @Test
    void validarCreacion_exigeCorreo() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setCorreo("   ");
        assertThrows(IllegalArgumentException.class, () -> ValidadorEstudiante.validarCreacion(estudiante));
    }

    @Test
    void validarCreacion_rechazaContrasenaCorta() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setContrasena("123");
        assertThrows(IllegalArgumentException.class, () -> ValidadorEstudiante.validarCreacion(estudiante));
    }

    @Test
    void validarCreacion_rechazaFechaNacimientoFutura() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setFechaNacimiento("2999-01-01");
        assertThrows(IllegalArgumentException.class, () -> ValidadorEstudiante.validarCreacion(estudiante));
    }

    @Test
    void validarCreacion_rechazaFechaConFormatoIncorrecto() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setFechaNacimiento("18/04/2005");
        assertThrows(IllegalArgumentException.class, () -> ValidadorEstudiante.validarCreacion(estudiante));
    }

    @Test
    void validarCreacion_rechazaTelefonoInvalido() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setTelefono("12-34");
        assertThrows(IllegalArgumentException.class, () -> ValidadorEstudiante.validarCreacion(estudiante));
    }

    @Test
    void validarCreacion_exigeIdPrograma() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setIdPrograma(0);
        assertThrows(IllegalArgumentException.class, () -> ValidadorEstudiante.validarCreacion(estudiante));
    }

    @Test
    void validarActualizacion_permiteCorreoYContrasenaOpcionales() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setCorreo(null);
        estudiante.setContrasena(null);
        assertDoesNotThrow(() -> ValidadorEstudiante.validarActualizacion(estudiante));
    }

    @Test
    void normalizar_recortaEspaciosYConvierteOpcionalesVaciosEnNulo() {
        Estudiante estudiante = crearEstudianteValido();
        estudiante.setNombres("  Camila  ");
        estudiante.setTelefono("   ");
        ValidadorEstudiante.normalizar(estudiante);
        assertEquals("Camila", estudiante.getNombres());
        assertNull(estudiante.getTelefono());
    }
}

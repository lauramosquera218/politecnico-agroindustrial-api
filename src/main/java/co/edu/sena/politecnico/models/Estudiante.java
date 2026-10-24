package co.edu.sena.politecnico.models;

/**
 * Modelo (POJO) que representa a un estudiante del Politécnico Agroindustrial.
 *
 * <p>Se relaciona con las tablas {@code estudiante}, {@code usuario} y {@code programa}
 * de la base de datos. Los campos {@code correo} y {@code contrasena} pertenecen a la
 * cuenta de usuario asociada; {@code nombrePrograma} es un dato de solo lectura.</p>
 */
public class Estudiante {

    private int idEstudiante;
    private int idUsuario;
    private int idPrograma;
    private String nombres;
    private String apellidos;
    private String documento;
    private String telefono;
    private String direccion;
    private String fechaNacimiento; // formato AAAA-MM-DD

    // Datos de la cuenta de usuario asociada
    private String correo;
    private String contrasena; // solo de entrada: nunca se devuelve en las respuestas

    // Dato de solo lectura obtenido con JOIN
    private String nombrePrograma;

    public Estudiante() {
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }
}

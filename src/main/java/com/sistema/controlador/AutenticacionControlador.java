package com.sistema.controlador;

import com.sistema.modelo.Administrador;
import com.sistema.modelo.Estudiante;
import com.sistema.modelo.Profesor;
import com.sistema.modelo.Rol;
import com.sistema.modelo.Usuario;
import com.sistema.persistencia.AdministradorDAO;
import com.sistema.persistencia.EstudianteDAO;
import com.sistema.persistencia.ProfesorDAO;
import com.sistema.persistencia.UsuarioDAO;

/** Reglas de negocio del login y del registro. Las ventanas solo llaman a estos métodos. */
public class AutenticacionControlador {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final ProfesorDAO profesorDAO = new ProfesorDAO();
    private final AdministradorDAO administradorDAO = new AdministradorDAO();

    /** Devuelve el usuario si las credenciales son válidas; si no, lanza un error genérico. */
    public Usuario iniciarSesion(String correo, String password) {
        ValidadorDatos.requerido(correo, "correo");
        ValidadorDatos.requerido(password, "contraseña");

        Usuario usuario = usuarioDAO.buscarPorCorreo(correo.trim().toLowerCase());
        // Mismo mensaje si el correo no existe o la contraseña falla: no revela cuál fue.
        if (usuario == null || !SeguridadPassword.verificar(password, usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Correo o contraseña incorrectos.");
        }
        return usuario;
    }

    public void registrarEstudiante(String carnet, String nombre, String carrera,
                                    String correo, String password) {
        ValidadorDatos.requerido(carrera, "carrera");
        Usuario usuario = crearUsuario(carnet, nombre, correo, password, Rol.ESTUDIANTE);
        guardar(usuario, () -> estudianteDAO.insertar(new Estudiante(usuario.getIdentificador(), carrera.trim())));
    }

    public void registrarProfesor(String escalafon, String nombre, String especializacion,
                                  String correo, String password) {
        ValidadorDatos.requerido(especializacion, "especialización");
        Usuario usuario = crearUsuario(escalafon, nombre, correo, password, Rol.PROFESOR);
        guardar(usuario, () -> profesorDAO.insertar(new Profesor(usuario.getIdentificador(), especializacion.trim())));
    }

    public void registrarAdministrador(String credencial, String nombre,
                                       String correo, String password) {
        Usuario usuario = crearUsuario(credencial, nombre, correo, password, Rol.ADMINISTRADOR);
        guardar(usuario, () -> administradorDAO.insertar(new Administrador(usuario.getIdentificador())));
    }

    /** Valida los datos comunes y arma el Usuario con la contraseña ya hasheada. */
    private Usuario crearUsuario(String codigo, String nombre, String correo,
                                 String password, Rol rol) {
        ValidadorDatos.requerido(codigo, rol.getNombreCodigo());
        ValidadorDatos.requerido(nombre, "nombre");
        ValidadorDatos.requerido(correo, "correo");
        ValidadorDatos.correo(correo);
        ValidadorDatos.requerido(password, "contraseña");
        ValidadorDatos.password(password);

        return new Usuario(codigo.trim(), nombre.trim(), correo.trim().toLowerCase(),
                SeguridadPassword.hashear(password), rol);
    }

    /**
     * Guarda el usuario y luego sus datos propios (guardarDetalle).
     * Si el segundo guardado falla, borra el usuario para no dejar registros a medias.
     */
    private void guardar(Usuario usuario, Runnable guardarDetalle) {
        if (usuarioDAO.existeIdentificador(usuario.getIdentificador())) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con ese " + usuario.getRol().getNombreCodigo() + ".");
        }
        if (usuarioDAO.existeCorreo(usuario.getCorreo())) {
            throw new IllegalArgumentException("Ese correo ya está registrado.");
        }

        usuarioDAO.insertar(usuario);
        try {
            guardarDetalle.run();
        } catch (RuntimeException e) {
            usuarioDAO.eliminar(usuario.getIdentificador());
            throw e;
        }
    }
}
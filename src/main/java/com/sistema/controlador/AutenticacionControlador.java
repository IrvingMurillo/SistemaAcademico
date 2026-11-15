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

/** Reglas de negocio del login y del registro de usuarios. */
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

    // En los tres registros: primero se valida todo en el orden del formulario,
    // luego se guarda el usuario y por último sus datos propios.

    /** Registra un estudiante nuevo. */
    public void registrarEstudiante(String carnet, String nombre, String carrera,
                                    String correo, String password) {
        ValidadorDatos.requerido(carnet, "carnet");
        ValidadorDatos.requerido(nombre, "nombre");
        ValidadorDatos.requerido(carrera, "carrera");
        ValidadorDatos.correo(correo);
        ValidadorDatos.password(password);

        Usuario usuario = guardarUsuario(carnet, nombre, correo, password, Rol.ESTUDIANTE);
        try {
            estudianteDAO.insertar(new Estudiante(usuario.getIdentificador(), carrera.trim()));
        } catch (RuntimeException e) {
            usuarioDAO.eliminar(usuario.getIdentificador()); // deshace el paso anterior
            throw e;
        }
    }

    /** Registra un profesor nuevo. */
    public void registrarProfesor(String escalafon, String nombre, String especializacion,
                                  String correo, String password) {
        ValidadorDatos.requerido(escalafon, "escalafón");
        ValidadorDatos.requerido(nombre, "nombre");
        ValidadorDatos.requerido(especializacion, "especialización");
        ValidadorDatos.correo(correo);
        ValidadorDatos.password(password);

        Usuario usuario = guardarUsuario(escalafon, nombre, correo, password, Rol.PROFESOR);
        try {
            profesorDAO.insertar(new Profesor(usuario.getIdentificador(), especializacion.trim()));
        } catch (RuntimeException e) {
            usuarioDAO.eliminar(usuario.getIdentificador());
            throw e;
        }
    }

    /** Registra un administrador nuevo (se usa desde el script CrearRegistros). */
    public void registrarAdministrador(String credencial, String nombre,
                                       String correo, String password) {
        ValidadorDatos.requerido(credencial, "credencial");
        ValidadorDatos.requerido(nombre, "nombre");
        ValidadorDatos.correo(correo);
        ValidadorDatos.password(password);

        Usuario usuario = guardarUsuario(credencial, nombre, correo, password, Rol.ADMINISTRADOR);
        try {
            administradorDAO.insertar(new Administrador(usuario.getIdentificador()));
        } catch (RuntimeException e) {
            usuarioDAO.eliminar(usuario.getIdentificador());
            throw e;
        }
    }

    /** Comprueba que no haya duplicados y guarda los datos de acceso (con la contraseña hasheada). */
    private Usuario guardarUsuario(String codigo, String nombre, String correo,
                                   String password, Rol rol) {
        String identificador = codigo.trim();
        String correoNormalizado = correo.trim().toLowerCase();

        if (usuarioDAO.existeIdentificador(identificador)) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con ese " + rol.getNombreCodigo() + ".");
        }
        if (usuarioDAO.existeCorreo(correoNormalizado)) {
            throw new IllegalArgumentException("Ese correo ya está registrado.");
        }

        Usuario usuario = new Usuario(identificador, nombre.trim(), correoNormalizado,
                SeguridadPassword.hashear(password), rol);
        usuarioDAO.insertar(usuario);
        return usuario;
    }
}
package com.sistema.controlador;

import com.sistema.modelo.Estudiante;
import com.sistema.modelo.Rol;
import com.sistema.modelo.Usuario;
import com.sistema.persistencia.EstudianteDAO;
import com.sistema.persistencia.MatriculaDAO;
import com.sistema.persistencia.UsuarioDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Reglas de negocio de la gestión de estudiantes (vista del administrador). */
public class EstudianteControlador {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();

    /** Datos de acceso de todos los estudiantes (nombre y correo). */
    public List<Usuario> listar() {
        return usuarioDAO.listarPorRol(Rol.ESTUDIANTE);
    }

    /** Carnet -> carrera. */
    public Map<String, String> carreras() {
        Map<String, String> carreras = new HashMap<>();
        for (Estudiante estudiante : estudianteDAO.listarTodos()) {
            carreras.put(estudiante.getCarnet(), estudiante.getCarrera());
        }
        return carreras;
    }

    /** Modifica nombre, carrera y correo. El carnet no cambia. */
    public void modificar(String carnet, String nombre, String carrera, String correo) {
        ValidadorDatos.requerido(carnet, "carnet");
        ValidadorDatos.requerido(nombre, "nombre");
        ValidadorDatos.requerido(carrera, "carrera");
        ValidadorDatos.correo(correo);

        String id = carnet.trim();
        if (estudianteDAO.buscarPorCarnet(id) == null) {
            throw new IllegalArgumentException("No existe ningún estudiante con el carnet " + id + ".");
        }
        String correoNormalizado = correo.trim().toLowerCase();
        Usuario otro = usuarioDAO.buscarPorCorreo(correoNormalizado);
        if (otro != null && !otro.getIdentificador().equals(id)) {
            throw new IllegalArgumentException("Ese correo ya está registrado por otro usuario.");
        }

        usuarioDAO.actualizarDatos(id, nombre.trim(), correoNormalizado);
        estudianteDAO.actualizar(new Estudiante(id, carrera.trim()));
    }

    /** Elimina al estudiante solo si no tiene matrículas (se conserva el historial). */
    public void eliminar(String carnet) {
        ValidadorDatos.requerido(carnet, "carnet");
        String id = carnet.trim();
        if (estudianteDAO.buscarPorCarnet(id) == null) {
            throw new IllegalArgumentException("No existe ningún estudiante con el carnet " + id + ".");
        }
        if (matriculaDAO.existenDeEstudiante(id)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar: el estudiante " + id + " tiene matrículas registradas.");
        }
        estudianteDAO.eliminar(id);
        usuarioDAO.eliminar(id);
    }
}
package com.sistema.controlador;

import com.sistema.modelo.Asignatura;
import com.sistema.modelo.Curso;
import com.sistema.modelo.Rol;
import com.sistema.modelo.Usuario;
import com.sistema.persistencia.AsignaturaDAO;
import com.sistema.persistencia.CursoDAO;
import com.sistema.persistencia.MatriculaDAO;
import com.sistema.persistencia.ProfesorDAO;
import com.sistema.persistencia.UsuarioDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Reglas de negocio de los cursos. */
public class CursoControlador {
    private final CursoDAO cursoDAO = new CursoDAO();
    private final AsignaturaDAO asignaturaDAO = new AsignaturaDAO();
    private final ProfesorDAO profesorDAO = new ProfesorDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final MatriculaDAO matriculaDAO = new MatriculaDAO();

    /** Guarda un curso nuevo si los datos son válidos y el NRC no existe. */
    public void guardar(String nrc, String sigla, String escalafon, String cupos) {
        Curso curso = validar(nrc, sigla, escalafon, cupos);
        if (cursoDAO.buscarPorNrc(curso.getNrc()) != null) {
            throw new IllegalArgumentException("Ya existe un curso con el NRC " + curso.getNrc() + ".");
        }
        cursoDAO.insertar(curso);
    }

    /** Modifica un curso; los cupos no pueden bajar de los estudiantes ya matriculados. */
    public void modificar(String nrc, String sigla, String escalafon, String cupos) {
        Curso curso = validar(nrc, sigla, escalafon, cupos);
        if (cursoDAO.buscarPorNrc(curso.getNrc()) == null) {
            throw new IllegalArgumentException("No existe ningún curso con el NRC " + curso.getNrc() + ".");
        }
        int inscritos = matriculaDAO.contarActivas(curso.getNrc());
        if (curso.getCupos() < inscritos) {
            throw new IllegalArgumentException(
                    "Los cupos no pueden ser menores a los estudiantes ya matriculados (" + inscritos + ").");
        }
        cursoDAO.actualizar(curso);
    }

    /** Elimina el curso solo si no tiene matrículas (activas ni retiradas). */
    public void eliminar(String nrc) {
        ValidadorDatos.requerido(nrc, "NRC");
        String clave = nrc.trim().toUpperCase();
        if (cursoDAO.buscarPorNrc(clave) == null) {
            throw new IllegalArgumentException("No existe ningún curso con el NRC " + clave + ".");
        }
        if (matriculaDAO.existenDeCurso(clave)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar: el curso " + clave + " tiene matrículas registradas.");
        }
        cursoDAO.eliminar(clave);
    }

    public List<Curso> listar() {
        return cursoDAO.listarTodos();
    }

    /** Cursos que imparte un profesor. */
    public List<Curso> listarCursosDe(String escalafon) {
        List<Curso> propios = new ArrayList<>();
        for (Curso curso : cursoDAO.listarTodos()) {
            if (escalafon.equals(curso.getProfesor())) {
                propios.add(curso);
            }
        }
        return propios;
    }

    /** Asignaturas disponibles para el combo del formulario. */
    public List<Asignatura> listarAsignaturas() {
        return asignaturaDAO.listarTodas();
    }

    /** Profesores disponibles para el combo del formulario. */
    public List<Usuario> listarProfesores() {
        return usuarioDAO.listarPorRol(Rol.PROFESOR);
    }

    /** Sigla -> nombre de la asignatura. */
    public Map<String, String> nombresDeAsignaturas() {
        Map<String, String> nombres = new HashMap<>();
        for (Asignatura asignatura : asignaturaDAO.listarTodas()) {
            nombres.put(asignatura.getSigla(), asignatura.getNombre());
        }
        return nombres;
    }

    /** Escalafón -> nombre del profesor. */
    public Map<String, String> nombresDeProfesores() {
        Map<String, String> nombres = new HashMap<>();
        for (Usuario profesor : usuarioDAO.listarPorRol(Rol.PROFESOR)) {
            nombres.put(profesor.getIdentificador(), profesor.getNombreCompleto());
        }
        return nombres;
    }

    /** Valida los campos en el orden del formulario y arma el curso. */
    private Curso validar(String nrc, String sigla, String escalafon, String cupos) {
        ValidadorDatos.requerido(nrc, "NRC");
        ValidadorDatos.requerido(sigla, "asignatura (registre primero una asignatura)");
        ValidadorDatos.requerido(escalafon, "profesor (registre primero un profesor)");
        int numeroCupos = ValidadorDatos.enteroPositivo(cupos, "cupos");

        String claveAsignatura = sigla.trim().toUpperCase();
        String clavePofesor = escalafon.trim();
        if (asignaturaDAO.buscarPorSigla(claveAsignatura) == null) {
            throw new IllegalArgumentException("La asignatura " + claveAsignatura + " no existe.");
        }
        if (profesorDAO.buscarPorEscalafon(clavePofesor) == null) {
            throw new IllegalArgumentException("El profesor " + clavePofesor + " no existe.");
        }
        return new Curso(nrc.trim().toUpperCase(), claveAsignatura, clavePofesor, numeroCupos);
    }
}
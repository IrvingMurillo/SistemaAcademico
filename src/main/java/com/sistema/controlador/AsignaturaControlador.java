package com.sistema.controlador;

import com.sistema.modelo.Asignatura;
import com.sistema.persistencia.AsignaturaDAO;
import com.sistema.persistencia.CursoDAO;
import java.util.List;

/** Reglas de negocio de las asignaturas. */
public class AsignaturaControlador {
    private final AsignaturaDAO asignaturaDAO = new AsignaturaDAO();
    private final CursoDAO cursoDAO = new CursoDAO();

    /** Guarda una asignatura nueva si los datos son válidos y la sigla no existe. */
    public void guardar(String sigla, String nombre, String validez) {
        Asignatura asignatura = validar(sigla, nombre, validez);
        if (asignaturaDAO.buscarPorSigla(asignatura.getSigla()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una asignatura con la sigla " + asignatura.getSigla() + ".");
        }
        asignaturaDAO.insertar(asignatura);
    }

    /** Modifica el nombre y la validez de una asignatura existente. */
    public void modificar(String sigla, String nombre, String validez) {
        Asignatura asignatura = validar(sigla, nombre, validez);
        if (asignaturaDAO.buscarPorSigla(asignatura.getSigla()) == null) {
            throw new IllegalArgumentException(
                    "No existe ninguna asignatura con la sigla " + asignatura.getSigla() + ".");
        }
        asignaturaDAO.actualizar(asignatura);
    }

    /** Elimina la asignatura solo si ningún curso la usa. */
    public void eliminar(String sigla) {
        ValidadorDatos.requerido(sigla, "sigla");
        String clave = sigla.trim().toUpperCase();
        if (asignaturaDAO.buscarPorSigla(clave) == null) {
            throw new IllegalArgumentException("No existe ninguna asignatura con la sigla " + clave + ".");
        }
        if (cursoDAO.existeCursoConAsignatura(clave)) {
            throw new IllegalArgumentException(
                    "No se puede eliminar: existen cursos asociados a la asignatura " + clave + ".");
        }
        asignaturaDAO.eliminar(clave);
    }

    public List<Asignatura> listar() {
        return asignaturaDAO.listarTodas();
    }

    /** Valida los campos en el orden del formulario y arma la asignatura. */
    private Asignatura validar(String sigla, String nombre, String validez) {
        ValidadorDatos.requerido(sigla, "sigla");
        ValidadorDatos.requerido(nombre, "nombre");
        int unidades = ValidadorDatos.enteroPositivo(validez, "validez");
        return new Asignatura(sigla.trim().toUpperCase(), nombre.trim(), unidades);
    }
}
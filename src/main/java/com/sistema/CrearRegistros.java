package com.sistema;

import com.sistema.controlador.AsignaturaControlador;
import com.sistema.controlador.AutenticacionControlador;
import com.sistema.controlador.CursoControlador;
import com.sistema.controlador.MatriculaControlador;
import com.sistema.persistencia.ConexionMongoDB;

/**
 * Script de inicialización: carga datos de prueba (administradores, profesores, estudiantes,
 * asignaturas, cursos y matrículas). Ejecutarlo después de LimpiarBaseDatos.
 */
public class CrearRegistros {
    private static final AutenticacionControlador AUTENTICACION = new AutenticacionControlador();
    private static final AsignaturaControlador ASIGNATURAS = new AsignaturaControlador();
    private static final CursoControlador CURSOS = new CursoControlador();
    private static final MatriculaControlador MATRICULAS = new MatriculaControlador();

    public static void main(String[] args) {
        System.out.println("=== INICIANDO CREACIÓN MASIVA DE REGISTROS ===");
        ConexionMongoDB.prepararBaseDatos();

        crearAdministradores();
        crearAsignaturas();
        crearProfesores();
        crearEstudiantes();
        crearCursos();
        crearMatriculas();

        System.out.println("\n=== PROCESO DE CREACIÓN MASIVA FINALIZADO ===");
    }

    /** Ejecuta una acción e imprime si funcionó o por qué falló. */
    private static void intentar(String descripcion, Runnable accion) {
        try {
            accion.run();
            System.out.println("[+] " + descripcion);
        } catch (RuntimeException e) {
            System.out.println("[-] " + descripcion + ": " + e.getMessage());
        }
    }

    private static void crearAdministradores() {
        System.out.println("\n--- 1. Administradores (credencial, nombre, correo, contraseña) ---");
        String[][] datos = {
            {"ADM-IRVING", "Irving", "irving@sistema.com", "admin123"},
            {"ADM-ISAIAS", "Isaías", "isaias@sistema.com", "admin123"},
            {"ADM-KEVIN", "Kevin", "kevin@sistema.com", "admin123"},
            {"ADM-WALMER", "Walmer", "walmer@sistema.com", "admin123"}
        };
        for (String[] d : datos) {
            intentar("Administrador " + d[0],
                    () -> AUTENTICACION.registrarAdministrador(d[0], d[1], d[2], d[3]));
        }
    }

    private static void crearAsignaturas() {
        System.out.println("\n--- 2. Asignaturas (sigla, nombre, validez en U.V.) ---");
        String[][] datos = {
            {"IS-101", "Programación Orientada a Objetos", "4"},
            {"IS-102", "Estructuras de Datos", "4"},
            {"IS-103", "Bases de Datos NoSQL", "3"},
            {"IS-104", "Ingeniería de Software", "4"}
        };
        for (String[] d : datos) {
            intentar("Asignatura " + d[0], () -> ASIGNATURAS.guardar(d[0], d[1], d[2]));
        }
    }

    private static void crearProfesores() {
        System.out.println("\n--- 3. Profesores (escalafón, nombre, especialización, correo, contraseña) ---");
        String[][] datos = {
            {"PROF-001", "Ing. Roberto Morales", "Ingeniería de Software", "roberto.morales@sistema.com", "prof123"},
            {"PROF-002", "Dra. Elena Castaneda", "Bases de Datos Avanzadas", "elena.castaneda@sistema.com", "prof123"},
            {"PROF-003", "Msc. Fernando Quintanilla", "Estructuras y Algoritmos", "fernando.quintanilla@sistema.com", "prof123"},
            {"PROF-004", "Licda. Gabriela Solórzano", "Desarrollo Orientado a Objetos", "gabriela.solorzano@sistema.com", "prof123"}
        };
        for (String[] d : datos) {
            intentar("Profesor " + d[0], () -> AUTENTICACION.registrarProfesor(d[0], d[1], d[2], d[3], d[4]));
        }
    }

    private static void crearEstudiantes() {
        System.out.println("\n--- 4. Estudiantes (carnet, nombre, carrera, correo, contraseña) ---");
        String[][] datos = {
            {"EST2026001", "Carlos Alberto Méndez", "Ingeniería en Sistemas", "carlos.mendez@ues.edu.sv", "est123"},
            {"EST2026002", "Sofía Valentina Rivas", "Ingeniería en Sistemas", "sofia.rivas@ues.edu.sv", "est123"},
            {"EST2026003", "Diego Alejandro Torres", "Ingeniería en Sistemas", "diego.torres@ues.edu.sv", "est123"},
            {"EST2026004", "Valeria Nicole Guzmán", "Ingeniería en Sistemas", "valeria.guzman@ues.edu.sv", "est123"},
            {"EST2026005", "Andrés Felipe Pineda", "Ingeniería en Sistemas", "andres.pineda@ues.edu.sv", "est123"},
            {"EST2026006", "Camila Fernanda López", "Ingeniería en Sistemas", "camila.lopez@ues.edu.sv", "est123"},
            {"EST2026007", "Mateo Sebastián Aguilar", "Ingeniería en Sistemas", "mateo.aguilar@ues.edu.sv", "est123"},
            {"EST2026008", "Daniela Sofía Portillo", "Ingeniería en Sistemas", "daniela.portillo@ues.edu.sv", "est123"},
            {"EST2026009", "Javier Eduardo Zelaya", "Ingeniería en Sistemas", "javier.zelaya@ues.edu.sv", "est123"},
            {"EST2026010", "Lucía Marisol Cabrera", "Ingeniería en Sistemas", "lucia.cabrera@ues.edu.sv", "est123"},
            {"EST2026011", "Sebastián José Meléndez", "Ingeniería en Sistemas", "sebastian.melendez@ues.edu.sv", "est123"},
            {"EST2026012", "Mariana Isabel Alas", "Ingeniería en Sistemas", "mariana.alas@ues.edu.sv", "est123"},
            {"EST2026013", "Alejandro David Navarro", "Ingeniería en Sistemas", "alejandro.navarro@ues.edu.sv", "est123"},
            {"EST2026014", "Paola Andrea Sandoval", "Ingeniería en Sistemas", "paola.sandoval@ues.edu.sv", "est123"},
            {"EST2026015", "Ricardo Enrique Orellana", "Ingeniería en Sistemas", "ricardo.orellana@ues.edu.sv", "est123"},
            {"EST2026016", "Natalia Beatriz Vásquez", "Ingeniería en Sistemas", "natalia.vasquez@ues.edu.sv", "est123"}
        };
        for (String[] d : datos) {
            intentar("Estudiante " + d[0], () -> AUTENTICACION.registrarEstudiante(d[0], d[1], d[2], d[3], d[4]));
        }
    }

    private static void crearCursos() {
        System.out.println("\n--- 5. Cursos (NRC, sigla de la asignatura, escalafón, cupos) ---");
        String[][] datos = {
            {"101", "IS-101", "PROF-001", "5"},
            {"102", "IS-102", "PROF-002", "4"},
            {"103", "IS-103", "PROF-003", "4"},
            {"104", "IS-104", "PROF-004", "3"},
            {"105", "IS-101", "PROF-004", "2"}, // otra sección de la misma materia
            {"106", "IS-102", "PROF-001", "6"}
        };
        for (String[] d : datos) {
            intentar("Curso " + d[0], () -> CURSOS.guardar(d[0], d[1], d[2], d[3]));
        }
    }

    private static void crearMatriculas() {
        System.out.println("\n--- 6. Matrículas (carnet, NRC) ---");
        String[][] datos = {
            {"EST2026001", "101"}, {"EST2026002", "101"}, {"EST2026003", "101"},
            {"EST2026001", "102"}, {"EST2026004", "102"}, {"EST2026005", "102"}, {"EST2026006", "102"}, // 102 queda lleno
            {"EST2026002", "103"}, {"EST2026007", "103"}, {"EST2026008", "103"},
            {"EST2026009", "104"}, {"EST2026010", "104"},
            {"EST2026011", "105"}, {"EST2026012", "105"}, // 105 queda lleno
            {"EST2026013", "106"}, {"EST2026014", "106"}, {"EST2026015", "106"}, {"EST2026016", "106"}
        };
        for (String[] d : datos) {
            intentar("Matrícula " + d[0] + " en el curso " + d[1], () -> MATRICULAS.matricular(d[0], d[1]));
        }
    }
}
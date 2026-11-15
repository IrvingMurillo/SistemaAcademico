package com.sistema;

import com.sistema.controlador.AutenticacionControlador;
import com.sistema.modelo.Administrador;
import com.sistema.modelo.Asignatura;
import com.sistema.modelo.Curso;
import com.sistema.modelo.Estudiante;
import com.sistema.modelo.Profesor;
import com.sistema.persistencia.AdministradorDAO;
import com.sistema.persistencia.AsignaturaDAO;
import com.sistema.persistencia.CursoDAO;
import com.sistema.persistencia.EstudianteDAO;
import com.sistema.persistencia.ProfesorDAO;

public class CrearRegistros {

    public static void main(String[] args) {
        AutenticacionControlador controlador = new AutenticacionControlador();
        
        AdministradorDAO adminDAO = new AdministradorDAO();
        AsignaturaDAO asignaturaDAO = new AsignaturaDAO();
        ProfesorDAO profesorDAO = new ProfesorDAO();
        EstudianteDAO estudianteDAO = new EstudianteDAO();
        CursoDAO cursoDAO = new CursoDAO();

        System.out.println("=== INICIANDO CREACIÓN MASIVA DE REGISTROS ===\n");

        // =========================================================================
        // 1. CREAR 4 ADMINISTRADORES (Credencial, Nombre, Correo, Contraseña)
        // =========================================================================
        System.out.println("--- 1. Registrando Administradores ---");
        String[][] administradores = {
            {"ADM-IRVING", "Irving", "irving@sistema.com", "admin123"},
            {"ADM-ISAIAS", "Isaías", "isaias@sistema.com", "admin123"},
            {"ADM-KEVIN", "Kevin", "kevin@sistema.com", "admin123"},
            {"ADM-WALMER", "Walmer", "walmer@sistema.com", "admin123"}
        };

        for (String[] adminData : administradores) {
            try {
                controlador.registrarAdministrador(adminData[0], adminData[1], adminData[2], adminData[3]);
                if (adminDAO.buscarPorCredencial(adminData[0]) == null) {
                    adminDAO.insertar(new Administrador(adminData[0]));
                }
                System.out.println("[+] Administrador creado: " + adminData[1] + " (" + adminData[0] + ")");
            } catch (Exception e) {
                System.out.println("[-] Error con Administrador " + adminData[0] + ": " + e.getMessage());
            }
        }

        // =========================================================================
        // 2. CREAR 4 ASIGNATURAS (Sigla, Nombre, Validez / Numérico)
        // =========================================================================
        System.out.println("\n--- 2. Registrando Asignaturas ---");
        String[][] asignaturas = {
            {"IS-101", "Programación Orientada a Objetos", "4"},
            {"IS-102", "Estructuras de Datos", "4"},
            {"IS-103", "Bases de Datos NoSQL", "3"},
            {"IS-104", "Ingeniería de Software", "4"}
        };

        for (String[] asig : asignaturas) {
            try {
                Asignatura asignatura = new Asignatura(asig[0], asig[1], asig[2]);
                boolean insertada = asignaturaDAO.insertar(asignatura);
                if (insertada) {
                    System.out.println("[+] Asignatura creada: " + asignatura.toString() + " (Validez: " + asig[2] + ")");
                } else {
                    System.out.println("[!] La asignatura " + asig[0] + " ya existe.");
                }
            } catch (Exception e) {
                System.out.println("[-] Error con Asignatura " + asig[0] + ": " + e.getMessage());
            }
        }

        // =========================================================================
        // 3. CREAR 4 PROFESORES (Escalafón, Nombre, Especialización, Correo, Contraseña)
        // =========================================================================
        System.out.println("\n--- 3. Registrando Profesores ---");
        String[][] profesores = {
            {"PROF-001", "Ing. Roberto Morales", "Ingeniería de Software", "roberto.morales@sistema.com", "prof123"},
            {"PROF-002", "Dra. Elena Castaneda", "Bases de Datos Avanzadas", "elena.castaneda@sistema.com", "prof123"},
            {"PROF-003", "Msc. Fernando Quintanilla", "Estructuras y Algoritmos", "fernando.quintanilla@sistema.com", "prof123"},
            {"PROF-004", "Licda. Gabriela Solórzano", "Desarrollo Orientado a Objetos", "gabriela.solorzano@sistema.com", "prof123"}
        };

        for (String[] profData : profesores) {
            try {
                controlador.registrarProfesor(profData[0], profData[1], profData[2], profData[3], profData[4]);
                if (profesorDAO.buscarPorEscalafon(profData[0]) == null) {
                    profesorDAO.insertar(new Profesor(profData[0], profData[2]));
                }
                System.out.println("[+] Profesor creado: " + profData[1] + " (Escalafón: " + profData[0] + ")");
            } catch (Exception e) {
                System.out.println("[-] Error con Profesor " + profData[0] + ": " + e.getMessage());
            }
        }

        // =========================================================================
        // 4. CREAR 16 ESTUDIANTES (Carnet, Nombre, Carrera, Correo, Contraseña)
        // =========================================================================
        System.out.println("\n--- 4. Registrando Estudiantes ---");
        String[][] estudiantes = {
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

        for (String[] estData : estudiantes) {
            try {
                controlador.registrarEstudiante(estData[0], estData[1], estData[2], estData[3], estData[4]);
                if (estudianteDAO.buscarPorCarnet(estData[0]) == null) {
                    estudianteDAO.insertar(new Estudiante(estData[0], estData[2]));
                }
                System.out.println("[+] Estudiante creado: " + estData[1] + " (Carnet: " + estData[0] + ")");
            } catch (Exception e) {
                System.out.println("[-] Error con Estudiante " + estData[0] + ": " + e.getMessage());
            }
        }

        // =========================================================================
        // 5. CREAR 4 CURSOS (NRC, Asignatura, Profesor, Cupos) -> Totalmente relacionados
        // =========================================================================
        System.out.println("\n--- 5. Registrando Cursos ---");
        String[][] cursos = {
            {"101", "IS-101", "PROF-001", "4"},
            {"102", "IS-102", "PROF-002", "4"},
            {"103", "IS-103", "PROF-003", "4"},
            {"104", "IS-104", "PROF-004", "4"}
        };

        for (String[] curData : cursos) {
            try {
                Curso curso = new Curso(curData[0], curData[1], curData[2], Integer.parseInt(curData[3]));
                boolean insertado = cursoDAO.insertar(curso);
                if (insertado) {
                    System.out.println("[+] Curso creado - NRC: " + curData[0] + " | Asignatura: " + curData[1] + " | Prof: " + curData[2] + " | Cupos: " + curData[3]);
                } else {
                    System.out.println("[!] El curso con NRC " + curData[0] + " ya existe.");
                }
            } catch (Exception e) {
                System.out.println("[-] Error con Curso NRC " + curData[0] + ": " + e.getMessage());
            }
        }

        System.out.println("\n=== PROCESO DE CREACIÓN MASIVA FINALIZADO ===");
    }
}
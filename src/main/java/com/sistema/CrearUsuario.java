package com.sistema;

import com.sistema.controlador.AutenticacionControlador;

public class CrearUsuario {

    public static void main(String[] args) {
        AutenticacionControlador controlador = new AutenticacionControlador();

        // Instrucción: Descomenta el bloque del rol que quieras crear,
        // cambia los valores de las variables y dale a Run File.


        /* 
         * Bloque para Administrador.
         * Ejemplo de perfil: 
         * - Credencial: ADM-001
         * - Nombre: María Administradora
         * - Correo: admin@sistema.com
         * - Contraseña: admin1234
         */
        /*
        String credencial = "ADM-001";
        String nombre = "María Administradora";
        String correo = "admin@sistema.com";
        String password = "admin1234";

        try {
            controlador.registrarAdministrador(credencial, nombre, correo, password);
            System.out.println("Administrador creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        */


        /* 
         * Bloque para Estudiante.
         * Ejemplo de perfil: 
         * - Carnet: EST-2026
         * - Nombre: Carlos Estudiante
         * - Correo: estudiante@sistema.com
         * - Contraseña: password123
         */
        /*
        String carnet = "EST-2026";
        String nombre = "Carlos Estudiante";
        String correo = "estudiante@sistema.com";
        String password = "password123";

        try {
            controlador.registrarEstudiante(carnet, nombre, correo, password);
            System.out.println("Estudiante creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        */


        /* 
         * Bloque para Profesor.
         * Ejemplo de perfil: 
         * - Escalafón: PROF-100
         * - Nombre: Ana Profesora
         * - Correo: profesor@sistema.com
         * - Contraseña: password123
         */
        /*
        String escalafon = "PROF-100";
        String nombre = "Ana Profesora";
        String correo = "profesor@sistema.com";
        String password = "password123";

        try {
            controlador.registrarProfesor(escalafon, nombre, correo, password);
            System.out.println("Profesor creado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        */

    }
}
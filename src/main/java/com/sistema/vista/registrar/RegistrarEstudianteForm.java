package com.sistema.vista.registrar;

/** Formulario de registro de estudiantes: carnet, nombre, carrera, correo y contraseña. */
public class RegistrarEstudianteForm extends RegistrarBaseForm {

    public RegistrarEstudianteForm() {
        super("Carnet:", "Carrera:");
    }

    @Override
    protected void guardar() {
        controlador.registrarEstudiante(txtCodigo.getText(), txtNombre.getText(),
                txtExtra.getText(), txtCorreo.getText(), password());
    }
}
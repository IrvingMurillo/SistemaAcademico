package com.sistema.vista.registrar;

/** Formulario de registro de profesores: escalafón, nombre, especialización, correo y contraseña. */
public class RegistrarProfesorForm extends RegistrarBaseForm {

    public RegistrarProfesorForm() {
        super("Escalafón:", "Especialización:");
    }

    @Override
    protected void guardar() {
        controlador.registrarProfesor(txtCodigo.getText(), txtNombre.getText(),
                txtExtra.getText(), txtCorreo.getText(), password());
    }
}
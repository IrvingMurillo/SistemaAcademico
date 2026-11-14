package com.sistema.vista.registrar;

public class RegistrarEstudianteForm extends RegistrarBaseForm {

    public RegistrarEstudianteForm(Runnable alTerminar) {
        super("Carnet:", "Carrera:", alTerminar);
    }

    @Override
    protected void guardar() {
        controlador.registrarEstudiante(txtCodigo.getText(), txtNombre.getText(),
                txtExtra.getText(), txtCorreo.getText(), password());
    }
}
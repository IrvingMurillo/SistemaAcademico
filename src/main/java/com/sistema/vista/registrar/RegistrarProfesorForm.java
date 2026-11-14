package com.sistema.vista.registrar;

public class RegistrarProfesorForm extends RegistrarBaseForm {

    public RegistrarProfesorForm(Runnable alTerminar) {
        super("Escalafón:", "Especialización:", alTerminar);
    }

    @Override
    protected void guardar() {
        controlador.registrarProfesor(txtCodigo.getText(), txtNombre.getText(),
                txtExtra.getText(), txtCorreo.getText(), password());
    }
}
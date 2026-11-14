package com.sistema.vista.registrar;

public class RegistrarAdministradorForm extends RegistrarBaseForm {

    public RegistrarAdministradorForm(Runnable alTerminar) {
        super("Credencial:", null, alTerminar);
    }

    @Override
    protected void guardar() {
        controlador.registrarAdministrador(txtCodigo.getText(), txtNombre.getText(),
                txtCorreo.getText(), password());
    }
}
package com.sistema.vista.registrar;

public class RegistrarAdministradorForm extends RegistrarBaseForm {

    public RegistrarAdministradorForm() {
        super("Credencial:", null);
    }

    @Override
    protected void guardar() {
        controlador.registrarAdministrador(txtCodigo.getText(), txtNombre.getText(),
                txtCorreo.getText(), password());
    }
}
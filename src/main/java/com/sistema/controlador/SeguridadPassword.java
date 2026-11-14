package com.sistema.controlador;

import org.mindrot.jbcrypt.BCrypt;

/** La contraseña nunca se guarda en texto plano: solo su hash BCrypt. */
public class SeguridadPassword {

    public static String hashear(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verificar(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}
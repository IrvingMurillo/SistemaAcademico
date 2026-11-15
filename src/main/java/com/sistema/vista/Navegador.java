package com.sistema.vista;

import javax.swing.JFrame;

/** Garantiza que solo haya una ventana abierta a la vez. */
public class Navegador {
    private static JFrame actual;

    public static void irA(JFrame nueva) {
        if (actual != null) {
            actual.dispose(); // cierra la ventana anterior
        }
        actual = nueva;
        actual.setVisible(true);
    }
}
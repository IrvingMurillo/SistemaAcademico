package com.sistema.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/** Ventana base: centrada, sin la barra del sistema y con botones propios de minimizar y cerrar. */
public abstract class BaseForm extends JFrame {
    public static final int ANCHO = 600;
    public static final int ALTO = 450;

    /** Cada ventana agrega aquí sus componentes. */
    protected final JPanel contenido = new JPanel(new BorderLayout());

    /** Ventana con el tamaño estándar (600x450). */
    protected BaseForm(String titulo) {
        this(titulo, ANCHO, ALTO);
    }

    /** Ventana con un tamaño propio (la usan los POV, que muestran tablas). */
    protected BaseForm(String titulo, int ancho, int alto) {
        super(titulo); // el título solo se ve en la barra de tareas
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(ancho, alto);
        setResizable(false);
        setLocationRelativeTo(null);
        getRootPane().setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JButton btnMinimizar = new JButton("_");
        JButton btnCerrar = new JButton("X");
        btnMinimizar.addActionListener(e -> setExtendedState(ICONIFIED));
        btnCerrar.addActionListener(e -> System.exit(0));

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        barra.add(btnMinimizar);
        barra.add(btnCerrar);

        add(barra, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
    }
}
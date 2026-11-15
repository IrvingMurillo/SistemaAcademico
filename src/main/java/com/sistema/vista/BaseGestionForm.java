package com.sistema.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel base de las pantallas del administrador: campos y botones arriba, tabla con buscador abajo.
 * Cada pantalla indica sus campos y botones, y cómo leer sus datos de MongoDB.
 */
public abstract class BaseGestionForm extends JPanel {
    protected final TablaConsulta tabla;
    /** Zona central: contiene la tabla y, si hace falta, otras tablas. */
    protected final JPanel centro = new JPanel(new GridLayout(0, 1, 0, 8));

    protected BaseGestionForm(String... columnas) {
        super(new BorderLayout(0, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tabla = new TablaConsulta(columnas);
        tabla.alSeleccionar(this::cargarSeleccion);
    }

    /** Coloca los campos y los botones arriba y la tabla al centro. */
    protected void armar(JPanel campos, JButton... botones) {
        JPanel barraBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton boton : botones) {
            barraBotones.add(boton);
        }
        JPanel norte = new JPanel(new BorderLayout());
        norte.add(campos, BorderLayout.CENTER);
        norte.add(barraBotones, BorderLayout.SOUTH);

        centro.add(tabla);
        add(norte, BorderLayout.NORTH);
        add(centro, BorderLayout.CENTER);
    }

    /** Arma el bloque de campos a partir de pares: "Etiqueta:", campo, "Etiqueta:", campo... */
    protected static JPanel campos(Object... pares) {
        JPanel cuadricula = new JPanel(new GridLayout(0, 2, 10, 6));
        for (int i = 0; i < pares.length; i += 2) {
            cuadricula.add(new JLabel((String) pares[i]));
            cuadricula.add((JComponent) pares[i + 1]);
        }
        JPanel contenedor = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contenedor.add(cuadricula);
        return contenedor;
    }

    /** Crea un botón que ejecuta la acción al hacer clic. */
    protected static JButton boton(String texto, Runnable accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    /** Vuelve a leer los datos de MongoDB; los errores se muestran al usuario. */
    public final void recargar() {
        Mensajes.ejecutar(this, this::leerDatos);
    }

    /** Lee de MongoDB lo que muestra la pantalla y rellena la tabla. */
    protected abstract void leerDatos();

    /** Copia la fila seleccionada de la tabla a los campos (no hace nada si no hay selección). */
    protected abstract void cargarSeleccion();
}
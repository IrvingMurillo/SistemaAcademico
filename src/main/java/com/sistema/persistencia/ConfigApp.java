package com.sistema.persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/** Lee los datos de config.properties (que no se sube al repositorio). */
public class ConfigApp {
    private static final Properties propiedades = new Properties();

    public static String get(String clave) {
        if (propiedades.isEmpty()) {
            try (FileInputStream archivo = new FileInputStream("config.properties")) {
                propiedades.load(archivo);
            } catch (IOException e) {
                throw new IllegalStateException("No se encontró config.properties en la carpeta del proyecto", e);
            }
        }
        
        String valor = propiedades.getProperty(clave);
        
        // Limpia comillas dobles o simples accidentales en el valor leído
        if (valor != null) {
            valor = valor.trim();
            if ((valor.startsWith("\"") && valor.endsWith("\"")) || (valor.startsWith("'") && valor.endsWith("'"))) {
                valor = valor.substring(1, valor.length() - 1);
            }
        }
        
        return valor;
    }
}
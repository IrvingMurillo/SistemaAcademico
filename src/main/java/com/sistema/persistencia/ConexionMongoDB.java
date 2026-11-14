package com.sistema.persistencia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class ConexionMongoDB {
    // CORREGIDO: Coincide exactamente con el nombre de la base de datos en Atlas ("SistemaAcademico")
    private static final String NOMBRE_DB = "SistemaAcademico";

    private static MongoClient cliente = null;
    private static MongoDatabase database = null;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                // Lee directamente la URI completa desde tu archivo config.properties
                cliente = MongoClients.create(ConfigApp.get("MONGODB_URI"));
                
                MongoDatabase db = cliente.getDatabase(NOMBRE_DB);
                db.runCommand(new Document("ping", 1)); // comprueba que de verdad hay conexión
                database = db;
            } catch (RuntimeException e) {
                cerrarConexion();
                throw new IllegalStateException("No se pudo conectar a MongoDB", e);
            }
        }
        return database;
    }

    public static void cerrarConexion() {
        if (cliente != null) {
            cliente.close();
        }
        cliente = null;
        database = null;
    }
}
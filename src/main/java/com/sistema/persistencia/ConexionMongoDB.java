package com.sistema.persistencia;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;

/** Conexión única a MongoDB Atlas. La URI se lee de config.properties. */
public class ConexionMongoDB {
    // Debe coincidir con el nombre de la base de datos en Atlas
    private static final String NOMBRE_DB = "SistemaAcademico";

    private static MongoClient cliente = null;
    private static MongoDatabase database = null;

    /** Devuelve la base de datos, conectando la primera vez. Lanza IllegalStateException si falla. */
    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
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

    /** Crea los índices únicos: la base de datos misma rechaza códigos repetidos. */
    public static void prepararBaseDatos() {
        MongoDatabase db = getDatabase();
        IndexOptions unico = new IndexOptions().unique(true);
        db.getCollection("usuarios").createIndex(Indexes.ascending("identificador"), unico);
        db.getCollection("usuarios").createIndex(Indexes.ascending("correo"), unico);
        db.getCollection("asignaturas").createIndex(Indexes.ascending("sigla"), unico);
        db.getCollection("cursos").createIndex(Indexes.ascending("nrc"), unico);
        db.getCollection("matriculas").createIndex(Indexes.ascending("codigo"), unico);
    }

    /** Cierra la conexión y la deja lista para reconectar. */
    public static void cerrarConexion() {
        if (cliente != null) {
            cliente.close();
        }
        cliente = null;
        database = null;
    }
}
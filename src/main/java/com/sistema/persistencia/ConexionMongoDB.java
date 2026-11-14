package com.sistema.controlador;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexionMongoDB {
    private static final String URI = "mongodb+srv://irvingstevenlopezmurillo3_db_user:gWAHsIzhusGB0Iu2@cluster0.zahrfg1.mongodb.net/?retryWrites=true&w=majority";
    private static final String NOMBRE_DB = "sistema_academico";
    
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(NOMBRE_DB);
                System.out.println("Ha conectado");
            } catch (Exception e) {
                System.err.println("Ha fracasado la conexión");
            }
        }
        return database;
    }
    
    public static void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }

    public static void main(String[] args) {
        getDatabase();
    }
}
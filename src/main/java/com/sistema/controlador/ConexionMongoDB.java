package com.sistema.controlador;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class ConexionMongoDB {
    // Pega aquí tu URI de MongoDB Atlas (reemplaza <usuario>, <password> y el host de tu clúster)
    //private static final String URI = "mongodb+srv://<usuario>:<password>@cluster0.xxxxx.mongodb.net/?retryWrites=true&w=majority";
    private static final String URI = "mongodb://localhost:27017/";
    private static final String NOMBRE_DB = "sistema_academico";
    
    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    public static MongoDatabase getDatabase() {
        if (database == null) {
            try {
                mongoClient = MongoClients.create(URI);
                database = mongoClient.getDatabase(NOMBRE_DB);
                System.out.println("¡Conexión exitosa a MongoDB Atlas!");
            } catch (Exception e) {
                System.err.println("Error al conectar con MongoDB: " + e.getMessage());
            }
        }
        return database;
    }
    
    public static void cerrarConexion() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
            System.out.println("Conexión a MongoDB cerrada.");
        }
    }

    public static void main(String[] args) {
        MongoDatabase db = getDatabase();
        if (db != null) {
            System.out.println("Colecciones encontradas en la base de datos:");
            for (String nombreColeccion : db.listCollectionNames()) {
                System.out.println("- " + nombreColeccion);
            }
        }
    }
}
package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.sistema.modelo.Administrador;
import org.bson.Document;

public class AdministradorDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("administradores");
    }

    public void insertar(Administrador administrador) {
        coleccion().insertOne(new Document("credencial", administrador.getCredencial()));
    }

    /** Devuelve el administrador, o null si no existe. */
    public Administrador buscarPorCredencial(String credencial) {
        Document doc = coleccion().find(eq("credencial", credencial)).first();
        if (doc == null) {
            return null;
        }
        return new Administrador(doc.getString("credencial"));
    }
}
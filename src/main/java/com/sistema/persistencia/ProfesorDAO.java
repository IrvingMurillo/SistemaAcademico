package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.sistema.modelo.Profesor;
import org.bson.Document;

/** Acceso a la colección "profesores": datos propios del profesor. */
public class ProfesorDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("profesores");
    }

    public void insertar(Profesor profesor) {
        coleccion().insertOne(new Document("escalafon", profesor.getEscalafon())
                .append("especializacion", profesor.getEspecializacion()));
    }

    /** Devuelve el profesor, o null si no existe. */
    public Profesor buscarPorEscalafon(String escalafon) {
        Document doc = coleccion().find(eq("escalafon", escalafon)).first();
        if (doc == null) {
            return null;
        }
        return new Profesor(doc.getString("escalafon"), doc.getString("especializacion"));
    }
}
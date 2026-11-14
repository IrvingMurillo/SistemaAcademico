package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.sistema.modelo.Estudiante;
import org.bson.Document;

public class EstudianteDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("estudiantes");
    }

    public void insertar(Estudiante estudiante) {
        coleccion().insertOne(new Document("carnet", estudiante.getCarnet())
                .append("carrera", estudiante.getCarrera()));
    }

    /** Devuelve el estudiante, o null si no existe. */
    public Estudiante buscarPorCarnet(String carnet) {
        Document doc = coleccion().find(eq("carnet", carnet)).first();
        if (doc == null) {
            return null;
        }
        return new Estudiante(doc.getString("carnet"), doc.getString("carrera"));
    }
}
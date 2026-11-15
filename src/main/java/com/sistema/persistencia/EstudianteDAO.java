package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.sistema.modelo.Estudiante;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/** Acceso a la colección "estudiantes": datos propios del estudiante. */
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
        return (doc == null) ? null : aEstudiante(doc);
    }

    public List<Estudiante> listarTodos() {
        List<Estudiante> lista = new ArrayList<>();
        for (Document doc : coleccion().find()) {
            lista.add(aEstudiante(doc));
        }
        return lista;
    }

    /** Actualiza la carrera del estudiante. */
    public void actualizar(Estudiante estudiante) {
        coleccion().updateOne(eq("carnet", estudiante.getCarnet()),
                Updates.set("carrera", estudiante.getCarrera()));
    }

    public void eliminar(String carnet) {
        coleccion().deleteOne(eq("carnet", carnet));
    }

    private Estudiante aEstudiante(Document doc) {
        return new Estudiante(doc.getString("carnet"), doc.getString("carrera"));
    }
}
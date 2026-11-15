package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.sistema.modelo.Curso;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

public class CursoDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("cursos");
    }

    public Curso buscarPorNrc(String nrc) {
        Document doc = coleccion().find(eq("nrc", nrc)).first();
        if (doc == null) {
            return null;
        }
        return new Curso(
                doc.getString("nrc"),
                doc.getString("asignatura"),
                doc.getString("profesor"),
                doc.getInteger("cupos", 0)
        );
    }

    public boolean existeCursoConAsignatura(String siglaAsignatura) {
        return coleccion().find(eq("asignatura", siglaAsignatura)).first() != null;
    }

    public boolean insertar(Curso curso) {
        if (buscarPorNrc(curso.getNrc()) != null) {
            return false;
        }
        Document doc = new Document("nrc", curso.getNrc())
                .append("asignatura", curso.getAsignatura())
                .append("profesor", curso.getProfesor())
                .append("cupos", curso.getCupos());
        coleccion().insertOne(doc);
        return true;
    }

    public boolean modificar(Curso curso) {
        if (buscarPorNrc(curso.getNrc()) == null) {
            return false;
        }
        coleccion().updateOne(
                eq("nrc", curso.getNrc()),
                Updates.combine(
                        Updates.set("asignatura", curso.getAsignatura()),
                        Updates.set("profesor", curso.getProfesor()),
                        Updates.set("cupos", curso.getCupos())
                )
        );
        return true;
    }

    public boolean eliminar(String nrc) {
        return coleccion().deleteOne(eq("nrc", nrc)).getDeletedCount() > 0;
    }

    public List<Curso> listarTodos() {
        List<Curso> lista = new ArrayList<>();
        for (Document doc : coleccion().find()) {
            lista.add(new Curso(
                    doc.getString("nrc"),
                    doc.getString("asignatura"),
                    doc.getString("profesor"),
                    doc.getInteger("cupos", 0)
            ));
        }
        return lista;
    }
}

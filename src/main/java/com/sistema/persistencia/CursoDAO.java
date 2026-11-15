package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.sistema.modelo.Curso;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/** Acceso a la colección "cursos". */
public class CursoDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("cursos");
    }

    public void insertar(Curso curso) {
        coleccion().insertOne(new Document("nrc", curso.getNrc())
                .append("asignatura", curso.getAsignatura())
                .append("profesor", curso.getProfesor())
                .append("cupos", curso.getCupos()));
    }

    /** Devuelve el curso, o null si no existe. */
    public Curso buscarPorNrc(String nrc) {
        Document doc = coleccion().find(eq("nrc", nrc)).first();
        return (doc == null) ? null : aCurso(doc);
    }

    /** ¿Hay algún curso de esa asignatura? Sirve para impedir borrar asignaturas en uso. */
    public boolean existeCursoConAsignatura(String sigla) {
        return coleccion().countDocuments(eq("asignatura", sigla)) > 0;
    }

    /** Actualiza asignatura, profesor y cupos del curso con ese NRC. */
    public void actualizar(Curso curso) {
        coleccion().updateOne(eq("nrc", curso.getNrc()), Updates.combine(
                Updates.set("asignatura", curso.getAsignatura()),
                Updates.set("profesor", curso.getProfesor()),
                Updates.set("cupos", curso.getCupos())));
    }

    public void eliminar(String nrc) {
        coleccion().deleteOne(eq("nrc", nrc));
    }

    public List<Curso> listarTodos() {
        List<Curso> lista = new ArrayList<>();
        for (Document doc : coleccion().find()) {
            lista.add(aCurso(doc));
        }
        return lista;
    }

    private Curso aCurso(Document doc) {
        return new Curso(doc.getString("nrc"), doc.getString("asignatura"),
                doc.getString("profesor"), doc.getInteger("cupos", 0));
    }
}
package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.sistema.modelo.Asignatura;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/** Acceso a la colección "asignaturas". */
public class AsignaturaDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("asignaturas");
    }

    public void insertar(Asignatura asignatura) {
        coleccion().insertOne(new Document("sigla", asignatura.getSigla())
                .append("nombre", asignatura.getNombre())
                .append("validez", asignatura.getValidez()));
    }

    /** Devuelve la asignatura, o null si no existe. */
    public Asignatura buscarPorSigla(String sigla) {
        Document doc = coleccion().find(eq("sigla", sigla)).first();
        return (doc == null) ? null : aAsignatura(doc);
    }

    /** Actualiza el nombre y la validez de la asignatura con esa sigla. */
    public void actualizar(Asignatura asignatura) {
        coleccion().updateOne(eq("sigla", asignatura.getSigla()), Updates.combine(
                Updates.set("nombre", asignatura.getNombre()),
                Updates.set("validez", asignatura.getValidez())));
    }

    public void eliminar(String sigla) {
        coleccion().deleteOne(eq("sigla", sigla));
    }

    public List<Asignatura> listarTodas() {
        List<Asignatura> lista = new ArrayList<>();
        for (Document doc : coleccion().find()) {
            lista.add(aAsignatura(doc));
        }
        return lista;
    }

    private Asignatura aAsignatura(Document doc) {
        return new Asignatura(doc.getString("sigla"), doc.getString("nombre"),
                doc.getInteger("validez", 0));
    }
}
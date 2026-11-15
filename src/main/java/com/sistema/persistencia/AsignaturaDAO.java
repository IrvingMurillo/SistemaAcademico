package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.sistema.modelo.Asignatura;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

public class AsignaturaDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("asignaturas");
    }

    public Asignatura buscarPorSigla(String sigla) {
        Document doc = coleccion().find(eq("sigla", sigla)).first();
        if (doc == null) {
            return null;
        }
        return new Asignatura(
                doc.getString("sigla"),
                doc.getString("nombre"),
                doc.getString("validez")
        );
    }

    public boolean insertar(Asignatura asignatura) {
        if (buscarPorSigla(asignatura.getSigla()) != null) {
            return false;
        }
        Document doc = new Document("sigla", asignatura.getSigla())
                .append("nombre", asignatura.getNombre())
                .append("validez", asignatura.getValidez());
        coleccion().insertOne(doc);
        return true;
    }

    public boolean modificar(Asignatura asignatura) {
        if (buscarPorSigla(asignatura.getSigla()) == null) {
            return false;
        }
        coleccion().updateOne(
                eq("sigla", asignatura.getSigla()),
                Updates.combine(
                        Updates.set("nombre", asignatura.getNombre()),
                        Updates.set("validez", asignatura.getValidez())
                )
        );
        return true;
    }

    public boolean eliminar(String sigla) {
        return coleccion().deleteOne(eq("sigla", sigla)).getDeletedCount() > 0;
    }

    public List<Asignatura> listarTodas() {
        List<Asignatura> lista = new ArrayList<>();
        for (Document doc : coleccion().find()) {
            lista.add(new Asignatura(
                    doc.getString("sigla"),
                    doc.getString("nombre"),
                    doc.getString("validez")
            ));
        }
        return lista;
    }
}

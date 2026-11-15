package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.sistema.modelo.Matricula;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/** Acceso a la colección "matriculas". Las matrículas nunca se borran: se retiran. */
public class MatriculaDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("matriculas");
    }

    public void insertar(Matricula matricula) {
        coleccion().insertOne(new Document("codigo", matricula.getCodigo())
                .append("carnet", matricula.getCarnet())
                .append("nrc", matricula.getNrc())
                .append("fecha", matricula.getFecha())
                .append("estado", matricula.getEstado()));
    }

    /** Devuelve la matrícula, o null si no existe. */
    public Matricula buscarPorCodigo(String codigo) {
        Document doc = coleccion().find(eq("codigo", codigo)).first();
        return (doc == null) ? null : aMatricula(doc);
    }

    /** ¿El estudiante ya tiene una matrícula activa en ese curso? */
    public boolean existeActiva(String carnet, String nrc) {
        return coleccion().countDocuments(and(
                eq("carnet", carnet), eq("nrc", nrc), eq("estado", Matricula.ACTIVA))) > 0;
    }

    /** Cantidad de estudiantes inscritos (matrículas activas) en un curso. */
    public int contarActivas(String nrc) {
        return (int) coleccion().countDocuments(and(eq("nrc", nrc), eq("estado", Matricula.ACTIVA)));
    }

    /** ¿El curso tiene alguna matrícula, activa o retirada? */
    public boolean existenDeCurso(String nrc) {
        return coleccion().countDocuments(eq("nrc", nrc)) > 0;
    }

    /** ¿El estudiante tiene alguna matrícula, activa o retirada? */
    public boolean existenDeEstudiante(String carnet) {
        return coleccion().countDocuments(eq("carnet", carnet)) > 0;
    }

    public List<Matricula> listarTodas() {
        return aLista(coleccion().find());
    }

    /** Todas las matrículas de un estudiante, activas y retiradas. */
    public List<Matricula> listarPorCarnet(String carnet) {
        return aLista(coleccion().find(eq("carnet", carnet)));
    }

    public List<Matricula> listarActivasPorNrc(String nrc) {
        return aLista(coleccion().find(and(eq("nrc", nrc), eq("estado", Matricula.ACTIVA))));
    }

    /** Retirarse no borra la matrícula: cambia su estado a RETIRADA. */
    public void retirar(String codigo) {
        coleccion().updateOne(eq("codigo", codigo), Updates.set("estado", Matricula.RETIRADA));
    }

    /** Genera el siguiente código de matrícula (MAT-0001, MAT-0002...). */
    public String siguienteCodigo() {
        return String.format("MAT-%04d", coleccion().countDocuments() + 1);
    }

    private List<Matricula> aLista(Iterable<Document> documentos) {
        List<Matricula> lista = new ArrayList<>();
        for (Document doc : documentos) {
            lista.add(aMatricula(doc));
        }
        return lista;
    }

    private Matricula aMatricula(Document doc) {
        return new Matricula(
                doc.getString("codigo"),
                doc.getString("carnet"),
                doc.getString("nrc"),
                doc.getString("fecha"),
                doc.getString("estado"));
    }
}
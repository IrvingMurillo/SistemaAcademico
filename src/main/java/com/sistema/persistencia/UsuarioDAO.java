package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import com.sistema.modelo.Rol;
import com.sistema.modelo.Usuario;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/** Acceso a la colección "usuarios": datos de acceso de todos los roles. */
public class UsuarioDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("usuarios");
    }

    /** Guarda un usuario nuevo. */
    public void insertar(Usuario usuario) {
        coleccion().insertOne(new Document("identificador", usuario.getIdentificador())
                .append("nombreCompleto", usuario.getNombreCompleto())
                .append("correo", usuario.getCorreo())
                .append("passwordHash", usuario.getPasswordHash())
                .append("rol", usuario.getRol().name()));
    }

    /** Devuelve el usuario con ese correo, o null si no existe. */
    public Usuario buscarPorCorreo(String correo) {
        Document doc = coleccion().find(eq("correo", correo)).first();
        return (doc == null) ? null : aUsuario(doc);
    }

    /** Lista los usuarios de un rol. */
    public List<Usuario> listarPorRol(Rol rol) {
        List<Usuario> lista = new ArrayList<>();
        for (Document doc : coleccion().find(eq("rol", rol.name()))) {
            lista.add(aUsuario(doc));
        }
        return lista;
    }

    public boolean existeIdentificador(String identificador) {
        return coleccion().countDocuments(eq("identificador", identificador)) > 0;
    }

    public boolean existeCorreo(String correo) {
        return coleccion().countDocuments(eq("correo", correo)) > 0;
    }

    /** Actualiza el nombre y el correo de un usuario (no toca contraseña ni rol). */
    public void actualizarDatos(String identificador, String nombreCompleto, String correo) {
        coleccion().updateOne(eq("identificador", identificador), Updates.combine(
                Updates.set("nombreCompleto", nombreCompleto),
                Updates.set("correo", correo)));
    }

    public void eliminar(String identificador) {
        coleccion().deleteOne(eq("identificador", identificador));
    }

    private Usuario aUsuario(Document doc) {
        return new Usuario(
                doc.getString("identificador"),
                doc.getString("nombreCompleto"),
                doc.getString("correo"),
                doc.getString("passwordHash"),
                Rol.valueOf(doc.getString("rol")));
    }
}
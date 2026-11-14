package com.sistema.persistencia;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import com.sistema.modelo.Rol;
import com.sistema.modelo.Usuario;
import org.bson.Document;

public class UsuarioDAO {

    private MongoCollection<Document> coleccion() {
        return ConexionMongoDB.getDatabase().getCollection("usuarios");
    }

    /** La base de datos misma rechaza identificadores o correos repetidos. */
    public void crearIndices() {
        IndexOptions unico = new IndexOptions().unique(true);
        coleccion().createIndex(Indexes.ascending("identificador"), unico);
        coleccion().createIndex(Indexes.ascending("correo"), unico);
    }

    public void insertar(Usuario usuario) {
        Document doc = new Document("identificador", usuario.getIdentificador())
                .append("nombreCompleto", usuario.getNombreCompleto())
                .append("correo", usuario.getCorreo())
                .append("passwordHash", usuario.getPasswordHash())
                .append("rol", usuario.getRol().name());
        coleccion().insertOne(doc);
    }

    /** Devuelve el usuario, o null si no existe. */
    public Usuario buscarPorCorreo(String correo) {
        Document doc = coleccion().find(eq("correo", correo)).first();
        if (doc == null) {
            return null;
        }
        return new Usuario(
                doc.getString("identificador"),
                doc.getString("nombreCompleto"),
                doc.getString("correo"),
                doc.getString("passwordHash"),
                Rol.valueOf(doc.getString("rol")));
    }

    public boolean existeIdentificador(String identificador) {
        return coleccion().countDocuments(eq("identificador", identificador)) > 0;
    }

    public boolean existeCorreo(String correo) {
        return coleccion().countDocuments(eq("correo", correo)) > 0;
    }

    public void eliminar(String identificador) {
        coleccion().deleteOne(eq("identificador", identificador));
    }
}
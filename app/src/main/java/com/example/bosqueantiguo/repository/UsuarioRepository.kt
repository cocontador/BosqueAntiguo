package com.example.bosqueantiguo.repository

import com.example.bosqueantiguo.model.Usuario
import com.example.bosqueantiguo.model.UsuarioDao
import kotlinx.coroutines.flow.Flow

/**
 * Capa intermedia (Repository) que abstrae el acceso a datos.
 * Se comunica con UsuarioDao (Room) y expone funciones suspendidas o Flow.
 *
 * Su propósito es separar la lógica de datos del ViewModel,
 * facilitando pruebas y mantenimiento.
 */
class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    /**
     * Inserta o reemplaza un usuario en la base de datos.
     */
    suspend fun insertarUsuario(usuario: Usuario) {
        usuarioDao.insertarUsuario(usuario)
    }

    /**
     * Devuelve un flujo (Flow) con la lista de usuarios almacenados.
     * Se actualiza automáticamente cuando cambia la base de datos.
     */
    fun obtenerUsuarios(): Flow<List<Usuario>> {
        return usuarioDao.obtenerUsuarios()
    }

    /**
     * Obtiene un usuario específico por su ID.
     */
    suspend fun obtenerPorId(id: Int): Usuario? {
        return usuarioDao.obtenerPorId(id)
    }

    /**
     * Elimina todos los registros de la tabla usuarios.
     */
    suspend fun eliminarTodos() {
        usuarioDao.eliminarTodos()
    }
}

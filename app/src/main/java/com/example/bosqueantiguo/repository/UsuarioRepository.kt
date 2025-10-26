package com.example.bosqueantiguo.repository

import com.example.bosqueantiguo.model.Usuario
import com.example.bosqueantiguo.model.UsuarioDao
import kotlinx.coroutines.flow.Flow

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun insertarUsuario(usuario: Usuario) = usuarioDao.insertarUsuario(usuario)

    suspend fun eliminarUsuario(usuario: Usuario) = usuarioDao.eliminarUsuario(usuario)

    fun obtenerUsuarios(): Flow<List<Usuario>> = usuarioDao.obtenerUsuarios()

    suspend fun eliminarTodos() = usuarioDao.eliminarTodos()

    suspend fun obtenerUsuarioPorId(id: Int): Usuario? = usuarioDao.obtenerUsuarioPorId(id)

    suspend fun actualizarImagenUsuario(id: Int, uri: String) =
        usuarioDao.actualizarImagenUsuario(id, uri)
}

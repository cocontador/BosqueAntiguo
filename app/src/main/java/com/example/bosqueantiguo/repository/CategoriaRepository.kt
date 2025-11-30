package com.example.bosqueantiguo.repository

import android.util.Log
import com.example.bosqueantiguo.model.Categoria
import com.example.bosqueantiguo.network.RetrofitConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoriaRepository {

    companion object {
        private const val TAG = "CategoriaRepository"
    }

    private val apiService = RetrofitConfig.categoriaApiService

    fun obtenerCategorias(): Flow<List<Categoria>> = flow {
        Log.d(TAG, "Iniciando petición a API de categorías")
        try {
            val response = apiService.getCategorias()
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d(TAG, "Categorías recibidas: ${it.size}")
                    emit(it)
                } ?: emit(emptyList())
            } else {
                Log.e(TAG, "Error al obtener categorías: ${response.code()}")
                emit(emptyList())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener categorías:", e)
            emit(emptyList())
        }
    }
}
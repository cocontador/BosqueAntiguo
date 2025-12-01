package com.example.bosqueantiguo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bosqueantiguo.repository.VentaRepository

class CarritoViewModelFactory(private val ventaRepository: VentaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarritoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarritoViewModel(ventaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
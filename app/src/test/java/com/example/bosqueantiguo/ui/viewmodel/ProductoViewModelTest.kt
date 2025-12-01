package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import app.cash.turbine.test
import com.example.bosqueantiguo.model.ProductoApi
import com.example.bosqueantiguo.repository.ProductoRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

// Se ha eliminado la clase MainDispatcherRule para simplificar el código y atacar el error de referencia.

@ExperimentalCoroutinesApi
class ProductoViewModelTest {

    // Se define el dispatcher directamente en la clase de prueba.
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var productoRepository: ProductoRepository
    private lateinit var viewModel: ProductoViewModel

    @Before
    fun setUp() {
        // Se establece el dispatcher principal ANTES de cada prueba.
        Dispatchers.setMain(testDispatcher)
        
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any<String>()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        productoRepository = mockk()
        viewModel = ProductoViewModel(productoRepository)
    }

    @After
    fun tearDown() {
        // Se limpia el dispatcher principal DESPUÉS de cada prueba.
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarProductos cuando el repositorio es exitoso, emite los estados correctos`() = runTest {
        val listaProductosMock = listOf(mockk<ProductoApi>(relaxed = true))
        coEvery { productoRepository.obtenerProductos(any()) } returns flowOf(listaProductosMock)

        viewModel.cargarProductos()
        // Es necesario avanzar el dispatcher para que la coroutine se ejecute.
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertFalse(viewModel.hasError.value)
        assertEquals(listaProductosMock, viewModel.productos.value)
    }

    @Test
    fun `cargarProductos cuando el repositorio falla, emite el estado de error`() = runTest {
        val mensajeError = "Error de red simulado"
        coEvery { productoRepository.obtenerProductos(any()) } returns flow { throw RuntimeException(mensajeError) }

        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertTrue(viewModel.hasError.value)
        assertTrue(viewModel.productos.value.isEmpty())
    }
}
package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import app.cash.turbine.test
import com.example.bosqueantiguo.model.ProductoApi
import com.example.bosqueantiguo.repository.ProductoRepository
import io.mockk.coEvery
import io.mockk.coVerify
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

@ExperimentalCoroutinesApi
class ProductoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    // Dependencias mockeadas
    private lateinit var productoRepository: ProductoRepository
    private lateinit var carritoViewModel: CarritoViewModel

    // La clase que estamos probando
    private lateinit var viewModel: ProductoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any<String>()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0

        // Creamos instancias falsas de las dependencias
        productoRepository = mockk(relaxed = true)
        carritoViewModel = mockk(relaxed = true) // Mock para el CarritoViewModel

        viewModel = ProductoViewModel(productoRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cargarProductos cuando el repositorio es exitoso, actualiza el estado a una lista de productos`() = runTest {
        val listaProductosMock = listOf(mockk<ProductoApi>(relaxed = true))
        coEvery { productoRepository.obtenerProductos(any()) } returns flowOf(listaProductosMock)

        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertFalse(viewModel.hasError.value)
        assertEquals(listaProductosMock, viewModel.productos.value)
    }

    @Test
    fun `cargarProductos cuando el repositorio falla, actualiza el estado a error`() = runTest {
        val mensajeError = "Error de red simulado"
        coEvery { productoRepository.obtenerProductos(any()) } returns flow { throw RuntimeException(mensajeError) }

        viewModel.cargarProductos()
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.isLoading.value)
        assertTrue(viewModel.hasError.value)
        assertTrue(viewModel.productos.value.isEmpty())
    }

    @Test
    fun `reintentar debe llamar a cargarProductos`() = runTest {
        // Arrange: Preparamos un mock para verificar la llamada
        coEvery { productoRepository.obtenerProductos(any()) } returns flowOf(emptyList())
        
        // Act
        viewModel.reintentar()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert: Verificamos que obtenerProductos fue llamado, lo que implica que reintentar llamó a cargarProductos
        coVerify(exactly = 1) { productoRepository.obtenerProductos(any()) }
    }

    @Test
    fun `buscarYAgregarProducto cuando el producto existe, lo agrega al carrito`() = runTest {
        // Arrange
        val productoId = 1L
        val productoEncontrado = mockk<ProductoApi>(relaxed = true)
        coEvery { productoRepository.obtenerProductoPorId(productoId) } returns productoEncontrado

        // Act
        viewModel.buscarYAgregarProducto(productoId, carritoViewModel)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { carritoViewModel.agregarProducto(productoEncontrado) }
    }

    @Test
    fun `buscarYAgregarProducto cuando el producto NO existe, NO lo agrega al carrito`() = runTest {
        // Arrange
        val productoId = 99L
        coEvery { productoRepository.obtenerProductoPorId(productoId) } returns null // Simulamos que el producto no se encuentra

        // Act
        viewModel.buscarYAgregarProducto(productoId, carritoViewModel)
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 0) { carritoViewModel.agregarProducto(any()) } // Verificamos que NUNCA se llama
    }
}
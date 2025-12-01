package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import com.example.bosqueantiguo.model.CarritoItem
import com.example.bosqueantiguo.model.ProductoApi
import com.example.bosqueantiguo.model.VentaResponse
import com.example.bosqueantiguo.repository.VentaRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CarritoViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var ventaRepository: VentaRepository
    private lateinit var viewModel: CarritoViewModel

    // Datos de prueba
    private val producto1 = mockk<ProductoApi>(relaxed = true).apply {
        every { id } returns 1L
        every { nombre } returns "Producto 1"
        every { precio } returns 1000.0
    }
    private val producto2 = mockk<ProductoApi>(relaxed = true).apply {
        every { id } returns 2L
        every { nombre } returns "Producto 2"
        every { precio } returns 500.0
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        ventaRepository = mockk(relaxed = true)
        viewModel = CarritoViewModel(ventaRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `agregarProducto cuando es nuevo, lo añade a la lista y actualiza el total`() {
        viewModel.agregarProducto(producto1)
        assertEquals(1, viewModel.carritoItems.value.size)
        assertEquals(1000.0, viewModel.totalPrecio.value, 0.0)
        assertEquals(1, viewModel.carritoItems.value.first().cantidad)
    }

    @Test
    fun `agregarProducto cuando ya existe, incrementa la cantidad y actualiza el total`() {
        viewModel.agregarProducto(producto1) // cantidad 1, total 1000
        viewModel.agregarProducto(producto1) // cantidad 2, total 2000

        assertEquals(1, viewModel.carritoItems.value.size)
        assertEquals(2, viewModel.carritoItems.value.first().cantidad)
        assertEquals(2000.0, viewModel.totalPrecio.value, 0.0)
    }

    @Test
    fun `removerProducto elimina el item y actualiza el total`() {
        viewModel.agregarProducto(producto1)
        viewModel.agregarProducto(producto2)

        viewModel.removerProducto(1L)

        assertEquals(1, viewModel.carritoItems.value.size)
        assertEquals(2L, viewModel.carritoItems.value.first().producto.id)
        assertEquals(500.0, viewModel.totalPrecio.value, 0.0)
    }

    @Test
    fun `actualizarCantidad a cero, elimina el item`() {
        viewModel.agregarProducto(producto1)
        viewModel.actualizarCantidad(1L, 0)
        assertTrue(viewModel.carritoItems.value.isEmpty())
        assertEquals(0.0, viewModel.totalPrecio.value, 0.0)
    }
    
    @Test
    fun `procesarPago con éxito, actualiza el estado a Success`() = runTest {
        // Arrange
        val ventaResponseMock = mockk<VentaResponse>()
        coEvery { ventaRepository.registrarVenta(any()) } returns ventaResponseMock
        viewModel.agregarProducto(producto1)
        
        // Act
        viewModel.procesarPago()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertTrue(viewModel.ventaState.value is VentaUiState.Success)
        assertEquals(ventaResponseMock, (viewModel.ventaState.value as VentaUiState.Success).venta)
    }
    
    @Test
    fun `procesarPago con fallo, actualiza el estado a Error`() = runTest {
        // Arrange
        coEvery { ventaRepository.registrarVenta(any()) } returns null // Simulamos un fallo
        viewModel.agregarProducto(producto1)
        
        // Act
        viewModel.procesarPago()
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Assert
        assertTrue(viewModel.ventaState.value is VentaUiState.Error)
    }
}
package com.example.bosqueantiguo.repository

import android.util.Log
import app.cash.turbine.test
import com.example.bosqueantiguo.model.ProductoApi
import com.example.bosqueantiguo.network.ProductoApiService
import com.example.bosqueantiguo.network.RetrofitConfig
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class ProductoRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var productoApiService: ProductoApiService
    private lateinit var productoRepository: ProductoRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        // CORREGIDO: Mockear la clase Log de Android para evitar el error 'not mocked'
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any<String>()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0
        
        productoApiService = mockk()

        mockkObject(RetrofitConfig)
        every { RetrofitConfig.productoApiService } returns productoApiService

        productoRepository = ProductoRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `obtenerProductos cuando la API responde exitosamente, retorna un flujo de productos`() = runTest {
        val listaProductosMock = listOf(mockk<ProductoApi>(relaxed = true))
        coEvery { productoApiService.getProductos(any()) } returns Response.success(listaProductosMock)

        val flujo = productoRepository.obtenerProductos()

        flujo.test { 
            val emision = awaitItem()
            assertEquals(listaProductosMock, emision)
            awaitComplete()
        }
    }

    @Test
    fun `obtenerProductos cuando la API falla, retorna un flujo vacio`() = runTest {
        coEvery { productoApiService.getProductos(any()) } returns Response.error(404, mockk(relaxed = true))

        val flujo = productoRepository.obtenerProductos()

        flujo.test { 
            val emision = awaitItem()
            assertEquals(emptyList<ProductoApi>(), emision)
            awaitComplete()
        }
    }
}
package com.example.bosqueantiguo.viewmodel

import android.util.Log
import com.example.bosqueantiguo.repository.UsuarioRepository
import io.mockk.coEvery
import io.mockk.coVerify
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
class UsuarioViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var viewModel: UsuarioViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        usuarioRepository = mockk(relaxed = true)
        coEvery { usuarioRepository.insertarUsuario(any()) } returns Unit

        viewModel = UsuarioViewModel(usuarioRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onNombreChange cuando se llama, el uiState se actualiza con el nuevo nombre`() {
        val nuevoNombre = "Constanza"
        viewModel.onNombreChange(nuevoNombre)
        assertEquals(nuevoNombre, viewModel.uiState.value.nombre)
    }

    @Test
    fun `guardarUsuario con datos válidos, llama al repositorio y el estado es exitoso`() = runTest {
        viewModel.onNombreChange("Usuario Válido")
        viewModel.onCorreoChange("correo@valido.com")
        viewModel.onEdadChange("25")
        viewModel.onContrasenaChange("Password123")

        viewModel.guardarUsuario()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { usuarioRepository.insertarUsuario(any()) }
        assertTrue(viewModel.uiState.value.guardadoExitoso)
    }

    @Test
    fun `guardarUsuario con correo inválido, no llama al repositorio y muestra error`() = runTest {
        viewModel.onNombreChange("Usuario Inválido")
        viewModel.onCorreoChange("correo-invalido")
        viewModel.onEdadChange("30")
        viewModel.onContrasenaChange("Password123")

        viewModel.guardarUsuario()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 0) { usuarioRepository.insertarUsuario(any()) }
        assertFalse(viewModel.uiState.value.guardadoExitoso)
        assertNotNull(viewModel.uiState.value.errores.correoError)
    }
}
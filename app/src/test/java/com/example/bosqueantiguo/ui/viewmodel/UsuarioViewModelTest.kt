package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import com.example.bosqueantiguo.viewmodel.UsuarioViewModel
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

// NOTA: Se eliminó @RunWith(AndroidJUnit4::class) porque esto es una prueba unitaria local.

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
        // Arrange
        viewModel.onNombreChange("Usuario Válido")
        viewModel.onCorreoChange("correo@valido.com")
        viewModel.onEdadChange("25")
        viewModel.onContrasenaChange("Password123")
        coEvery { usuarioRepository.insertarUsuario(any()) } returns Unit

        // Act
        viewModel.guardarUsuario()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 1) { usuarioRepository.insertarUsuario(any()) }
        assertTrue(viewModel.uiState.value.guardadoExitoso)
    }

    @Test
    fun `guardarUsuario con correo inválido, no llama al repositorio y muestra error`() = runTest {
        // Arrange
        viewModel.onNombreChange("Usuario Inválido")
        viewModel.onCorreoChange("correo-invalido") // Correo incorrecto
        viewModel.onEdadChange("30")
        viewModel.onContrasenaChange("Password123")

        // Act
        viewModel.guardarUsuario()
        testDispatcher.scheduler.advanceUntilIdle()

        // Assert
        coVerify(exactly = 0) { usuarioRepository.insertarUsuario(any()) } // Verificamos que NUNCA se llama al repositorio
        assertFalse(viewModel.uiState.value.guardadoExitoso)
        assertNotNull(viewModel.uiState.value.errores.correoError)
    }
}
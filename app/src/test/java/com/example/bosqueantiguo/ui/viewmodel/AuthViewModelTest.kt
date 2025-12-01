package com.example.bosqueantiguo.ui.viewmodel

import android.util.Log
import app.cash.turbine.test
import com.example.bosqueantiguo.datastore.TokenManager
import com.example.bosqueantiguo.model.LoginResponse
import com.example.bosqueantiguo.model.UsuarioApi
import com.example.bosqueantiguo.repository.AuthRepository
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
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var authRepository: AuthRepository
    private lateinit var tokenManager: TokenManager
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0

        authRepository = mockk()
        tokenManager = mockk(relaxed = true)

        viewModel = AuthViewModel(authRepository, tokenManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `doLogin con credenciales válidas, emite Loading y luego Success`() = runTest {
        // Arrange
        val usuarioApiMock = mockk<UsuarioApi>(relaxed = true)
        val loginResponseMock = LoginResponse(token = "token_de_prueba", usuario = usuarioApiMock)
        coEvery { authRepository.login(any(), any()) } returns loginResponseMock

        // Act y Assert
        viewModel.loginState.test {
            assertEquals("El estado inicial debe ser Idle", LoginUiState.Idle, awaitItem())

            viewModel.doLogin("test@test.com", "password")
            
            // Verificamos la secuencia de estados
            assertEquals("Debe cambiar a Loading", LoginUiState.Loading, awaitItem())
            
            val successState = awaitItem()
            assertTrue("El estado final debe ser Success", successState is LoginUiState.Success)
            assertEquals(loginResponseMock, (successState as LoginUiState.Success).loginResponse)

            // Verificamos que se guardó el token
            coVerify(exactly = 1) { tokenManager.saveToken(loginResponseMock.token) }
            
            // Nos aseguramos de que no haya más emisiones
            expectNoEvents()
        }
    }

    @Test
    fun `doLogin con credenciales inválidas, emite Loading y luego Error`() = runTest {
        // Arrange
        coEvery { authRepository.login(any(), any()) } returns null // Simulamos un fallo

        // Act y Assert
        viewModel.loginState.test {
            assertEquals(LoginUiState.Idle, awaitItem())

            viewModel.doLogin("test@test.com", "password_incorrecto")

            assertEquals(LoginUiState.Loading, awaitItem())
            assertTrue(awaitItem() is LoginUiState.Error)

            expectNoEvents()
        }
    }
}
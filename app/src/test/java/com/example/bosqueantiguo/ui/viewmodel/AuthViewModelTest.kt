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
    fun `doLogin con credenciales válidas, actualiza el estado a Success y guarda el token`() = runTest {
        // Arrange: Creamos los mocks correctos que coinciden con tus modelos de datos
        val usuarioApiMock = mockk<UsuarioApi>(relaxed = true)
        val loginResponseMock = LoginResponse(token = "token_de_prueba", usuario = usuarioApiMock)
        
        coEvery { authRepository.login(any(), any()) } returns loginResponseMock

        // Act y Assert
        viewModel.loginState.test {
            assertEquals(LoginUiState.Idle, awaitItem())

            viewModel.doLogin("test@test.com", "password")
            testDispatcher.scheduler.advanceUntilIdle() // Avanzamos el dispatcher

            // Verificamos que el estado cambia a Loading y luego a Success
            val loadingState = awaitItem()
            assertTrue(loadingState is LoginUiState.Loading)

            val successState = awaitItem()
            assertTrue(successState is LoginUiState.Success)
            assertEquals(loginResponseMock, (successState as LoginUiState.Success).loginResponse)

            // Verificamos que se intentó guardar el token
            coVerify(exactly = 1) { tokenManager.saveToken(loginResponseMock.token) }
        }
    }

    @Test
    fun `doLogin con credenciales inválidas, actualiza el estado a Error`() = runTest {
        // Arrange
        coEvery { authRepository.login(any(), any()) } returns null // Simulamos un fallo

        // Act y Assert
        viewModel.loginState.test {
            assertEquals(LoginUiState.Idle, awaitItem())

            viewModel.doLogin("test@test.com", "password_incorrecto")
            testDispatcher.scheduler.advanceUntilIdle()

            val loadingState = awaitItem()
            assertTrue(loadingState is LoginUiState.Loading)
            
            val errorState = awaitItem()
            assertTrue(errorState is LoginUiState.Error)
        }
    }
}
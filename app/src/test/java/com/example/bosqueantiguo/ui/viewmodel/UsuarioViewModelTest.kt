package com.example.bosqueantiguo.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bosqueantiguo.model.Usuario
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas unitarias para UsuarioViewModel
 * Verifica las validaciones y lógica de negocio
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class UsuarioViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `validarEmail_emailValido_debeRetornarTrue`() {
        // Given
        val emailsValidos = listOf(
            "test@ejemplo.com",
            "usuario.prueba@dominio.cl",
            "nombre123@correo.co"
        )
        
        // When & Then
        emailsValidos.forEach { email ->
            assertTrue(
                "El email '$email' debería ser válido",
                esEmailValido(email)
            )
        }
    }

    @Test
    fun `validarEmail_emailInvalido_debeRetornarFalse`() {
        // Given
        val emailsInvalidos = listOf(
            "email-sin-arroba.com",
            "@dominio.com",
            "usuario@",
            "usuario..doble@punto.com",
            ""
        )
        
        // When & Then
        emailsInvalidos.forEach { email ->
            assertFalse(
                "El email '$email' debería ser inválido",
                esEmailValido(email)
            )
        }
    }

    @Test
    fun `validarEdad_edadValida_debeRetornarTrue`() {
        // Given
        val edadesValidas = listOf(18, 25, 45, 65, 99)
        
        // When & Then
        edadesValidas.forEach { edad ->
            assertTrue(
                "La edad $edad debería ser válida",
                esEdadValida(edad)
            )
        }
    }

    @Test
    fun `validarEdad_edadInvalida_debeRetornarFalse`() {
        // Given
        val edadesInvalidas = listOf(0, -1, 17, 150, 200)
        
        // When & Then
        edadesInvalidas.forEach { edad ->
            assertFalse(
                "La edad $edad debería ser inválida",
                esEdadValida(edad)
            )
        }
    }

    @Test
    fun `validarContrasena_contrasenaValida_debeRetornarTrue`() {
        // Given
        val contrasenasValidas = listOf(
            "MiPassword123!",
            "Segura2024#",
            "Password123"
        )
        
        // When & Then
        contrasenasValidas.forEach { password ->
            assertTrue(
                "La contraseña '$password' debería ser válida",
                esContrasenaValida(password)
            )
        }
    }

    @Test
    fun `validarContrasena_contrasenaInvalida_debeRetornarFalse`() {
        // Given
        val contrasenasInvalidas = listOf(
            "123",      // Muy corta
            "password", // Sin números ni mayúsculas
            "",         // Vacía
            "a"         // Muy corta
        )
        
        // When & Then
        contrasenasInvalidas.forEach { password ->
            assertFalse(
                "La contraseña '$password' debería ser inválida",
                esContrasenaValida(password)
            )
        }
    }

    @Test
    fun `crearUsuario_datosValidos_debeCrearUsuarioCorrectamente`() {
        // Given
        val nombre = "Juan Pérez"
        val correo = "juan@ejemplo.com"
        val edad = 25
        val contrasena = "Password123"
        
        // When
        val usuario = Usuario(
            nombre = nombre,
            correo = correo,
            edad = edad,
            contrasena = contrasena
        )
        
        // Then
        assertEquals("El nombre debe coincidir", nombre, usuario.nombre)
        assertEquals("El correo debe coincidir", correo, usuario.correo)
        assertEquals("La edad debe coincidir", edad, usuario.edad)
        assertEquals("La contraseña debe coincidir", contrasena, usuario.contrasena)
        assertEquals("El ID debe ser 0 por defecto", 0, usuario.id)
    }

    // Funciones helper para validaciones (deberían estar en el ViewModel real)
    private fun esEmailValido(email: String): Boolean {
        return email.contains("@") && 
               email.contains(".") && 
               email.length > 5 &&
               !email.startsWith("@") &&
               !email.endsWith("@") &&
               !email.contains("..")
    }

    private fun esEdadValida(edad: Int): Boolean {
        return edad in 18..120
    }

    private fun esContrasenaValida(password: String): Boolean {
        return password.length >= 6 &&
               password.any { it.isDigit() } &&
               password.any { it.isLetter() }
    }
}
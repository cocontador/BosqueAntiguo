## Bosque Antiguo App

**Bosque Antiguo App** es la adaptación móvil del proyecto web *Bosque Antiguo*, desarrollada con **Kotlin y Jetpack Compose** como parte del curso **Desarrollo de Aplicaciones Móviles (DSY1105)**.

Esta versión traslada la esencia de la tienda online original a una aplicación Android, enfocándose en el diseño visual, la experiencia de usuario y el uso de recursos nativos del dispositivo.

---
## Tecnologías utilizadas

- **Kotlin**  
- **Jetpack Compose (Material 3)**  
- **Navigation Compose**  
- **Room Database** (persistencia local)  
- **DataStore** (preferencias)  
- **Coil** (carga de imágenes)  

---

## Características principales

-  **Pantalla principal (Home)** con navegación entre secciones.  
-  **Formulario con validaciones** visuales y lógicas desacopladas.  
-  **Persistencia local** con Room y DataStore.  
-  **Integración con cámara y galería** (recursos nativos Android).  
-  **Animaciones** suaves y retroalimentación visual.  

---
## Objetivo del proyecto

Explorar el desarrollo de interfaces móviles estructuradas y funcionales, aplicando conceptos de:
- Usabilidad y jerarquía visual.  
- Gestión de estado con ViewModel.  
- Persistencia de datos y acceso seguro a recursos del dispositivo.  

---
## Estructura del proyecto
com.example.bosqueantiguo/
├─ model/ → Clases de datos y componentes de persistencia
│   ├─ Usuario.kt → Modelo principal
│   ├─ UsuarioDao.kt → Interfaz DAO para operaciones con Room
│   └─ AppDatabase.kt → Base de datos local (Room)
│
├─ repository/ → Capa de acceso a datos
│   └─ UsuarioRepository.kt → Gestiona las operaciones entre ViewModel y Room
│
├─ ui/
│   ├─ theme/ → Colores, tipografía y estilos de Compose
│   ├─ view/ → Pantallas principales (Home, Form, Perfil, Ajustes, etc.)
│   └─ viewmodel/ → Lógica de presentación y gestión de estado (UsuarioViewModel, etc.)
│
├─ BosqueAntiguoApp.kt → Punto de entrada de la aplicación
└─ MainActivity.kt → Activity principal que inicializa la navegación

---

## Inspiración

Proyecto basado en la versión web original **Bosque Antiguo**, desarrollada con **HTML, CSS y JavaScript**, ahora llevada al entorno móvil para potenciar la accesibilidad y la experiencia de usuario en Android.

---
## Equipo de desarrollo

- **Paulina Zúñiga** 
- **César Monguez** 
- **Constanza Contador** 

---

## Licencia

Proyecto académico sin fines comerciales.  
© 2025 — *Equipo Bosque Antiguo*

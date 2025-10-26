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
```plaintext
app/
├─ manifests/
│  └─ AndroidManifest.xml
│
├─ kotlin+java/
│  └─ com/
│     └─ example/
│        └─ bosqueantiguo/
│           ├─ model/
│           │  ├─ AppDatabase.kt
│           │  ├─ Usuario.kt
│           │  └─ UsuarioDao.kt
│           │
│           ├─ repository/
│           │  └─ UsuarioRepository.kt
│           │
│           ├─ ui/
│           │  ├─ theme/
│           │  │  ├─ Color.kt
│           │  │  ├─ Theme.kt
│           │  │  └─ Type.kt
│           │  │
│           │  ├─ view/
│           │  │  ├─ AjustesScreen.kt
│           │  │  ├─ FormularioScreen.kt
│           │  │  ├─ MainScreen.kt
│           │  │  ├─ PerfilScreen.kt
│           │  │  ├─ ProductoScreen.kt
│           │  │  └─ ResumenScreen.kt
│           │  │
│           │  └─ viewmodel/
│           │     ├─ UsuarioErrores.kt
│           │     ├─ UsuarioUIState.kt
│           │     ├─ UsuarioViewModel.kt
│           │     └─ UsuarioViewModelFactory.kt
│           │
│           ├─ BosqueAntiguoApp.kt
│           └─ MainActivity.kt
│
├─ res/
│  ├─ drawable/
│  ├─ mipmap/
│  ├─ values/
│  └─ xml/
│
└─ Gradle Scripts/
   ├─ build.gradle.kts (Project: Bosque_Antiguo)
   ├─ build.gradle.kts (Module: app)
   ├─ proguard-rules.pro
   ├─ gradle.properties
   ├─ gradle-wrapper.properties
   ├─ libs.versions.toml
   ├─ local.properties
   └─ settings.gradle.kts
```
---

## Inspiración

Proyecto basado en la versión web original **Bosque Antiguo**, desarrollada con **HTML, CSS y JavaScript**, ahora llevada al entorno móvil para potenciar la accesibilidad y la experiencia de usuario en Android.

---
## Equipo de desarrollo

- **Paulina Zúñiga** 
- **César Mongez** 
- **Constanza Contador** 

---

## Licencia

Proyecto académico sin fines comerciales.  
© 2025 — *Equipo Bosque Antiguo*

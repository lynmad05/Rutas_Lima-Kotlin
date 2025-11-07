# 🚇 MetroLima GO: Tu Compañero para el Transporte en Lima

## 🎯 Objetivo General

**MetroLima GO** es una aplicación móvil creada para **transformar y simplificar la experiencia de transporte público** en Lima, Perú.  
Su propósito es brindar a los ciudadanos y visitantes una forma **rápida, visual y confiable** de planificar sus viajes a través del **Metro de Lima** 

La app busca:
- Optimizar la movilidad urbana y reducir tiempos de espera.  
- Proporcionar información actualizada sobre estaciones, horarios y rutas.  
- Incluir soporte multilingüe (Español e Inglés) para turistas.  

---

## ✨ Funcionalidades Principales

1. **Visualización y Búsqueda de Estaciones**
   - Listado completo de estaciones del Metro de Lima y Metropolitano.
   - Búsqueda por nombre o línea.
   - Visualización de detalles: nombre, línea, distrito, horario y coordenadas.

2. **Planificación Inteligente de Rutas**
   - Selección de origen y destino.
   - Cálculo del tiempo estimado (simulado).
   - Pasos del recorrido y estaciones intermedias.
   - Opción de guardar rutas favoritas.

3. **Información de Alertas y Noticias**
   - Muestra mensajes en tiempo real sobre cierres, mantenimientos o retrasos.
   - Consumo de datos mediante API o JSON remoto (Retrofit).

4. **Configuración Personalizada**
   - Activar modo claro/oscuro.
   - Cambiar idioma (Español/Inglés).
   - Mostrar versión de la app y créditos.

5. **Pantalla de Inicio (Home)**
   - Acceso rápido a todas las secciones principales.
   - Vista general del sistema de transporte.
   - Integración visual del mapa esquemático del Metro y Metropolitano.

---

## 🧩 Requerimientos Funcionales

### 1️⃣ Módulo de Estaciones
- Registrar y listar estaciones del Metro.
- Mostrar: nombre, línea, distrito, coordenadas y horario.
- Permitir búsqueda por nombre o línea.
- Ver detalles individuales de cada estación.

### 2️⃣ Módulo de Rutas
- Seleccionar origen y destino.
- Calcular tiempo estimado del recorrido (simulado).
- Mostrar pasos o estaciones intermedias.
- Guardar rutas favoritas.

### 3️⃣ Módulo de Datos Externos
- Consumir información desde una API o JSON remoto mediante Retrofit.
- Actualizar horarios, alertas y mantenimientos.
- Mostrar alertas dentro de la app (en Home o secciones relevantes).

### 4️⃣ Módulo de Configuración
- Activar modo claro/oscuro.
- Cambiar idioma (Español/Inglés).
- Mostrar versión y créditos del proyecto.

### 5️⃣ Módulo de Inicio (Home)
- Presentar información general y acceso a funciones principales.
- Integrar mapa o imagen esquemática del Metro.
- Incluir alertas visibles y botón “Ver en Google”.

---

## 🗓️ Planificación del Proyecto (7 Días de Desarrollo)

| **Día** | **Actividades / Tareas** | **Entregables** |
|----------|--------------------------|-----------------|
| **Día 1 – Análisis y Setup del Proyecto** | • Presentar el objetivo general y funcionalidades básicas.<br>• Crear el proyecto en Android Studio.<br>• Configurar dependencias (Compose, Room, Retrofit, Coroutines, Navigation).<br>• Diseñar logo e íconos básicos. | ✅ Proyecto base corriendo con pantalla inicial “MetroLima GO”. |
| **Día 2 – Diseño de UI con Compose** | • Crear pantallas principales:<br> - HomeScreen (mapa o imagen del metro).<br> - ListaEstacionesScreen (desde Room).<br> - DetalleEstacionScreen (detalle de estación).<br>• Implementar navegación entre pantallas (Navigation Compose). | ✅ Navegación funcional + prototipo de interfaz. |
| **Día 3 – Base de Datos Local (Room)** | • Crear entidad `Estacion`, DAO y `MetroDatabase`.<br>• Insertar datos locales (mock o JSON).<br>• Mostrar estaciones desde Room.<br>• Implementar búsqueda por nombre o línea. | ✅ CRUD básico funcionando con Room. |
| **Día 4 – Consumo de API (Retrofit)** | • Integrar API pública o JSON remoto.<br>• Crear modelos de red y Repository.<br>• Mostrar información actualizada (horarios, alertas). | ✅ Consumo de API funcionando y mostrando datos dinámicos. |
| **Día 5 – Planificador de Rutas (Simulado)** | • Implementar pantalla para elegir origen y destino.<br>• Calcular tiempo estimado (simulado).<br>• Mostrar pasos del recorrido. | ✅ Lógica de rutas básica + pantalla funcional. |
| **Día 6 – Ajustes y Mejoras Visuales** | • Añadir Material 3 y temas (modo oscuro/claro).<br>• Agregar íconos, menú inferior y animaciones.<br>• Mejorar diseño general. | ✅ App con diseño moderno y navegación completa. |
| **Día 7 – Pruebas y Despliegue** | • Pruebas funcionales y corrección de errores.<br>• Presentación final (demo o video).<br>• Documentar README y preparar entrega. | ✅ Versión final lista para exposición. |

---
# Figma
https://www.figma.com/design/5ZArdPdHcov5raScjkjj6A/Proyecto_Kotlin?node-id=0-1&t=KeMnwhwYAOTJ9CoY-1
---

## 🧠 Tecnologías Utilizadas

| Categoría | Tecnología |
|------------|-------------|
| Lenguaje Principal | Kotlin |
| Framework de UI | Jetpack Compose |
| Base de Datos Local | Room |
| Red / API | Retrofit |
| Navegación | Navigation Compose |
| Diseño | Material 3 |
| IDE | Android Studio  |
---

## 📁 Estructura de Carpetas del Proyecto

``` 

C:.
|   MainActivity.kt
|
+---data
|   +---api
|   |       MetroLimaApi.kt
|   |       RetrofitInstance.kt
|   |
|   +---dao
|   |       EstacionDao.kt
|   |       LineaDao.kt
|   |       RutaDao.kt
|   |       TransbordoDao.kt
|   |
|   +---db
|   |       MetroLimaDataBase.kt
|   |
|   \---model
|       |   Estacion.kt
|       |   EstacionExtendida.kt
|       |   Linea.kt
|       |   LineaConPuntos.kt
|       |   Ruta.kt
|       |   RutaResultado.kt
|       |   Transbordo.kt
|       |
|       \---remote
|               EstacionRemota.kt
|
+---navigation
|       NavGraph.kt
|
+---presentacion
|   |   MyApp.kt
|   |
|   +---screens
|   |   |   ConfigScreen.kt
|   |   |   DetalleEstacionScreen.kt
|   |   |   FavoritosScreen.kt
|   |   |   HomeScreen.kt
|   |   |   IniciarRutaScreen.kt
|   |   |   ListaEstacionesScreen.kt
|   |   |   ListaLineasScreen.kt
|   |   |   MapaGeneralScreen.kt
|   |   |   MapaScreen.kt
|   |   |   RutaScreen.kt
|   |   |   SplashScreen.kt
|   |   |
|   |   \---menu
|   |           AcercaAppScreen.kt
|   |           HistorialRutasScreen.kt
|   |
|   \---utils
|           LocaleHelper.kt
|
+---repository
|       EstacionRepository.kt
|       LineaRepository.kt
|       RutaRepository.kt
|
+---ui
|   +---components
|   |       BottomNavigationBar.kt
|   |       TopAppBarEstaciones.kt
|   |       TopAppBarWithMenuAndNotifications.kt
|   |
|   \---theme
|           Color.kt
|           Theme.kt
|           Type.kt
|
\---viewmodel
        DetalleEstacionViewModel.kt
        ListaEstacionesViewModel.kt
        ListaLineasViewModel.kt
        MapaGeneralViewModel.kt
        MapaLineaViewModel.kt
        RutaViewModel.kt

```

## Proyecto base corriendo con pantalla inicial 
<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/c93ad4ec-d6eb-460e-a9cd-a6c96339402c" />

## DIA 2
<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/098bd9a9-0573-4820-bc69-68faa984d52c" />
<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/a78b5cca-7268-4cf3-b9b3-834d35e4d1f2" />
<img width="250" height="700" alt="image" src="https://github.com/user-attachments/assets/ed68c7f9-37ad-42c7-9128-afc153db01a9" />

## DIA 3 - CRUD básico funcionando con Room 
<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/28a81f70-3d45-478d-a9c5-013b9939be4a" />
<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/7c1420e9-e9ca-4593-970a-830dfa12fa68" />


## DIA 4 - Consumo de API funcionando y mostrando datos. 

<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/6d2a113a-9556-40c3-b073-be6dbb7e0d59" />
<img width="250" height="650"  alt="image" src="https://github.com/user-attachments/assets/39896458-f0ba-4fac-a12b-866c86377a9b" />
<img width="250" height="650"  alt="image" src="https://github.com/user-attachments/assets/d2b57e25-9d96-43f8-a291-79621896ba75" />

## DIA 5 - Lógica de rutas básica + pantalla funcional.

<img width="200" height="650" alt="image" src="https://github.com/user-attachments/assets/45d20115-09a1-441a-b863-c11827f16523" />
<img width="200" height="400" alt="image" src="https://github.com/user-attachments/assets/b65d37c5-3d8d-407c-aec5-c52969a4d5c6" />
<img width="200" height="650" alt="image" src="https://github.com/user-attachments/assets/e886c48e-a8f0-4d86-a159-d68d5c0409f5" />
<img width="200" height="720" alt="image" src="https://github.com/user-attachments/assets/7fc60630-4962-4656-b761-22bf3e1caeaa" />


## DIA 6 - App con diseño moderno y navegación completa.
<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/f852e0c7-5bdd-4be0-bad8-e0e0b0a01479" />
<img width="250" height="700" alt="image" src="https://github.com/user-attachments/assets/5d10824a-8cae-4254-95ee-116455e74e4a" />
<img width="250" height="700" alt="image" src="https://github.com/user-attachments/assets/94ba3039-dc4f-46b0-a82f-a977be65bc37" />


## DIA 7 - Versión final lista para entrega. 

<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/315de3b6-c466-44b1-b73d-d78d5c951539" />
<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/2a459723-287e-47ac-abd2-78342d94b8da" />
<img width="250" height="650" alt="image" src="https://github.com/user-attachments/assets/e4514d08-3fe5-4938-9c4c-b4612ddea6dc" />

## 🎞️ App en funcionamiento:
# https://youtube.com/shorts/lbtHSJ0u-ao

## 👨‍💻 Desarrollado por:

Este proyecto ha sido desarrollado por el siguiente equipo:

* **Ailyn Medina**
* **Julio Medrano** 
* **Marlon Livia**

---

## 🚀 Conclusión

**MetroLima GO** representa una solución moderna y accesible para los usuarios del transporte público limeño.  
Su desarrollo combina tecnologías actuales de Android (Compose, Room, Retrofit) con un enfoque centrado en la experiencia del usuario, ofreciendo una herramienta útil tanto para ciudadanos como para visitantes.

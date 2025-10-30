# ProductApp - Aplicación de Gestión de Productos

## 📱 Descripción del Proyecto

**ProductApp** es una aplicación móvil Android desarrollada en **Kotlin** que permite gestionar productos de manera eficiente con sincronización a la **FakeStore API**. La aplicación cuenta con un sistema completo de carrito de compras, proceso de checkout y generación de recibos.

## ✨ Funcionalidades Principales

### 🏠 **Gestión de Productos**
- ✅ **Agregar productos** con formulario completo
- ✅ **Listar productos** con interfaz moderna
- ✅ **Ver detalles** de productos individuales
- ✅ **Editar y eliminar** productos existentes
- ✅ **Sincronización automática** con FakeStore API

### 🛒 **Sistema de Carrito de Compras**
- ✅ **Agregar productos** al carrito con cantidades
- ✅ **Modificar cantidades** con botones +/-
- ✅ **Eliminar productos** individuales
- ✅ **Calcular totales** con IGV (18%)
- ✅ **Limpiar carrito** completo

### 💳 **Proceso de Checkout Completo**
- ✅ **Formulario de cliente** (nombre, email, teléfono)
- ✅ **Información de envío** (dirección completa)
- ✅ **Métodos de pago** (efectivo, tarjeta, transferencia)
- ✅ **Resumen de pedido** detallado
- ✅ **Generación de recibo** con número de orden

## 🏗️ Arquitectura Técnica

### **Patrón de Diseño**
- **MVVM (Model-View-ViewModel)** con **LiveData**
- **Repository Pattern** para abstracción de datos
- **Separación de responsabilidades** por capas

### **Base de Datos Local**
- **Room Database** con entidades, DAOs y migraciones
- **Persistencia offline** completa
- **Consultas reactivas** con LiveData

### **Integración API REST**
- **Retrofit** para comunicación HTTP
- **FakeStore API** como backend
- **Sincronización bidireccional** (local ↔ remoto)
- **Manejo de errores** robusto

### **Interfaz de Usuario**
- **Material Design 3** con colores pasteles
- **ViewBinding** para acceso a vistas
- **CardViews** con elevación y esquinas redondeadas
- **Gradientes suaves** en backgrounds
- **Navegación intuitiva** con toolbar personalizada

## 🛠️ Tecnologías Implementadas

| Categoría | Tecnología | Versión |
|-----------|------------|---------|
| **Lenguaje** | Kotlin | 2.0.21 |
| **UI Framework** | Material Design 3 | 1.11.0 |
| **Base de Datos** | Room | 2.6.1 |
| **Networking** | Retrofit + Gson | 2.9.0 |
| **Async** | Coroutines | 1.7.3 |
| **Arquitectura** | ViewModel + LiveData | 2.7.0 |
| **Imágenes** | Glide | 4.16.0 |
| **Build** | Gradle KTS | 8.13 |

## 📁 Estructura del Proyecto

```
app/src/main/
├── java/com/cibertec/productapp/
│   ├── MainActivity.kt                    # Pantalla principal
│   ├── data/
│   │   ├── local/
│   │   │   ├── entity/ProductEntity.kt    # Entidad Room
│   │   │   ├── dao/ProductDao.kt          # Data Access Object
│   │   │   └── database/AppDatabase.kt    # Base de datos Room
│   │   ├── remote/
│   │   │   ├── api/FakeStoreApi.kt        # Interface API
│   │   │   ├── model/ProductResponse.kt   # Modelos de respuesta
│   │   │   └── RetrofitInstance.kt        # Configuración Retrofit
│   │   └── repository/ProductRepository.kt # Patrón Repository
│   └── ui/
│       ├── activities/                    # Actividades principales
│       │   ├── AddProductActivity.kt      # Agregar/Editar productos
│       │   ├── ProductListActivity.kt     # Lista de productos
│       │   ├── ProductDetailActivity.kt   # Detalle de producto
│       │   ├── CartActivity.kt            # Carrito de compras
│       │   ├── CheckoutActivity.kt        # Proceso de checkout
│       │   └── OrderReceiptActivity.kt    # Recibo de orden
│       ├── adapter/ProductAdapter.kt      # Adapter para RecyclerView
│       └── viewmodel/                     # ViewModels MVVM
│           ├── ProductViewModel.kt        # ViewModel de productos
│           └── CartViewModel.kt           # ViewModel del carrito
├── res/
│   ├── layout/                           # Layouts XML
│   ├── values/                           # Strings, colores, temas
│   ├── drawable/                         # Iconos y fondos
│   └── menu/                            # Menús de navegación
└── AndroidManifest.xml                   # Configuración de la app
```

## 🎨 Diseño Visual

### **Paleta de Colores**
- **Primarios**: Tonos pasteles suaves (#E3F2FD, #F3E5F5)
- **Secundarios**: Acentos complementarios (#FFF3E0, #E8F5E8)
- **Textos**: Alto contraste para legibilidad
- **Gradientes**: Transiciones suaves en backgrounds

### **Componentes UI**
- **Cards**: Esquinas redondeadas (20dp) con elevación sutil
- **Botones**: Material Design con iconos descriptivos
- **TextFields**: Outlined style con validación visual
- **RecyclerView**: Lista fluida con animaciones

## ⚙️ Configuración de Desarrollo

### **Requisitos del Sistema**
- **Android Studio**: Arctic Fox o superior
- **Gradle**: 8.13+
- **Kotlin**: 2.0.21
- **Android SDK**: API 24-34
- **JDK**: 11 o superior

### **Dependencias Principales**
```kotlin
// Base de datos
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// UI y Arquitectura
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
implementation("com.google.android.material:material:1.11.0")

// Imágenes
implementation("com.github.bumptech.glide:glide:4.16.0")

// Async
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

## 🚀 Instalación y Uso

### **1. Clonar/Descargar Proyecto**
```bash
# Proyecto listo para importar en Android Studio
```

### **2. Configuración**
- Abrir en **Android Studio**
- Sincronizar **Gradle**
- Configurar **dispositivo/emulador**

### **3. Ejecutar**
```bash
# Compilar y ejecutar
./gradlew assembleDebug
./gradlew installDebug
```

### **4. Funcionalidades Disponibles**
1. **Pantalla Principal**: Navegación a todas las secciones
2. **Agregar Producto**: Formulario completo con validaciones
3. **Lista de Productos**: Visualización con imágenes y sincronización
4. **Carrito**: Gestión completa de productos seleccionados
5. **Checkout**: Proceso de compra paso a paso
6. **Recibo**: Confirmación detallada de la orden

## 🌐 Integración con API

### **FakeStore API**
- **Base URL**: `https://fakestoreapi.com/`
- **Endpoints utilizados**:
  - `GET /products` - Obtener todos los productos
  - `GET /products/{id}` - Obtener producto específico
  - `POST /products` - Crear nuevo producto
  - `PUT /products/{id}` - Actualizar producto
  - `DELETE /products/{id}` - Eliminar producto

### **Sincronización**
- **Automática** al abrir la lista de productos
- **Manual** mediante botón de sincronización
- **Offline-first** con Room como fuente de verdad
- **Manejo de errores** con feedback al usuario

## 📊 Características de Calidad

### **Rendimiento**
- ✅ **Operaciones asíncronas** con Coroutines
- ✅ **Carga de imágenes optimizada** con Glide
- ✅ **Consultas eficientes** con Room
- ✅ **Recycling de vistas** en listas

### **Usabilidad**
- ✅ **Interfaz intuitiva** y consistente
- ✅ **Feedback inmediato** con Toast/Snackbar
- ✅ **Navegación fluida** entre pantallas
- ✅ **Validación de formularios** en tiempo real

### **Robustez**
- ✅ **Manejo de errores** completo
- ✅ **Persistencia offline** confiable
- ✅ **Validación de datos** en múltiples capas
- ✅ **Recuperación automática** de errores de red

## 📦 Control de Versiones

### **Configuración Git**
```bash
# Inicializar repositorio
git init

# Agregar archivos
git add .

# Commit inicial
git commit -m "Initial commit: ProductApp - Aplicación completa de gestión de productos"

# Conectar con repositorio remoto
git remote add origin https://github.com/tu-usuario/ProductApp.git

# Subir al repositorio
git push -u origin main
```

### **Estructura de Commits Recomendada**
- `feat:` - Nueva funcionalidad
- `fix:` - Corrección de bugs
- `docs:` - Actualización de documentación
- `style:` - Cambios de formato/estilo
- `refactor:` - Refactorización de código

---

## 👥 Información del Proyecto

**Desarrollado por**: Estudiante de Cibertec - Desarrollo de Aplicaciones Móviles I  
**Tecnología**: Android nativo con Kotlin  
**Fecha**: 2024  
**Estado**: Completamente funcional y listo para presentación

---

### 🎯 **Proyecto optimizado para presentación académica y demostración de competencias en desarrollo Android**

## 📋 Licencia

Este proyecto fue desarrollado con fines educativos para el curso de Desarrollo de Aplicaciones Móviles I en Cibertec.

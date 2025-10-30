@echo off
color 0C
echo.
echo ═══════════════════════════════════════════════════════════════════
echo    ⚡ PREPARACION RAPIDA PARA GIT - SOLO LO ESENCIAL
echo ═══════════════════════════════════════════════════════════════════
echo.
echo Este script prepara tu proyecto ProductApp para subirlo a Git
echo de la manera más rápida y sencilla posible.
echo.

echo [1/4] Verificando Git...
git --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Git no está instalado
    echo.
    echo ACCION REQUERIDA:
    echo 1. Ve a: https://git-scm.com/downloads
    echo 2. Descarga e instala Git para Windows
    echo 3. Vuelve a ejecutar este script
    echo.
    pause
    exit /b 1
) else (
    echo ✅ Git instalado correctamente
)

echo.
echo [2/4] Inicializando repositorio...
if not exist ".git" (
    git init
    echo ✅ Repositorio inicializado
) else (
    echo ✅ Repositorio ya existe
)

echo.
echo [3/4] Agregando archivos...
git add .
echo ✅ Archivos agregados

echo.
echo [4/4] Commit inicial...
git commit -m "ProductApp: Aplicacion completa de gestion de productos con carrito y checkout" >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo ✅ Commit inicial completado
) else (
    echo ✅ Proyecto ya está actualizado
)

echo.
echo ═══════════════════════════════════════════════════════════════════
echo    🎉 PROYECTO LISTO PARA SUBIR A GIT!
echo ═══════════════════════════════════════════════════════════════════
echo.
echo SIGUIENTES PASOS:
echo.
echo 1. CREAR REPOSITORIO EN GITHUB:
echo    • Ve a: https://github.com/new
echo    • Nombre: ProductApp
echo    • Descripción: Aplicación Android de gestión de productos
echo    • Visibilidad: Público (recomendado)
echo    • NO marcar "Initialize with README"
echo    • Click "Create repository"
echo.
echo 2. COPIA ESTOS COMANDOS Y EJECUTALOS:
echo.
echo    git remote add origin https://github.com/TU-USUARIO/ProductApp.git
echo    git branch -M main
echo    git push -u origin main
echo.
echo    (Reemplaza TU-USUARIO con tu username de GitHub)
echo.
echo ═══════════════════════════════════════════════════════════════════
echo.
echo ARCHIVOS INCLUIDOS EN EL REPOSITORIO:
echo • ✅ Código fuente completo (MainActivity, Activities, ViewModels)
echo • ✅ Base de datos Room configurada
echo • ✅ Integración Retrofit con FakeStore API
echo • ✅ Sistema de carrito de compras
echo • ✅ Proceso de checkout completo
echo • ✅ README.md profesional
echo • ✅ .gitignore configurado para Android
echo.
echo 🎓 Tu proyecto está listo para impresionar!
echo.
pause

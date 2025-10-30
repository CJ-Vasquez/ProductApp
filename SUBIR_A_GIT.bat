@echo off
color 0B
echo.
echo ═══════════════════════════════════════════════════════════════════
echo    📦 SUBIR PROYECTO A GIT - CONFIGURACION AUTOMATICA
echo ═══════════════════════════════════════════════════════════════════
echo.
echo Este script te ayudará a subir el proyecto ProductApp a tu repositorio Git.
echo Asegúrate de tener Git instalado y una cuenta en GitHub/GitLab.
echo.

echo [PASO 1] Verificando Git...
git --version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo ❌ Git no está instalado o no está en el PATH
    echo    Descarga Git desde: https://git-scm.com/downloads
    echo    Instálalo y vuelve a ejecutar este script
    pause
    exit /b 1
) else (
    echo ✅ Git encontrado
)

echo.
echo [PASO 2] Configuración inicial de Git...
echo Configurando usuario de Git (si no está configurado)

git config --global user.name >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    set /p git_name="Ingresa tu nombre completo: "
    git config --global user.name "%git_name%"
)

git config --global user.email >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    set /p git_email="Ingresa tu email: "
    git config --global user.email "%git_email%"
)

echo ✅ Usuario configurado:
git config --global user.name
git config --global user.email

echo.
echo [PASO 3] Inicializando repositorio...
if exist ".git" (
    echo ✅ Repositorio Git ya existe
) else (
    git init
    echo ✅ Repositorio Git inicializado
)

echo.
echo [PASO 4] Agregando archivos al repositorio...
git add .
if %ERRORLEVEL% EQU 0 (
    echo ✅ Archivos agregados correctamente
) else (
    echo ❌ Error al agregar archivos
    pause
    exit /b 1
)

echo.
echo [PASO 5] Realizando commit inicial...
git commit -m "Initial commit: ProductApp - Aplicación completa de gestión de productos con carrito y checkout"
if %ERRORLEVEL% EQU 0 (
    echo ✅ Commit inicial realizado
) else (
    echo ✅ Archivos ya están en el último commit
)

echo.
echo ═══════════════════════════════════════════════════════════════════
echo    🌐 CONFIGURACION DE REPOSITORIO REMOTO
echo ═══════════════════════════════════════════════════════════════════
echo.
echo OPCIONES:
echo [1] GitHub (github.com)
echo [2] GitLab (gitlab.com)
echo [3] Bitbucket (bitbucket.org)
echo [4] Otro servicio
echo [5] Solo local (sin repositorio remoto)
echo.
set /p git_option="Selecciona una opción (1-5): "

if "%git_option%"=="1" (
    set git_service=GitHub
    set git_base=https://github.com
    echo.
    echo INSTRUCCIONES PARA GITHUB:
    echo 1. Ve a https://github.com/new
    echo 2. Crea un repositorio llamado "ProductApp"
    echo 3. NO inicialices con README ^(ya tienes uno^)
    echo 4. Copia la URL del repositorio
    echo.
) else if "%git_option%"=="2" (
    set git_service=GitLab
    set git_base=https://gitlab.com
    echo.
    echo INSTRUCCIONES PARA GITLAB:
    echo 1. Ve a https://gitlab.com/projects/new
    echo 2. Crea un repositorio llamado "ProductApp"
    echo 3. NO inicialices con README ^(ya tienes uno^)
    echo 4. Copia la URL del repositorio
    echo.
) else if "%git_option%"=="3" (
    set git_service=Bitbucket
    set git_base=https://bitbucket.org
    echo.
    echo INSTRUCCIONES PARA BITBUCKET:
    echo 1. Ve a https://bitbucket.org/repo/create
    echo 2. Crea un repositorio llamado "ProductApp"
    echo 3. NO inicialices con README ^(ya tienes uno^)
    echo 4. Copia la URL del repositorio
    echo.
) else if "%git_option%"=="5" (
    echo.
    echo ✅ Repositorio configurado solo en local
    echo Tu proyecto está listo para desarrollo local con Git.
    echo Puedes agregarlo a un servicio remoto más tarde con:
    echo git remote add origin [URL_DEL_REPOSITORIO]
    echo git push -u origin main
    goto :end
) else (
    echo.
    set /p git_base="Ingresa la URL base de tu servicio Git: "
)

if not "%git_option%"=="5" (
    echo.
    set /p repo_url="Pega la URL completa de tu repositorio: "

    echo.
    echo [PASO 6] Conectando con repositorio remoto...
    git remote add origin "%repo_url%" 2>nul
    if %ERRORLEVEL% EQU 0 (
        echo ✅ Repositorio remoto agregado
    ) else (
        echo ⚠️  Repositorio remoto ya existe, actualizando...
        git remote set-url origin "%repo_url%"
    )

    echo.
    echo [PASO 7] Subiendo proyecto al repositorio...
    echo Esto puede tomar unos minutos dependiendo del tamaño del proyecto...

    git branch -M main 2>nul
    git push -u origin main

    if %ERRORLEVEL% EQU 0 (
        echo.
        echo ═══════════════════════════════════════════════════════════════════
        echo    🎉 PROYECTO SUBIDO EXITOSAMENTE!
        echo ═══════════════════════════════════════════════════════════════════
        echo.
        echo ✅ Tu proyecto ProductApp está ahora en: %repo_url%
        echo.
        echo PRÓXIMOS PASOS RECOMENDADOS:
        echo • Verifica que todos los archivos estén en el repositorio
        echo • Configura la descripción del repositorio
        echo • Agrega topics/tags: android, kotlin, mobile-app, cibertec
        echo • Considera hacer el repositorio público para portafolio
        echo.
        echo COMANDOS ÚTILES PARA EL FUTURO:
        echo git add .                     # Agregar cambios
        echo git commit -m "mensaje"       # Guardar cambios
        echo git push                      # Subir cambios
        echo git pull                      # Descargar cambios
        echo.
    ) else (
        echo.
        echo ❌ Error al subir el proyecto
        echo.
        echo POSIBLES SOLUCIONES:
        echo 1. Verifica tu conexión a internet
        echo 2. Confirma que la URL del repositorio sea correcta
        echo 3. Asegúrate de tener permisos en el repositorio
        echo 4. Si es un repositorio privado, configura autenticación
        echo.
        echo Puedes intentar manualmente con:
        echo git push -u origin main --force
    )
)

:end
echo.
echo ═══════════════════════════════════════════════════════════════════
echo    📋 RESUMEN DEL PROYECTO SUBIDO
echo ═══════════════════════════════════════════════════════════════════
echo.
echo PROYECTO: ProductApp - Aplicación de Gestión de Productos
echo TECNOLOGÍA: Android + Kotlin + Room + Retrofit
echo FUNCIONALIDADES:
echo • ✅ Gestión completa de productos (CRUD)
echo • ✅ Carrito de compras funcional
echo • ✅ Proceso de checkout con recibo
echo • ✅ Sincronización con FakeStore API
echo • ✅ Base de datos Room local
echo • ✅ Material Design 3
echo • ✅ Arquitectura MVVM
echo.
echo ARCHIVOS INCLUIDOS:
echo • ✅ Código fuente completo
echo • ✅ README.md profesional
echo • ✅ .gitignore configurado
echo • ✅ Documentación técnica
echo.
echo 🎓 Ideal para portafolio académico y profesional!
echo.
pause

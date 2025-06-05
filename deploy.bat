@echo off
setlocal enabledelayedexpansion

:: =================================================================
::                    USER MANAGEMENT DEPLOYMENT SCRIPT
:: =================================================================

echo.
echo ========================================
echo    USER MANAGEMENT - AUTO DEPLOYMENT
echo ========================================
echo.

:: Configuration variables
set TOMCAT_PATH=C:\Program Files\Apache Software Foundation\Tomcat 10.1
set WEBAPP_NAME=UserManagement
set PROJECT_DIR=%cd%

echo [INFO] Starting deployment process...
echo [INFO] Project Directory: %PROJECT_DIR%
echo [INFO] Tomcat Path: %TOMCAT_PATH%
echo [INFO] Web Application: %WEBAPP_NAME%
echo.

:: Step 1: Stop Tomcat
echo [STEP 1/6] Stopping Tomcat server...
cd /d "%TOMCAT_PATH%\bin"
call shutdown.bat >nul 2>&1
echo [SUCCESS] Tomcat shutdown initiated
echo.

:: Wait for Tomcat to stop
echo [WAIT] Waiting for Tomcat to fully stop...
timeout /t 5 /nobreak >nul
echo [SUCCESS] Wait completed
echo.

:: Step 2: Clean old deployment
echo [STEP 2/6] Cleaning old deployment...
if exist "%TOMCAT_PATH%\webapps\%WEBAPP_NAME%.war" (
    del "%TOMCAT_PATH%\webapps\%WEBAPP_NAME%.war"
    echo [SUCCESS] Removed old WAR file
) else (
    echo [INFO] No old WAR file found
)

if exist "%TOMCAT_PATH%\webapps\%WEBAPP_NAME%" (
    rmdir /s /q "%TOMCAT_PATH%\webapps\%WEBAPP_NAME%"
    echo [SUCCESS] Removed old webapp directory
) else (
    echo [INFO] No old webapp directory found
)
echo.

:: Step 3: Return to project directory and clean Maven
echo [STEP 3/6] Cleaning Maven project...
cd /d "%PROJECT_DIR%"
call mvn clean
if !errorlevel! neq 0 (
    echo [ERROR] Maven clean failed!
    pause
    exit /b 1
)
echo [SUCCESS] Maven clean completed
echo.

:: Step 4: Package the application
echo [STEP 4/6] Packaging application...
call mvn package -DskipTests
if !errorlevel! neq 0 (
    echo [ERROR] Maven package failed!
    pause
    exit /b 1
)
echo [SUCCESS] Application packaged successfully
echo.

:: Step 5: Deploy to Tomcat
echo [STEP 5/6] Deploying to Tomcat...
if exist "target\%WEBAPP_NAME%.war" (
    copy "target\%WEBAPP_NAME%.war" "%TOMCAT_PATH%\webapps\"
    echo [SUCCESS] WAR file deployed to Tomcat
) else (
    echo [ERROR] WAR file not found in target directory!
    pause
    exit /b 1
)
echo.

:: Step 6: Start Tomcat
echo [STEP 6/6] Starting Tomcat server...
cd /d "%TOMCAT_PATH%\bin"
call startup.bat
echo [SUCCESS] Tomcat startup initiated
echo.

:: Final status
echo ========================================
echo           DEPLOYMENT COMPLETED!
echo ========================================
echo.
echo [INFO] Application URL: http://localhost:8080/%WEBAPP_NAME%
echo [INFO] Direct User List: http://localhost:8080/%WEBAPP_NAME%/users
echo [INFO] Add New User: http://localhost:8080/%WEBAPP_NAME%/users?action=add
echo.
echo [WAIT] Waiting for Tomcat to fully start...
timeout /t 10 /nobreak >nul

:: Optional: Open browser
set /p OPEN_BROWSER="Do you want to open the application in browser? (y/n): "
if /i "!OPEN_BROWSER!"=="y" (
    start http://localhost:8080/%WEBAPP_NAME%
    echo [SUCCESS] Application opened in browser
)

echo.
echo [INFO] Deployment script completed successfully!
echo [TIP] You can run this script anytime to redeploy your application
echo.
pause
exit /b 0
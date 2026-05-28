@echo off
chcp 65001 > nul
setlocal enabledelayedexpansion
cd /d "%~dp0"

echo Checking project structure...

if not exist "src\asteroidgame\app\Main.java" (
    echo.
    echo ERROR: Main.java was not found.
    echo Expected location:
    echo src\asteroidgame\app\Main.java
    echo.
    echo Make sure this run_windows.bat is in the main project folder,
    echo the same folder where the src folder is located.
    echo.
    pause
    exit /b 1
)

where javac > nul 2>&1
if errorlevel 1 (
    echo.
    echo ERROR: javac was not found.
    echo Please install Java JDK or add it to your PATH.
    echo.
    pause
    exit /b 1
)

echo Compiling ASCII Asteroid Blaster...

if exist sources.txt del sources.txt
if exist out rmdir /s /q out
mkdir out

for /r "src" %%f in (*.java) do (
    set "file=%%f"
    set "file=!file:\=/!"
    echo "!file!" >> sources.txt
)

javac -encoding UTF-8 -d out @sources.txt

if errorlevel 1 (
    echo.
    echo Compilation failed.
    echo Please check the Java error messages above.
    echo.
    pause
    exit /b 1
)

echo.
echo Starting game...
java -Dfile.encoding=UTF-8 -cp out asteroidgame.app.Main

pause
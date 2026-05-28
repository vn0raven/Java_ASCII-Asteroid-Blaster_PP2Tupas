@echo off
chcp 65001 > nul
if not exist out mkdir out
javac -encoding UTF-8 -d out src\asteroidgame\app\*.java src\asteroidgame\core\*.java src\asteroidgame\managers\*.java src\asteroidgame\objects\*.java src\asteroidgame\ui\*.java
if errorlevel 1 pause && exit /b
java -Dfile.encoding=UTF-8 -cp out asteroidgame.app.Main
pause

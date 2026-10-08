@echo off
echo Compiling Smart Vaccination Management System...
javac VaccinationManagementSystem.java
if errorlevel 1 (
  echo Compilation failed.
  pause
  exit /b 1
)
echo Starting application...
java VaccinationManagementSystem
pause

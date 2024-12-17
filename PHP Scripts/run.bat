@echo off
:loop
php "%~dp0deleteEvery24Hours.php"
timeout /t 60 > nul
goto loop
@echo off
echo Compilando ObservaAção...

if not exist out mkdir out

dir /s /b src\*.java > sources.txt

javac -encoding UTF-8 -d out @sources.txt

if %errorlevel% == 0 (
    echo.
    echo Compilacao concluida com sucesso!
    echo Para executar: run.bat
) else (
    echo.
    echo Erro na compilacao. Verifique os erros acima.
)

del sources.txt

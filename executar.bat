@echo off
cd /d "%~dp0"

set JAR=Aplicacao-1.0-SNAPSHOT.jar
if not exist "%JAR%" (
    echo Nao encontrei %JAR% nesta pasta.
    echo Coloque este .bat na mesma pasta do .jar.
    pause
    exit /b 1
)

echo.
echo ============================================
echo  TESTE 1 - Roteiro do HTML
echo ============================================
java -cp "%JAR%" com.mycompany.aplicacao.TesteRoteiro
set R1=%ERRORLEVEL%

echo.
echo ============================================
echo  TESTE 2 - Estresse (varios livros e leitores)
echo ============================================
java -cp "%JAR%" com.mycompany.aplicacao.TesteEstresse
set R2=%ERRORLEVEL%

echo.
if %R1%==0 if %R2%==0 (
    echo ============================================
    echo  RESULTADO: TODOS OS TESTES PASSARAM
    echo ============================================
) else (
    echo ============================================
    echo  RESULTADO: HOUVE FALHAS - veja acima
    echo ============================================
)
pause
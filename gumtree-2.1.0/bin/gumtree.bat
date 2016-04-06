@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  gumtree startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and GUMTREE_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\dist-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\core-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\client-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\client.diff-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.antlr-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.antlr-antlr-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.antlr-css-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.antlr-json-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.antlr-php-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.antlr-r-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.antlr-xml-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.c-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.jdt-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.js-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\gen.ruby-2.1.0-SNAPSHOT.jar;%APP_HOME%\lib\simmetrics-core-3.2.3.jar;%APP_HOME%\lib\trove4j-3.0.3.jar;%APP_HOME%\lib\gson-2.4.jar;%APP_HOME%\lib\reflections-0.9.10.jar;%APP_HOME%\lib\nanohttpd-webserver-2.1.1.jar;%APP_HOME%\lib\rendersnake-1.9.0.jar;%APP_HOME%\lib\antlr-3.5.2.jar;%APP_HOME%\lib\org.eclipse.jdt.core-3.11.0.v20150602-1242.jar;%APP_HOME%\lib\runtime-3.10.0-v20140318-2214.jar;%APP_HOME%\lib\org.eclipse.core.resources-3.10.0.v20150423-0755.jar;%APP_HOME%\lib\rhino-1.7.7.jar;%APP_HOME%\lib\jrubyparser-0.5.3.jar;%APP_HOME%\lib\guava-18.0.jar;%APP_HOME%\lib\commons-codec-1.10.jar;%APP_HOME%\lib\javassist-3.18.2-GA.jar;%APP_HOME%\lib\annotations-2.0.1.jar;%APP_HOME%\lib\nanohttpd-2.1.1.jar;%APP_HOME%\lib\junit-4.8.2.jar;%APP_HOME%\lib\servlet-api-2.4.jar;%APP_HOME%\lib\commons-lang3-3.1.jar;%APP_HOME%\lib\commons-io-2.0.1.jar;%APP_HOME%\lib\spring-webmvc-4.1.6.RELEASE.jar;%APP_HOME%\lib\jtidy-r938.jar;%APP_HOME%\lib\guice-3.0.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\antlr-runtime-3.5.2.jar;%APP_HOME%\lib\ST4-4.0.8.jar;%APP_HOME%\lib\osgi-3.10.0-v20140606-1445.jar;%APP_HOME%\lib\common-3.6.200-v20130402-1505.jar;%APP_HOME%\lib\jobs-3.6.0-v20140424-0053.jar;%APP_HOME%\lib\registry-3.5.400-v20140428-1507.jar;%APP_HOME%\lib\preferences-3.5.200-v20140224-1527.jar;%APP_HOME%\lib\contenttype-3.4.200-v20140207-1251.jar;%APP_HOME%\lib\app-1.3.200-v20130910-1609.jar;%APP_HOME%\lib\spring-beans-4.1.6.RELEASE.jar;%APP_HOME%\lib\spring-context-4.1.6.RELEASE.jar;%APP_HOME%\lib\spring-core-4.1.6.RELEASE.jar;%APP_HOME%\lib\spring-expression-4.1.6.RELEASE.jar;%APP_HOME%\lib\spring-web-4.1.6.RELEASE.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\cglib-2.2.1-v20090111.jar;%APP_HOME%\lib\spring-aop-4.1.6.RELEASE.jar;%APP_HOME%\lib\commons-logging-1.2.jar;%APP_HOME%\lib\asm-3.1.jar

@rem Execute gumtree
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GUMTREE_OPTS%  -classpath "%CLASSPATH%" com.github.gumtreediff.client.Run %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable GUMTREE_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%GUMTREE_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega

@ECHO OFF
REM ---------------------------------------------------------------------------
REM Multi-Phasic Applications: SquirrelJME
REM     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
REM     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
REM ---------------------------------------------------------------------------
REM SquirrelJME is under the GNU General Public License v3, or later.
REM For more information see license.mkd.
REM ---------------------------------------------------------------------------
REM DESCRIPTION: Builds whatever is needed to support hairball and then invokes
REM it using the host virtual machine.

REM Location of EXE file (for out of tree builds)
set __EXEDIR=%~dp0

REM Do not give any variable assignments to the calling shell
setlocal
setlocal enableextensions

REM reset error level
set ERRORLEVEL=
cmd /c "exit /b 0"

REM Set some variables
if not defined JAVA set JAVA=java
if not defined JAVAC set JAVAC=javac
if not defined BOOTSTRAP_CLASS set BOOTSTRAP_CLASS=Build

REM Make sure commands exist (try to call them(
%JAVA% -version 2> NUL > NUL
if %ERRORLEVEL% neq 0 (
	echo The java command is missing, please set %%JAVA%%.
	exit /b 1
)

%JAVAC% -version 2> NUL > NUL
if %ERRORLEVEL% neq 0 (
	echo The javac command is missing, please set %%JAVAC%%.
	exit /b 1
)

REM If the build system class does not exist, compile it
set __HB_VCLS=%BOOTSTRAP_CLASS%.class
set __HB_VSRC=%__EXEDIR%\%BOOTSTRAP_CLASS%.java
if not exist %__HB_VCLS% (
	echo Compiling the build system...
	
	REM Compile it
	%JAVAC% -source 1.7 -target 1.7 -d . %__HB_VSRC%
	if %ERRORLEVEL% neq 0 (
		REM Note it
		echo Failed to compile the build system.
		
		REM Fail
		exit /b 1
	)
)

REM Execute Java
%JAVA% -Dproject.root=%__EXEDIR% %BOOTSTRAP_CLASS% %*

REM End of script, return with java command error (hopefully)
exit /b %ERRORLEVEL%


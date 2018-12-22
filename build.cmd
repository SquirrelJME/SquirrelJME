REM @ECHO OFF
REM ---------------------------------------------------------------------------
REM Multi-Phasic Applications: SquirrelJME
REM     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
REM     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
REM ---------------------------------------------------------------------------
REM SquirrelJME is under the GNU General Public License v3, or later.
REM See license.mkd for licensing and copyright information.
REM ---------------------------------------------------------------------------
REM DESCRIPTION: Builds whatever is needed to support the builder and then
REM invokes it using the host virtual machine.

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
if not defined BOOTSTRAP_CLASS set BOOTSTRAP_CLASS=NewBootstrap

REM Make sure commands exist (try to call them)
%JAVA% -version 2> NUL > NUL
if %ERRORLEVEL% neq 0 (
	echo The java command is missing, please set %%JAVA%%.
	exit /b 2
)

%JAVAC% -version 2> NUL > NUL
if %ERRORLEVEL% neq 0 (
	echo The javac command is missing, please set %%JAVAC%%.
	exit /b 3
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
		exit /b 4
	)
)

REM Execute Java
%JAVA% -Dproject.root=%__EXEDIR% ^
	-Dcc.squirreljme.bootstrap.binary=. ^
	-Dcc.squirreljme.builder.root=%__EXEDIR% ^
	%BOOTSTRAP_CLASS% %*

REM Failed to build the bootstrap (stage 1)
if %ERRORLEVEL% neq 0 (
	echo Failed to build the build system.
	exit /b %ERRORLEVEL%
)

REM Execute Java, since Proxy interfaces are a mess, a double invocation of
REM the JVM is performed.
%JAVA% -Dproject.root=%__EXEDIR% ^
	-Dcc.squirreljme.bootstrap.binary=. ^
	-Dcc.squirreljme.builder.root=%__EXEDIR% ^
	-Dcc.squirreljme.runtime.javase.java=%JAVA% ^
	-Dcc.squirreljme.runtime.javase.bootpath=sjmeboot.jar ^
	-Dcc.squirreljme.home=home ^
	-jar sjmeboot.jar %*

REM Failed?
exit /b %ERRORLEVEL%


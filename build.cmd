@ECHO OFF
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
SET __EXEDIR=%~dp0

REM Do NOT give any variable assignments to the calling shell
setlocal
setlocal enableextensions

REM reset error level
SET ERRORLEVEL=
cmd /c "EXIT /b 0"

REM Set some variables
IF NOT DEFINED JAVA SET JAVA=java
IF NOT DEFINED JAVAC SET JAVAC=javac
IF NOT DEFINED BOOTSTRAP_CLASS SET BOOTSTRAP_CLASS=NewBootstrap

REM Make sure commands exist (try to call them)
%JAVA% -version 2> NUL > NUL
IF %ERRORLEVEL% NEQ 0 (
	ECHO The java command is missing, please SET %%JAVA%%.
	EXIT /b 2
)

%JAVAC% -version 2> NUL > NUL
IF %ERRORLEVEL% NEQ 0 (
	ECHO The javac command is missing, please SET %%JAVAC%%.
	EXIT /b 3
)

REM If the build system class does NOT exist, compile it
SET __HB_VCLS=%BOOTSTRAP_CLASS%.class
SET __HB_VSRC=%__EXEDIR%\%BOOTSTRAP_CLASS%.java
IF NOT EXIST %__HB_VCLS% (
	ECHO Compiling the build system...
	%JAVAC% -source 1.7 -target 1.7 -d . %__HB_VSRC%
	IF %ERRORLEVEL% NEQ 0 (
		ECHO Failed to compile the build system.
		EXIT /b 4
	)
)

REM Execute Java
%JAVA% -Dproject.root=%__EXEDIR% ^
	-Dcc.squirreljme.bootstrap.binary=. ^
	-Dcc.squirreljme.builder.root=%__EXEDIR% ^
	%BOOTSTRAP_CLASS% %*

REM Failed to build the bootstrap (stage 1)
IF %ERRORLEVEL% NEQ 0 (
	ECHO Failed to build the build system.
	EXIT /b %ERRORLEVEL%
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
EXIT /b %ERRORLEVEL%


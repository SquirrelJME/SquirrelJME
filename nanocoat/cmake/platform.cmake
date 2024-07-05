# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Platform information

# Determine the host platform, if not set
if(NOT DEFINED SQUIRRELJME_HOST_PLATFORM)
	squirreljme_util(platformExePath platform)
	execute_process(COMMAND "${platformExePath}"
		OUTPUT_VARIABLE SQUIRRELJME_HOST_PLATFORM
		ERROR_VARIABLE platformExeIgnore
		TIMEOUT 16)
endif()

# Determine the platform we are targeting
# https://cmake.org/cmake/help/latest/variable/CMAKE_SYSTEM_NAME.html
if(NOT DEFINED SQUIRRELJME_PLATFORM)
	if("${CMAKE_SYSTEM_NAME}" STREQUAL "BeOS")
		set(SQUIRRELJME_PLATFORM "beos")
	elseif(CMAKE_SYSTEM_NAME STREQUAL "FreeBSD" OR
		CMAKE_SYSTEM_NAME STREQUAL "OpenBSD" OR
		CMAKE_SYSTEM_NAME STREQUAL "NetBSD")
		set(SQUIRRELJME_PLATFORM "bsd")
	elseif("${CMAKE_SYSTEM_NAME}" STREQUAL "CYGWIN" OR
		"${CMAKE_SYSTEM_NAME}" STREQUAL "MSYS2")
		set(SQUIRRELJME_PLATFORM "cygwin")
	elseif("${CMAKE_SYSTEM_NAME}" STREQUAL "DOS")
		set(SQUIRRELJME_PLATFORM "dos")
	elseif(LINUX OR "${CMAKE_SYSTEM_NAME}" STREQUAL "Linux")
		set(SQUIRRELJME_PLATFORM "linux")
	elseif(APPLE OR "${CMAKE_SYSTEM_NAME}" STREQUAL "Darwin")
		set(SQUIRRELJME_PLATFORM "macos")
	elseif(WIN32 OR "${CMAKE_SYSTEM_NAME}" STREQUAL "Windows")
		set(SQUIRRELJME_PLATFORM "win32")
	elseif("${CMAKE_SYSTEM_NAME}" STREQUAL "WindowsCE")
		set(SQUIRRELJME_PLATFORM "wince")
	else()
		set(SQUIRRELJME_PLATFORM "${SQUIRRELJME_HOST_PLATFORM}")
	endif()
endif()

if (NOT "${SQUIRRELJME_HOST_PLATFORM}" STREQUAL "${SQUIRRELJME_PLATFORM}")
	set(SQUIRRELJME_CROSS_COMPILE YES)
else()
	set(SQUIRRELJME_CROSS_COMPILE NO)
endif()

# Debug
message(STATUS "Host Platform: ${SQUIRRELJME_HOST_PLATFORM}")
message(STATUS "Target Platform: ${SQUIRRELJME_PLATFORM}")
message(STATUS "Cross Compiled? ${SQUIRRELJME_CROSS_COMPILE}")

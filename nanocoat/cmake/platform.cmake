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
	execute_process(COMMAND "${platformExePath}" "-p"
		OUTPUT_VARIABLE SQUIRRELJME_HOST_PLATFORM
		ERROR_VARIABLE platformExeIgnore
		TIMEOUT 16)
	execute_process(COMMAND "${platformExePath}" "-a"
		OUTPUT_VARIABLE SQUIRRELJME_HOST_ARCH
		ERROR_VARIABLE platformExeIgnore
		TIMEOUT 16)
endif()

# Some compile definitions which may shine light on the target platform
string(FIND "${COMPILE_DEFINITIONS}" "-D_3DS"
	SQUIRRELJME_CHECK_CDEF_3DS)

# Target architecture
if(NOT DEFINED SQUIRRELJME_ARCH)
	message(STATUS "Input CPU: ${CMAKE_SYSTEM_PROCESSOR}")

	if("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "AMD64" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "EM64T" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "x86_64" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "x86-64" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "amd64")
		set(SQUIRRELJME_ARCH "amd64")
	elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "armv6k" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "arm32" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "armel" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "armbe" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "arm" OR
		"${SQUIRRELJME_CHECK_CDEF_3DS}" GREATER_EQUAL 0)
		set(SQUIRRELJME_ARCH "arm32")
	elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "arm64" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "aarch64" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "aarch64_be")
		set(SQUIRRELJME_ARCH "arm64")
	elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "i286" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "I86" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "ia16")
		set(SQUIRRELJME_ARCH "ia16")
	elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "i386" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "i486" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "i586" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "i686" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "X86" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "x86" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "ia32")
		set(SQUIRRELJME_ARCH "ia32")
	elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "IA64" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "ia64" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "Itanium" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "Itanic")
		set(SQUIRRELJME_ARCH "ia64")
	elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "mips" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "mipsel" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "mipseb" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "mips32" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "mips32el" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "mips32eb")
		set(SQUIRRELJME_ARCH "mips32")
	elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "mips64" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "mips64el" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "mips64eb")
		set(SQUIRRELJME_ARCH "mips64")
	elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "ppc" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "powerpc")
		set(SQUIRRELJME_ARCH "powerpc32")
	elseif("${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "ppc64" OR
		"${CMAKE_SYSTEM_PROCESSOR}" STREQUAL "powerpc64")
		set(SQUIRRELJME_ARCH "powerpc64")
	else()
		set(SQUIRRELJME_ARCH "${SQUIRRELJME_HOST_ARCH}")
	endif()
endif()

# Determine the platform we are targeting
# https://cmake.org/cmake/help/latest/variable/CMAKE_SYSTEM_NAME.html
if(NOT DEFINED SQUIRRELJME_PLATFORM)
	message(STATUS "Input Platform: ${CMAKE_SYSTEM_NAME}")

	if("${SQUIRRELJME_CHECK_CDEF_3DS}" GREATER_EQUAL 0)
		set(SQUIRRELJME_PLATFORM "3ds")
	elseif("${CMAKE_SYSTEM_NAME}" STREQUAL "BeOS")
		set(SQUIRRELJME_PLATFORM "beos")
	elseif(BSD OR CMAKE_SYSTEM_NAME STREQUAL "FreeBSD" OR
		CMAKE_SYSTEM_NAME STREQUAL "OpenBSD" OR
		CMAKE_SYSTEM_NAME STREQUAL "NetBSD")
		set(SQUIRRELJME_PLATFORM "bsd")
	elseif("${CMAKE_SYSTEM_NAME}" STREQUAL "CYGWIN" OR
		"${CMAKE_SYSTEM_NAME}" STREQUAL "MSYS2")
		set(SQUIRRELJME_PLATFORM "cygwin")
	elseif("${CMAKE_SYSTEM_NAME}" STREQUAL "DOS")
		set(SQUIRRELJME_PLATFORM "dos")
	elseif(EMSCRIPTEN OR "${CMAKE_SYSTEM_NAME}" STREQUAL "Emscripten")
		set(SQUIRRELJME_PLATFORM "emscripten")
	elseif(LINUX OR "${CMAKE_SYSTEM_NAME}" STREQUAL "Linux")
		set(SQUIRRELJME_PLATFORM "linux")
	elseif(APPLE OR "${CMAKE_SYSTEM_NAME}" STREQUAL "Darwin")
		set(SQUIRRELJME_PLATFORM "macos")
	elseif(WIN32 OR "${CMAKE_SYSTEM_NAME}" STREQUAL "Windows")
		if("${SQUIRRELJME_ARCH}" STREQUAL "ia16")
			set(SQUIRRELJME_PLATFORM "win16")
		else()
			set(SQUIRRELJME_PLATFORM "win32")
		endif()
	elseif("${CMAKE_SYSTEM_NAME}" STREQUAL "WindowsCE")
		set(SQUIRRELJME_PLATFORM "wince")
	elseif("${CMAKE_SYSTEM_NAME}" STREQUAL "Generic")
		set(SQUIRRELJME_PLATFORM "unknown")
	else()
		set(SQUIRRELJME_PLATFORM "${SQUIRRELJME_HOST_PLATFORM}")
	endif()
endif()

if (NOT "${SQUIRRELJME_HOST_PLATFORM}" STREQUAL "${SQUIRRELJME_PLATFORM}" OR
	NOT "${SQUIRRELJME_HOST_ARCH}" STREQUAL "${SQUIRRELJME_ARCH}")
	set(SQUIRRELJME_IS_CROSS_COMPILE YES)
else()
	set(SQUIRRELJME_IS_CROSS_COMPILE NO)
endif()

# It's a UNIX system! I know this!
if(SQUIRRELJME_PLATFORM STREQUAL "bsd" OR
	SQUIRRELJME_PLATFORM STREQUAL "linux" OR
	SQUIRRELJME_PLATFORM STREQUAL "macos")
	set(SQUIRRELJME_IS_UNIX YES)
else()
	set(SQUIRRELJME_IS_UNIX NO)
endif()

# It's a Windows system? Must be another flavor of UNIX!
if(SQUIRRELJME_HOST_PLATFORM STREQUAL "win16" OR
	SQUIRRELJME_HOST_PLATFORM STREQUAL "win32" OR
	SQUIRRELJME_HOST_PLATFORM STREQUAL "wince")
	set(SQUIRRELJME_IS_WINDOWS YES)
else()
	set(SQUIRRELJME_IS_WINDOWS NO)
endif()

# Is this RetroArch? Any kind of RetroArch build?
if(RETROARCH OR ENV{RETROARCH} OR
	LIBRETRO_STATIC OR ENV{LIBRETRO_STATIC} OR
	LIBRETRO_SUFFIX OR ENV{LIBRETRO_SUFFIX} OR
	ENV{LIBRETRO})
	set(SQUIRRELJME_IS_LIBRETRO YES)
else()
	set(SQUIRRELJME_IS_LIBRETRO NO)
endif()

# Debug
message(STATUS "Host: ${SQUIRRELJME_HOST_PLATFORM}/${SQUIRRELJME_HOST_ARCH}")
message(STATUS "Target: ${SQUIRRELJME_PLATFORM}/${SQUIRRELJME_ARCH}")
message(STATUS "Cross Compiled? ${SQUIRRELJME_IS_CROSS_COMPILE}")
message(STATUS "Is UNIX? ${SQUIRRELJME_IS_UNIX}")
message(STATUS "Is Windows? ${SQUIRRELJME_IS_WINDOWS}")
message(STATUS "Is libretro? ${SQUIRRELJME_IS_LIBRETRO}")

# Options
## Dynamic libraries?
if("${SQUIRRELJME_PLATFORM}" STREQUAL "dos" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "emscripten" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "palmos" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "wince" OR
	LIBRETRO_STATIC OR ENV{LIBRETRO_STATIC})
	set(SQUIRRELJME_ENABLE_DYLIB_DEFAULT OFF)
else()
	set(SQUIRRELJME_ENABLE_DYLIB_DEFAULT ON)
endif()

## Enable LUA?
if("${SQUIRRELJME_PLATFORM}" STREQUAL "dos" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "classicmac" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "emscripten" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "palmos" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "wince" OR
	"${SQUIRRELJME_ARCH}" STREQUAL "ia16")
	set(SQUIRRELJME_ENABLE_LUA_DEFAULT OFF)
else()
	set(SQUIRRELJME_ENABLE_LUA_DEFAULT ON)
endif()

## Enable MiniZ?
if("${SQUIRRELJME_ARCH}" STREQUAL "ia16")
	set(SQUIRRELJME_ENABLE_MINIZ_DEFAULT OFF)
else()
	set(SQUIRRELJME_ENABLE_MINIZ_DEFAULT ON)
endif()

## Netscape JRI Interface?
if(SQUIRRELJME_ENABLE_DYLIB_DEFAULT AND
	("${SQUIRRELJME_PLATFORM}" STREQUAL "classicmac" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "win16"))
	set(SQUIRRELJME_ENABLE_FRONTEND_JRI_DEFAULT ON)
else()
	set(SQUIRRELJME_ENABLE_FRONTEND_JRI_DEFAULT OFF)
endif()

## LibRetro?
if("${SQUIRRELJME_ARCH}" STREQUAL "ia16" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "classicmac" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "palmos" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "win16" OR
	"${SQUIRRELJME_PLATFORM}" STREQUAL "wince")
	set(SQUIRRELJME_ENABLE_FRONTEND_LIBRETRO_DEFAULT OFF)
else()
	set(SQUIRRELJME_ENABLE_FRONTEND_LIBRETRO_DEFAULT ON)
endif()

## Testing?
if(SQUIRRELJME_IS_CROSS_COMPILE OR
	${CMAKE_VERSION} VERSION_LESS_EQUAL "3.12")
	set(SQUIRRELJME_ENABLE_TESTING_DEFAULT OFF)
else()
	set(SQUIRRELJME_ENABLE_TESTING_DEFAULT ON)
endif()

# Option flags which are available
option(SQUIRRELJME_ENABLE_DYLIB "Enable Dynamic Libraries"
	${SQUIRRELJME_ENABLE_DYLIB_DEFAULT})
option(SQUIRRELJME_ENABLE_FRONTEND_JRI "Enable Front End: JRI"
	${SQUIRRELJME_ENABLE_FRONTEND_JRI_DEFAULT})
option(SQUIRRELJME_ENABLE_FRONTEND_LIBRETRO "Enable Front End: LibRetro"
	${SQUIRRELJME_ENABLE_FRONTEND_LIBRETRO_DEFAULT})
option(SQUIRRELJME_ENABLE_LUA "Enable Lua"
	${SQUIRRELJME_ENABLE_LUA_DEFAULT})
option(SQUIRRELJME_ENABLE_MINIZ "Enable MiniZ"
	${SQUIRRELJME_ENABLE_MINIZ_DEFAULT})
option(SQUIRRELJME_ENABLE_TESTING "Enable Host System Tests"
	${SQUIRRELJME_ENABLE_TESTING_DEFAULT})

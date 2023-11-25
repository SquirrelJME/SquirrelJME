# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Defines the base project and the versioning info

# Easier includes
include("${CMAKE_SOURCE_DIR}/cmake/easier-includes.cmake" NO_POLICY_SCOPE)

# Needed for some ancient RetroArch toolchains
if(${CMAKE_VERSION} VERSION_LESS "3.13")
	message("Ancient CMake has been detected (${CMAKE_VERSION})")
	cmake_minimum_required(VERSION 3.7)

	# Needs to be this or newer
else()
	message("Detected CMake (${CMAKE_VERSION})")
	cmake_minimum_required(VERSION 3.13)
endif()

# Determine version information
squirreljme_include("identify-squirreljme-version.cmake")

# Define project
if(${CMAKE_VERSION} VERSION_LESS "3.12")
	project(SquirrelJME
		VERSION ${SQUIRRELJME_VERSION}
		LANGUAGES C )#CXX)
else()
	project(SquirrelJME
		VERSION ${SQUIRRELJME_VERSION}
		DESCRIPTION "SquirrelJME is a Java ME 8 \
Virtual Machine for embedded and Internet of Things devices."
		HOMEPAGE_URL https://squirreljme.cc/
		LANGUAGES C )#CXX)
endif()

# Fixes and otherwise for compatibility
squirreljme_include("fixes.cmake")

# CMake Utilities, compiled and used during build
squirreljme_include("utils.cmake")

# To Emily and Near...
squirreljme_include("i-miss-you.cmake")

# Everything is C99
set(CMAKE_C_STANDARD 99)
set(CMAKE_C_STANDARD_REQUIRED True)

# Declare Version
add_compile_definitions(
	SQUIRRELJME_VERSION_TRIM=${SQUIRRELJME_VERSION})

# Valgrind Support on UNIX OSes, if debugging
if(ANDROID OR APPLE OR BSD OR LINUX OR
	CMAKE_SYSTEM_NAME STREQUAL "Linux" OR
	CMAKE_SYSTEM_NAME STREQUAL "Solaris" OR
	CMAKE_SYSTEM_NAME STREQUAL "Darwin" OR
	CMAKE_SYSTEM_NAME STREQUAL "FreeBSD" OR
	CMAKE_SYSTEM_NAME STREQUAL "OpenBSD" OR
	CMAKE_SYSTEM_NAME STREQUAL "NetBSD")
	if(CMAKE_BUILD_TYPE STREQUAL "Debug" OR
		CMAKE_BUILD_TYPE STREQUAL "RelWithDebInfo")
		squirreljme_include("valgrind.cmake")
	endif()
endif()

# Enable support for testing, this is needed here otherwise testing will not
# work at all! Major headache this has caused...
# From: https://cmake.org/cmake/help/v3.13/command/enable_testing.html
# > Note that ctest expects to find a test file in the build directory root.
# > Therefore, this command should be in the source directory root.
if(SQUIRRELJME_ENABLE_TESTING)
	enable_testing()
endif()

# CPack for installing
if(SQUIRRELJME_ENABLE_PACKING)
	squirreljme_include("packing.cmake")
endif()

# Include the base core first because it is completely standalone and it
# uses nothing else
add_subdirectory(src)

# Front ends
add_subdirectory(frontend)

# Tests first since we add to these and when the ROMs register themselves
# they will get their tests added accordingly
if(SQUIRRELJME_ENABLE_TESTING)
	add_subdirectory(tests)
endif()

# Directory where the ROM compiled sources exist
add_subdirectory(rom)

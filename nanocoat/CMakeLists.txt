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
	message(STATUS "Ancient CMake has been detected (${CMAKE_VERSION})")
	cmake_minimum_required(VERSION 3.7)

	# Needs to be this or newer
else()
	message(STATUS "Detected CMake (${CMAKE_VERSION})")
	cmake_minimum_required(VERSION 3.13)
endif()

# Determine version information
squirreljme_include("identify-squirreljme-version.cmake")

# Fixes and otherwise for compatibility
squirreljme_include("pre-fixes.cmake")

# Define project
if(${CMAKE_VERSION} VERSION_LESS "3.12")
	project(SquirrelJME
		VERSION ${SQUIRRELJME_VERSION}
		LANGUAGES C)
else()
	project(SquirrelJME
		VERSION ${SQUIRRELJME_VERSION}
		DESCRIPTION "SquirrelJME is a Java ME 8 \
Virtual Machine for embedded and Internet of Things devices."
		HOMEPAGE_URL https://squirreljme.cc/
		LANGUAGES C)
endif()

# Fixes and otherwise for compatibility
squirreljme_include("fixes.cmake")

# Hardening options
squirreljme_include("hardening.cmake")

# CMake Utilities, compiled and used during build
squirreljme_include("utils.cmake")

# Doxygen
squirreljme_include("doxygen.cmake")

# Threading and atomics
squirreljme_include("threads.cmake")

# To Emily and Near...
squirreljme_include("i-miss-you.cmake")

# Everything is C99
set(CMAKE_C_STANDARD 99)
set(CMAKE_C_STANDARD_REQUIRED True)

# Assets and otherwise, such as icons, etc.
squirreljme_include("assets.cmake")

# Multi-library declarations
squirreljme_include("multilib.cmake")

# Visual studio does not like a bunch of standard C functions, even those
# that take sizes...
if(MSVC)
	add_compile_definitions(_CRT_SECURE_NO_WARNINGS=1)
endif()

# Declare Version
add_compile_definitions(
	SQUIRRELJME_VERSION_TRIM=${SQUIRRELJME_VERSION})

# Debugging?
if(SQUIRRELJME_IS_DEBUG)
	add_compile_definitions(SJME_CONFIG_DEBUG=1)

	if(SQUIRRELJME_ENABLE_TESTING)
		add_compile_definitions(SJME_CONFIG_UNIT_TEST=1)
	endif()
endif()

# Valgrind Support on UNIX OSes, if debugging is enabled
if(ANDROID OR SQUIRRELJME_IS_UNIX)
	if(SQUIRRELJME_IS_DEBUG)
		squirreljme_include("valgrind.cmake")
	endif()
endif()

# Where should dynamic libraries go when output?
if(NOT DEFINED SQUIRRELJME_DYLIB_OUTPUT_DIR)
	set(SQUIRRELJME_DYLIB_OUTPUT_DIR
		"${CMAKE_BINARY_DIR}")
endif()

# Enable support for testing, this is needed here otherwise testing will not
# work at all! Major headache this has caused...
# From: https://cmake.org/cmake/help/v3.13/command/enable_testing.html
# > Note that ctest expects to find a test file in the build directory root.
# > Therefore, this command should be in the source directory root.
if(SQUIRRELJME_ENABLE_TESTING OR
	NOT DEFINED SQUIRRELJME_ENABLE_TESTING)
	enable_testing()
else()
	message(WARNING "Testing was disabled "
		"(${SQUIRRELJME_ENABLE_TESTING})...")
endif()

# CPack for installing
if(SQUIRRELJME_ENABLE_PACKING OR
	NOT DEFINED SQUIRRELJME_ENABLE_PACKING)
	squirreljme_include("packing.cmake")
endif()

# SquirrelJME Base Library, everything depends on this essentially
add_subdirectory(lib/base)

# Include the base core first because it is completely standalone and it
# uses nothing else
add_subdirectory(src)

# Internal Libraries other than Base
add_subdirectory(lib)

# Tests first since we add to these and when the ROMs register themselves
# they will get their tests added accordingly
if(SQUIRRELJME_ENABLE_TESTING OR
	NOT DEFINED SQUIRRELJME_ENABLE_TESTING)
	add_subdirectory(tests)
else()
	message(WARNING "Testing was disabled "
		"(${SQUIRRELJME_ENABLE_TESTING})...")
endif()

# Front ends
add_subdirectory(frontend)

# Directory where the ROM compiled sources exist
add_subdirectory(rom)

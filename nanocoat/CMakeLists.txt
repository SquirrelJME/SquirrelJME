# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Defines the base project and the versioning info

# Use the best version possible
# Otherwise, fallback to the lowest viable version
if(CMAKE_VERSION VERSION_GREATER_EQUAL 3.13)
	cmake_minimum_required(VERSION 3.13..3.31)
else()
	cmake_minimum_required(VERSION 3.1)
endif()

# Easier includes
set(SQUIRRELJME_NANOCOAT_SOURCE_DIR "${CMAKE_SOURCE_DIR}")
include("${CMAKE_SOURCE_DIR}/cmake/easier-includes.cmake"
	NO_POLICY_SCOPE)

# Include backports
squirreljme_include_nanocoat("cmake-bp.cmake")

# Up-to-date info for NanoCoat, used to trigger reconfigures
squirreljme_include("up-to-date-when.cmake")

# Determine version information from the base file
squirreljme_include("identify-squirreljme-version-file.cmake")

# Special compiler shims
squirreljme_include("compiler-shim.cmake")

# Define project
if(NOT squirreljme_bp_version_3_12 OR
	"${CMAKE_VERSION}" VERSION_LESS "3.12")
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

# Enable C (again)
enable_language(C)

# Is this a GCC compiler?
if(CMAKE_COMPILER_IS_GNUCC OR
	CMAKE_COMPILER_IS_GNUCXX OR
	"${CMAKE_C_COMPILER_ID}" STREQUAL "GNU" OR
	"${CMAKE_C_COMPILER_ID}" STREQUAL "Clang" OR
	"${CMAKE_C_COMPILER_ID}" STREQUAL "AppleClang" OR
	"${CMAKE_C_COMPILER_ID}" STREQUAL "ARMClang" OR
	"${CMAKE_C_COMPILER_ID}" STREQUAL "CrayClang" OR
	"${CMAKE_C_COMPILER_ID}" STREQUAL "FujitsuClang" OR
	"${CMAKE_C_COMPILER_ID}" STREQUAL "TIClang" OR
	"${CMAKE_C_COMPILER_ID}" STREQUAL "XLClang" OR
	"${CMAKE_C_COMPILER_ID}" STREQUAL "IBMClang")
	set(SQUIRRELJME_IS_GCC TRUE)
else()
	set(SQUIRRELJME_IS_GCC FALSE)
endif()

# Needed for language check
include(CheckLanguage)

# Use GCC as the assembler?
if(SQUIRRELJME_IS_GCC)
	# Alias to CC
	set(CMAKE_ASM_COMPILER "${CMAKE_C_COMPILER}" CACHE STRING "" FORCE)
	set(CMAKE_ASM_COMPILER "${CMAKE_C_COMPILER}")

# Otherwise look for one
else()
	check_language(ASM)
	if(CMAKE_ASM_COMPILER)
		enable_language(ASM)
		set(SQUIRRELJME_HAS_ASM TRUE)
	else()
		set(SQUIRRELJME_HAS_ASM FALSE)
	endif()
endif()

# Is there Objective-C?
check_language(OBJC)
if(CMAKE_OBJC_COMPILER)
	enable_language(OBJC)
	set(SQUIRRELJME_HAS_OBJC TRUE)
else()
	set(SQUIRRELJME_HAS_OBJC FALSE)
endif()

# Is there C++?
check_language(CXX)
if(CMAKE_CXX_COMPILER)
	enable_language(CXX)
	set(SQUIRRELJME_HAS_CXX TRUE)
else()
	set(SQUIRRELJME_HAS_CXX FALSE)
endif()

# Debug the languages
message(STATUS "C: ${CMAKE_C_COMPILER}")
message(STATUS "ASM: ${CMAKE_ASM_COMPILER}")
message(STATUS "OBJC: ${CMAKE_OBJC_COMPILER}")
message(STATUS "CXX: ${CMAKE_CXX_COMPILER}")

# Determine version information
squirreljme_include("identify-squirreljme-version.cmake")

# Soft patching of CMake for older version support
squirreljme_include("cmake-patch.cmake")

# Fixes and otherwise for compatibility
squirreljme_include("pre-fixes.cmake")

# Include system map identifications
squirreljme_include("system-map.cmake")

# CMake Utilities, compiled and used during build
squirreljme_include("utils.cmake")

# Platform information
squirreljme_include("platform.cmake")

# Verbosity
squirreljme_include("verbose.cmake")

# Valgrind Support on UNIX OSes, if debugging is enabled
if(NOT SQUIRRELJME_IS_ANDROID AND
	SQUIRRELJME_IS_UNIX AND SQUIRRELJME_IS_DEBUG)
	squirreljme_include("valgrind.cmake")
endif()

### These need to be before fixes.cmake! ###
# Dynamic linker
squirreljme_include("find-dl.cmake")

# Threading and atomics
squirreljme_include("find-threads.cmake")
############################################

# Fixes and otherwise for compatibility
squirreljme_include("fixes.cmake")

# Common find support
squirreljme_include("find.cmake")

# Win32 Options
squirreljme_include("win32.cmake")

# Apple Options
squirreljme_include("apple.cmake")

# Hardening options
squirreljme_include("hardening.cmake")

# Is the system JNI available?
squirreljme_include("find-jni.cmake")

# Doxygen
squirreljme_include("doxygen.cmake")

# To Emily and Near...
squirreljme_include("i-miss-you.cmake")

# Settings that adjust the build completely
squirreljme_include("environment-options.cmake")

# Everything is C99
set(CMAKE_C_STANDARD 99)
set(CMAKE_C_STANDARD_REQUIRED True)

# Optimization for size, since embedded, if not debugging
if(SQUIRRELJME_IS_RELEASE)
	if(SQUIRRELJME_IS_GCC)
		add_compile_options("-Os")
	elseif(MSVC OR
		CMAKE_C_COMPILER_ID STREQUAL "MSVC" OR
		CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
		add_compile_options("/Os")
	endif()
elseif(SQUIRRELJME_IS_DEBUG)
	if(MSVC OR
		CMAKE_C_COMPILER_ID STREQUAL "MSVC" OR
		CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
		add_compile_options("/Zi")
		add_link_options("/DEBUG")
	endif()
endif()

# Assets and otherwise, such as icons, etc.
squirreljme_include("assets.cmake")

# Multi-library declarations
squirreljme_include("multilib.cmake")

# Distribution
squirreljme_include("distrib.cmake")

# Visual studio does not like a bunch of standard C functions, even those
# that take sizes...
if(MSVC)
	add_compile_definitions(_CRT_SECURE_NO_WARNINGS=1)
endif()

# Declare Version
add_compile_definitions(
	SQUIRRELJME_VERSION="${SQUIRRELJME_VERSION}")
add_compile_definitions(
	SQUIRRELJME_VERSION_BUILD="${SQUIRRELJME_VERSION_BUILD}")
add_compile_definitions(
	SQUIRRELJME_VERSION_STABILITY="${SQUIRRELJME_VERSION_STABILITY}")
add_compile_definitions(
	SQUIRRELJME_VERSION_ID="${SQUIRRELJME_VERSION_ID}")

# Trim is the version without quotes
add_compile_definitions(
	SQUIRRELJME_VERSION_TRIM=${SQUIRRELJME_VERSION})

# Debugging?
if(SQUIRRELJME_IS_DEBUG)
	if(SQUIRRELJME_ENABLE_TESTING)
		add_compile_definitions(SJME_CONFIG_UNIT_TEST=1)
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
if(SQUIRRELJME_ENABLE_TESTING)
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
if(SQUIRRELJME_ENABLE_TESTING)
	add_subdirectory(tests)
else()
	message(WARNING "Testing was disabled "
		"(${SQUIRRELJME_ENABLE_TESTING})...")
endif()

# Front ends
add_subdirectory(frontend)

# Directory where the ROM compiled sources exist
add_subdirectory(rom)

# General applications
add_subdirectory(apps)

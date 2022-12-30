# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Macro for include chunks
	# Non-optional variant
macro(squirreljme_include what)
	include("${CMAKE_SOURCE_DIR}/cmake/${what}"
		NO_POLICY_SCOPE)
endmacro()

	# Optional variant
macro(squirreljme_include_optional what)
	include("${CMAKE_SOURCE_DIR}/cmake/${what}"
		OPTIONAL NO_POLICY_SCOPE)
endmacro()

# Needed for some ancient RetroArch toolchains
if(${CMAKE_VERSION} VERSION_LESS "3.13")
	message("Ancient CMake has been detected (${CMAKE_VERSION})")
	cmake_minimum_required(VERSION 3.7)

# Needs to be this or newer
else()
	message("Detected CMake (${CMAKE_VERSION})")
	cmake_minimum_required(VERSION 3.13)
endif()

# Load version number
file(STRINGS "${CMAKE_CURRENT_SOURCE_DIR}/squirreljme-version"
	SQUIRRELJME_VERSION LIMIT_COUNT 1)
message("Building for SquirrelJME ${SQUIRRELJME_VERSION}!")

# Make Windows compatible version
set(SQUIRRELJME_VERSION_WINDOWS "${SQUIRRELJME_VERSION}.0")
string(REGEX REPLACE "\\." "," SQUIRRELJME_VERSION_WINDOWS_RC
	"${SQUIRRELJME_VERSION_WINDOWS}")

# Put down the configure time
string(TIMESTAMP SQUIRRELJME_VERSION_ID_TIME "%Y-%m-%dT%H:%M:%SZ" UTC)
message("Configure time is ${SQUIRRELJME_VERSION_ID_TIME}.")

# Load Fossil manifest, if possible
if(EXISTS "${CMAKE_CURRENT_SOURCE_DIR}/../manifest.uuid" AND
	NOT IS_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/../manifest.uuid")
	file(STRINGS "${CMAKE_CURRENT_SOURCE_DIR}/../manifest.uuid"
		SQUIRRELJME_VERSION_ID_FOSSIL LIMIT_COUNT 1)
	set(SQUIRRELJME_VERSION_ID "fossil:${SQUIRRELJME_VERSION_ID_FOSSIL}")
else()
	# Try to get version from Git Commit
	execute_process(COMMAND git rev-parse HEAD
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		ERROR_QUIET
		RESULT_VARIABLE SQUIRRELJME_VERSION_ID_GIT_CODE
		OUTPUT_VARIABLE SQUIRRELJME_VERSION_ID_GIT
		ERROR_VARIABLE SQUIRRELJME_VERSION_GIT_ERROR
		OUTPUT_STRIP_TRAILING_WHITESPACE)

	if("${SQUIRRELJME_VERSION_ID_GIT_CODE}" EQUAL 0)
		set(SQUIRRELJME_VERSION_ID "git:${SQUIRRELJME_VERSION_ID_GIT}")
	else()
		# Otherwise use a basic timestamp
		set(SQUIRRELJME_VERSION_ID "unknown:${SQUIRRELJME_VERSION_ID_TIME}")
	endif()
endif()

# Show ID version
message("Version ID: ${SQUIRRELJME_VERSION_ID}")

# Define the project and the versioning info
if(${CMAKE_VERSION} VERSION_LESS "3.12")
	project(SquirrelJME
		VERSION ${SQUIRRELJME_VERSION}
		LANGUAGES C )#CXX)
else()
	project(SquirrelJME
		VERSION ${SQUIRRELJME_VERSION}
		DESCRIPTION "SquirrelJME is a Java ME 8 Virtual Machine for embedded and Internet of Things devices."
		HOMEPAGE_URL https://squirreljme.cc/
		LANGUAGES C )#CXX)
endif()

# To Emily and Near...
message("******************************")
message("To my close friends who are not here today, your friendships were")
message("very important to me. I hope that you are both resting in piece:")
message("  * Emily, developer of Mu. (1998-2020)")
message("  * Near, developer of Higan and Ares. (1983-2021)")
message(" -- Your friend, Stephanie")
message("******************************")

# After project for both system and processor
squirreljme_include_optional(
	"after-project.${CMAKE_SYSTEM_NAME}.cmake")
squirreljme_include_optional(
	"after-project.${CMAKE_SYSTEM_NAME}.${CMAKE_SYSTEM_PROCESSOR}.cmake")

# Mac OS Multi Architecture: PowerPC, x86, x86_64, and M1
# Can be overridden, so only auto-detect here if possible
if(APPLE AND "${CMAKE_OSX_ARCHITECTURES}" STREQUAL "")
	squirreljme_include("apple-arch-detect.cmake")
endif()

# For Emscripten generate an HTML
if(EMSCRIPTEN)
	message("Detected Emscripten...")

	set(CMAKE_EXECUTABLE_SUFFIX ".html")
endif()

# Ancient legacy system?
if(PALMOS)
	set(SQUIRRELJME_ANCIENT TRUE)

	# You are doing whatever
	message("SquirrelJME is building for an ancient legacy system!")
	message("Not supported, compile and run at your own risk!")

	# Debugging ancient stuff is made easier this way
	set(CMAKE_VERBOSE_MAKEFILE TRUE)
endif()

# Default on for -fPIC
if(NOT DEFINED SQUIRRELJME_FPIC_MODE)
	message("Defaulting to ON for -fPIC.")
	set(SQUIRRELJME_FPIC_MODE ON)
endif()

# Standard C and C++ versions
set(CMAKE_C_STANDARD 99)
set(CMAKE_C_STANDARD_REQUIRED True)
#set(CMAKE_CXX_STANDARD 11)
#set(CMAKE_CXX_STANDARD_REQUIRED True)

# Does the static ROM file exist? If it does we can pack it into the binary
option(SQUIRRELJME_USE_BUILTIN "Use Built-In ROM (If Detected)" ON)

set(SQUIRRELJME_BUILTIN_TEST_CHECK "${PROJECT_SOURCE_DIR}/build/builtinTest.c")
set(SQUIRRELJME_BUILTIN_FILE_CHECK "${PROJECT_SOURCE_DIR}/build/builtin.c")
	# Testing ROM
if(SQUIRRELJME_USE_BUILTIN AND EXISTS "${SQUIRRELJME_BUILTIN_TEST_CHECK}")
	message("SquirrelJME: Including Built-In ROM (Testing).")

	set(SQUIRRELJME_HAS_BUILTIN ON)
	set(SQUIRRELJME_BUILTIN_FILE "${SQUIRRELJME_BUILTIN_TEST_CHECK}")

	# Release ROM
elseif(SQUIRRELJME_USE_BUILTIN AND EXISTS "${SQUIRRELJME_BUILTIN_FILE_CHECK}")
	message("SquirrelJME: Including Built-In ROM.")

	set(SQUIRRELJME_HAS_BUILTIN ON)
	set(SQUIRRELJME_BUILTIN_FILE "${SQUIRRELJME_BUILTIN_FILE_CHECK}")
else()
	if(EXISTS "${SQUIRRELJME_BUILTIN_FILE_CHECK}")
		message("SquirrelJME: Built-In ROM Available, but disabled.")
	else()
		message("SquirrelJME: No Built-In ROM Available.")
	endif()

	set(SQUIRRELJME_HAS_BUILTIN OFF)
	set(SQUIRRELJME_BUILTIN_FILE)
endif()

# This is not supported with older CMake versions
if(${CMAKE_VERSION} VERSION_LESS "3.12")
	function(add_compile_definitions ARG_DEFINITION)
		add_definitions(-D${ARG_DEFINITION})
	endfunction()
endif()

# Does the static ROM file exist? If it does we can pack it into the binary
if(SQUIRRELJME_HAS_BUILTIN)
	message("Including built-in ROM!")
	add_compile_definitions(SQUIRRELJME_HAS_BUILTIN=1)
endif()

# Use specific threading libraries?
if(NOT CMAKE_SYSTEM_NAME STREQUAL "ieee1275")
	# For compatibility for Windows, do not use pthreads even if available
	find_package(Threads)
	if(WIN32)
		add_compile_definitions(SQUIRRELJME_THREADS=1)
		add_compile_definitions(SQUIRRELJME_THREADS_WIN32=1)
	elseif(Threads_FOUND)
		if(CMAKE_USE_PTHREADS_INIT)
			add_compile_definitions(SQUIRRELJME_THREADS=1)
			add_compile_definitions(SQUIRRELJME_THREADS_PTHREAD=1)
		endif()
	endif()
endif()

# Enable extra warnings since they are useful
if(MSVC)
	string(REGEX REPLACE "/W[3|4]" "/W4" CMAKE_C_FLAGS "${CMAKE_C_FLAGS}")
	string(REGEX REPLACE "/W[3|4]" "/W4" CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
elseif(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
	if(NOT CMAKE_SYSTEM_NAME STREQUAL "ieee1275")
		add_compile_options(-Wall -Wextra -pedantic -pedantic-errors)
	endif()
endif()

# Versioning information
add_compile_definitions(SQUIRRELJME_VERSION=${SQUIRRELJME_VERSION})
add_compile_definitions(SQUIRRELJME_VERSION_ID=${SQUIRRELJME_VERSION_ID})
add_compile_definitions(SQUIRRELJME_VERSION_ID_TIME=${SQUIRRELJME_VERSION_ID_TIME})
add_compile_definitions(SQUIRRELJME_VERSION_WINDOWS=${SQUIRRELJME_VERSION_WINDOWS})
add_compile_definitions(SQUIRRELJME_VERSION_WINDOWS_RC=${SQUIRRELJME_VERSION_WINDOWS_RC})

# Enable testing by default, if not set
if(NOT DEFINED SQUIRRELJME_ALLOW_TESTING)
	message("Unit Tests: Permitted")
	set(SQUIRRELJME_ALLOW_TESTING YES)
endif()

# Enable support for testing, this is needed here otherwise testing will not
# work at all! Major headache this has caused...
# From: https://cmake.org/cmake/help/v3.13/command/enable_testing.html
# > Note that ctest expects to find a test file in the build directory root.
# > Therefore, this command should be in the source directory root.
if(SQUIRRELJME_ALLOW_TESTING)
	enable_testing()
endif()

# Helpful functions and macros
squirreljme_include("macros-and-functions.cmake")

# Bundle our own C library?
if(SQUIRRELJME_BUNDLED_STDC)
	add_compile_definitions(SQUIRRELJME_BUNDLED_STDC=1)
	include_directories(
		"${PROJECT_SOURCE_DIR}/include/libstdc")
endif()

# Include directories for the project
add_subdirectory(src)

# Testing
if(SQUIRRELJME_ALLOW_TESTING)
	add_subdirectory(tests)
endif()

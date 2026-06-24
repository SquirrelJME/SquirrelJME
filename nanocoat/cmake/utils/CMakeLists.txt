# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various utilities

# Use the best version possible
# Otherwise, fallback to the lowest viable version
message(STATUS "Detected CMake (${CMAKE_VERSION})")
if(CMAKE_VERSION VERSION_GREATER_EQUAL 3.13)
	cmake_minimum_required(VERSION 3.13..3.31)
else()
	cmake_minimum_required(VERSION 3.0)
endif()

# Debug
message(STATUS "Util generator is ${CMAKE_GENERATOR}...")

# Declare Project
project(CMakeUtils)

# Always output binaries into the binary directory because we do not want
# to bother with how things should be found...
message(STATUS "Setting outputs...")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY
	"${CMAKE_BINARY_DIR}")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_DEBUG
	"${CMAKE_BINARY_DIR}")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_RELEASE
	"${CMAKE_BINARY_DIR}")

# Dump executable suffix, since target could be different from host
message(STATUS "Writing suffixes...")
file(WRITE "${CMAKE_BINARY_DIR}/suffix"
	"${CMAKE_EXECUTABLE_SUFFIX_C}")
file(WRITE "${CMAKE_BINARY_DIR}/dylibprefix"
	"${CMAKE_SHARED_LIBRARY_PREFIX}")
file(WRITE "${CMAKE_BINARY_DIR}/dylibsuffix"
	"${CMAKE_SHARED_LIBRARY_SUFFIX}")

message(STATUS "Including patches...")
include_directories("${CMAKE_SOURCE_DIR}/../../include/")
include("${CMAKE_SOURCE_DIR}/../cmake-patch.cmake"
	NO_POLICY_SCOPE)
#include("${CMAKE_SOURCE_DIR}/../fixes.cmake"
#	NO_POLICY_SCOPE)

# Ignore C11 threading
add_compile_definitions(SJME_CONFIG_HAS_NO_C11_THREADS=1)

# Hex decoding to binary
message(STATUS "Adding decode...")
add_executable(decode
	decode.c)

# Simple test binary
message(STATUS "Adding simple...")
add_executable(simple
	simple.c)

# Turn binary file into source code
message(STATUS "Adding sourceize...")
add_executable(sourceize
	sourceize.c)

# Print compiler platform
message(STATUS "Adding platform...")
add_executable(platform
	platform.c)

# JVM and JAWT stubs
message(STATUS "Adding jvm...")
add_library(jvm SHARED
	stub_jvm.c)

message(STATUS "Adding jawt...")
add_library(jawt SHARED
	stub_jawt.c)

if (MSVC)
	message(STATUS "Setting MSVC link options...")

	target_link_options(jvm PRIVATE
		"/IMPLIB:${CMAKE_BINARY_DIR}/jvm.lib")
	target_link_options(jawt PRIVATE
		"/IMPLIB:${CMAKE_BINARY_DIR}/jawt.lib")
endif()

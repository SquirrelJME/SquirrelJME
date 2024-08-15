# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Various utilities

# Just allow a very early 3.0 release so all utilities work regardless
cmake_minimum_required(VERSION 3.0)

message(STATUS "Util generator is ${CMAKE_GENERATOR}...")

# Declare Project
project(CMakeUtils)

# Always output binaries into the binary directory because we do not want
# to bother with how things should be found...
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY
	"${CMAKE_BINARY_DIR}")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_DEBUG
	"${CMAKE_BINARY_DIR}")
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY_RELEASE
	"${CMAKE_BINARY_DIR}")

# Dump executable suffix, since target could be different from host
file(WRITE "${CMAKE_BINARY_DIR}/suffix"
	"${CMAKE_EXECUTABLE_SUFFIX_C}")
file(WRITE "${CMAKE_BINARY_DIR}/dylibprefix"
	"${CMAKE_SHARED_LIBRARY_PREFIX}")
file(WRITE "${CMAKE_BINARY_DIR}/dylibsuffix"
	"${CMAKE_SHARED_LIBRARY_SUFFIX}")

# Hex decoding to binary
add_executable(decode
	decode.c)

# Simple test binary
add_executable(simple
	simple.c)

# Turn binary file into source code
add_executable(sourceize
	sourceize.c)

# Print compiler platform
add_executable(platform
	platform.c)

# JVM and JAWT stubs
add_library(jvm SHARED
	stub_jvm.c)

add_library(jawt SHARED
	stub_jawt.c)

if (MSVC)
	target_link_options(jvm PRIVATE
		"/IMPLIB:${CMAKE_BINARY_DIR}/jvm.lib")

	target_link_options(jawt PRIVATE
		"/IMPLIB:${CMAKE_BINARY_DIR}/jawt.lib")
endif()
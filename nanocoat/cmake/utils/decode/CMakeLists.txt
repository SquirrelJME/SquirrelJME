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
	cmake_minimum_required(VERSION 3.1)
endif()

# Debug
message(STATUS "Util generator is ${CMAKE_GENERATOR}...")

# Declare Project
project(Decode)

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

# Hex decoding to binary
message(STATUS "Adding decode...")
add_executable(decode
	decode.c)

target_include_directories(decode PUBLIC
	"${CMAKE_SOURCE_DIR}/..")

target_compile_definitions(decode PUBLIC
	SJME_CONFIG_HAS_NO_C11_THREADS=1)

if(MSVC)
	target_compile_options(decode PUBLIC
		"/MT")
elseif(CMAKE_COMPILER_IS_GNUCC OR
	CMAKE_COMPILER_IS_GNUCXX OR
	CMAKE_C_COMPILER_ID STREQUAL "GNU" OR
	CMAKE_CXX_COMPILER_ID STREQUAL "Clang")
	target_link_options(decode PUBLIC
		"-static")
endif()

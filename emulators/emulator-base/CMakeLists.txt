# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake Helper for project

cmake_minimum_required(VERSION 3.5)

# Declare project
project(libEmulatorBase)

# Use standard JNI search
find_package(JNI QUIET)

# If JNI was not found, use a local copy as a fallback
if(NOT JNI_FOUND)
	# Where are the headers?
	set(JNI_INCLUDE_DIRS
		"src/main/headers/jni")

	# Set as found
	set(JNI_FOUND ON)
endif()

# Declare library
add_library(libEmulatorBase SHARED
	src/main/c/mle_debug.c
    src/main/c/mle_form.c
    src/main/c/mle_jar.c
    src/main/c/mle_math.c
    src/main/c/mle_midi.c
    src/main/c/mle_nativearchive.c
    src/main/c/mle_object.c
    src/main/c/mle_pencil.c
    src/main/c/mle_reflection.c
    src/main/c/mle_runtime.c
    src/main/c/mle_task.c
    src/main/c/mle_terminal.c
    src/main/c/mle_thread.c
    src/main/c/mle_type.c
    src/main/c/nativebinding.c
    src/main/c/utils.c)

# Includes for the build
target_include_directories(libEmulatorBase PUBLIC
	"src/main/headers"
	${JNI_INCLUDE_DIRS})

# It is easier to find this when it is in the build root
set_target_properties(libEmulatorBase PROPERTIES
	RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	ARCHIVE_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}")

# Use a more basic name for the library
set_target_properties(libEmulatorBase PROPERTIES
	RUNTIME_OUTPUT_NAME
	"emulator-base"
	LIBRARY_OUTPUT_NAME
	"emulator-base"
	ARCHIVE_OUTPUT_NAME
	"emulator-base")

# Always make this position independent
set_property(TARGET libEmulatorBase
	PROPERTY POSITION_INDEPENDENT_CODE ON)

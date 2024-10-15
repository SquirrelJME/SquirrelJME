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
project(libEmulatorBase
    LANGUAGES C)

# Support bases
include("${CMAKE_SOURCE_DIR}/../../nanocoat/cmake/pre-fixes.cmake"
    NO_POLICY_SCOPE)
include("${CMAKE_SOURCE_DIR}/../../nanocoat/cmake/utils.cmake"
    NO_POLICY_SCOPE)
include("${CMAKE_SOURCE_DIR}/../../nanocoat/cmake/fixes.cmake"
    NO_POLICY_SCOPE)
include("${CMAKE_SOURCE_DIR}/../../nanocoat/cmake/jni.cmake"
    NO_POLICY_SCOPE)
include("${CMAKE_SOURCE_DIR}/../../nanocoat/cmake/threads.cmake"
    NO_POLICY_SCOPE)

# Where is NanoCoat?
get_filename_component(nanocoatIncludePath
	"${CMAKE_SOURCE_DIR}/../../nanocoat/include" ABSOLUTE)
message(STATUS "NanoCoat include is: ${nanocoatIncludePath}")

# Declare library
add_library(libEmulatorBase SHARED
	c/jniHelper.c
	c/mle_debug.c
	c/mle_jar.c
	c/mle_math.c
	c/mle_midi.c
	c/mle_nativearchive.c
	c/mle_object.c
	c/mle_pencil.c
    c/mle_pencilfont.c
	c/mle_reflection.c
	c/mle_runtime.c
	c/mle_scritchui.c
	c/mle_scritchuibaseobject.c
	c/mle_scritchuidylib.c
	c/mle_task.c
	c/mle_terminal.c
	c/mle_thread.c
	c/mle_type.c
	c/nativebinding.c
	c/utils.c)

# Includes for the build
target_include_directories(libEmulatorBase PUBLIC
	"headers"
	"${nanocoatIncludePath}"
	${JNI_INCLUDE_DIRS}
	${JAVA_INCLUDE_PATH}
	${JAVA_INCLUDE_PATH2})

# Libraries for the build
get_filename_component(nanocoatLibPath
	"${CMAKE_SOURCE_DIR}/../emulator-base/build/cmake-libsNativeNanoCoat"
	ABSOLUTE)
message(STATUS "NanoCoat lib is: ${nanocoatLibPath}")
target_link_directories(libEmulatorBase PUBLIC
	"${nanocoatLibPath}")
target_link_libraries(libEmulatorBase PUBLIC
	"BaseStatic" "${JAVA_JVM_LIBRARY}")

# It is easier to find this when it is in the build root
set_target_properties(libEmulatorBase PROPERTIES
	RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	ARCHIVE_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}")

foreach(outputConfig ${CMAKE_CONFIGURATION_TYPES})
	string(TOUPPER "${outputConfig}" outputConfig)

	set_target_properties(libEmulatorBase PROPERTIES
		RUNTIME_OUTPUT_DIRECTORY_${outputConfig} "${CMAKE_BINARY_DIR}"
		LIBRARY_OUTPUT_DIRECTORY_${outputConfig} "${CMAKE_BINARY_DIR}"
		ARCHIVE_OUTPUT_DIRECTORY_${outputConfig} "${CMAKE_BINARY_DIR}")
endforeach()

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

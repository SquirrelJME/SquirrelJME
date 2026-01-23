# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake Helper for project

cmake_minimum_required(VERSION 3.5)

# This is definitely needed
include(ExternalProject)

# Declare project
project(libEmulatorBase
    LANGUAGES C)

# Specify NanoCoat directory for includes
set(SQUIRRELJME_ROOT_DIR "${CMAKE_SOURCE_DIR}/../..")
set(SQUIRRELJME_NANOCOAT_DIR "${SQUIRRELJME_ROOT_DIR}/nanocoat")
include("${SQUIRRELJME_NANOCOAT_DIR}/cmake/easier-includes.cmake")

# Support bases
squirreljme_include_nanocoat("cmake-patch.cmake")
squirreljme_include_nanocoat("pre-fixes.cmake")
squirreljme_include_nanocoat("system-map.cmake")
squirreljme_include_nanocoat("utils.cmake")
squirreljme_include_nanocoat("fixes.cmake")
squirreljme_include_nanocoat("find-jni.cmake")
squirreljme_include_nanocoat("find-threads.cmake")

# Add NanoCoat as an external project
ExternalProject_Add(NanoCoat
	SOURCE_DIR "${SQUIRRELJME_NANOCOAT_DIR}"
	STEP_TARGETS build)

# Include the NanoCoat target for building
message(STATUS "Build: ${NanoCoat_BUILD}")

# Where is NanoCoat?
get_filename_component(nanocoatIncludePath
	"${SQUIRRELJME_NANOCOAT_DIR}/include" ABSOLUTE)
message(STATUS "NanoCoat include is: ${nanocoatIncludePath}")

# Declare library
add_library(libEmulatorBase SHARED
	c/jniHelper.c
	c/mle_audiostream.c
	c/mle_bucket.c
	c/mle_debug.c
	c/mle_jar.c
	c/mle_math.c
	c/mle_mathaccel.c
	c/mle_midi.c
	c/mle_nativearchive.c
	c/mle_object.c
	c/mle_pencil.c
    c/mle_pencilfont.c
	c/mle_reflection.c
	c/mle_runtime.c
	c/mle_scritchui.c
	c/mle_scritchuibaseobject.c
	c/mle_scritchuicallback.c
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
	"${SQUIRRELJME_JAVA_JNI_INCLUDE}"
	"${SQUIRRELJME_JAVA_JVM_INCLUDE}"
	"${SQUIRRELJME_EMULATOR_BASE_IMPORT_DIR}")

# Libraries for the build
get_filename_component(nanocoatLibPath
	"${CMAKE_SOURCE_DIR}/../emulator-base/build/cmake-libNativeUI"
	ABSOLUTE)
message(STATUS "NanoCoat lib is: ${nanocoatLibPath}")
target_link_directories(libEmulatorBase PUBLIC
	"${nanocoatLibPath}"
	"${SQUIRRELJME_EMULATOR_BASE_IMPORT_DIR}")
target_link_libraries(libEmulatorBase PUBLIC
	"BaseStatic" "jvm")

# It is easier to find this when it is in the build root
squirreljme_target_binary_output(libEmulatorBase "${CMAKE_BINARY_DIR}")

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

# Needed for all platforms
squirreljme_target_link_libraries_required(libEmulatorBase)

# Library Exports
squirreljme_target_shared_library_exports(libEmulatorBase)


# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake Helper for project

set(CMAKE_VERBOSE_MAKEFILE YES)

cmake_minimum_required(VERSION 3.5)

# Declare project
project(libEmulatorBase
    LANGUAGES C)

# Specify NanoCoat directory for includes
set(SQUIRRELJME_ROOT_DIR "${CMAKE_SOURCE_DIR}/../..")
set(SQUIRRELJME_NANOCOAT_SOURCE_DIR "${SQUIRRELJME_ROOT_DIR}/nanocoat")
include("${SQUIRRELJME_NANOCOAT_SOURCE_DIR}/cmake/easier-includes.cmake")

# Configure parent NanoCoat
set(SQUIRRELJME_NANOCOAT_BINARY_DIR "${CMAKE_BINARY_DIR}/nanocoat")
execute_process(COMMAND "${CMAKE_COMMAND}"
	"-DCMAKE_BUILD_TYPE=${CMAKE_BUILD_TYPE}"
	"-DCMAKE_C_COMPILER=${CMAKE_C_COMPILER}"
	"-DCMAKE_C_FLAGS=${CMAKE_C_FLAGS}"
	"-DCMAKE_ASM_COMPILER=${CMAKE_ASM_COMPILER}"
	"-DCMAKE_ASM_FLAGS=${CMAKE_ASM_FLAGS}"
	"-DCMAKE_AR=${CMAKE_AR}"
	"-DCMAKE_C_LINK_EXECUTABLE=${CMAKE_C_LINK_EXECUTABLE}"
	"-DCMAKE_LINK_LIBRARY_FLAG=${CMAKE_LINK_LIBRARY_FLAG}"
	"-DCMAKE_EXE_LINKER_FLAGS=${CMAKE_EXE_LINKER_FLAGS}"
	"-DCMAKE_SHARED_LINKER_FLAGS=${CMAKE_SHARED_LINKER_FLAGS}"
	"-DCMAKE_STATIC_LINKER_FLAGS=${CMAKE_STATIC_LINKER_FLAGS}"
	"-G" "${CMAKE_GENERATOR}"
	"-T" "${CMAKE_GENERATOR_TOOLSET}"
	"-A" "${CMAKE_GENERATOR_PLATFORM}"
	"-S" "${SQUIRRELJME_NANOCOAT_SOURCE_DIR}"
	"-B" "${SQUIRRELJME_NANOCOAT_BINARY_DIR}"
	RESULT_VARIABLE nanoCoatConfigure)
if(NOT nanoCoatConfigure EQUAL "0")
	message(FATAL_ERROR "Could not configure NanoCoat!")
endif()

# Support bases
squirreljme_include_nanocoat("cmake-patch.cmake")
squirreljme_include_nanocoat("pre-fixes.cmake")
squirreljme_include_nanocoat("system-map.cmake")
squirreljme_include_nanocoat("fixes.cmake")
squirreljme_include_nanocoat("multilib.cmake")

# Import the JVM library that NanoCoat found
# We also need to build the SquirrelJME base library
include("${SQUIRRELJME_NANOCOAT_BINARY_DIR}/BaseStatic.cmake")
include("${SQUIRRELJME_NANOCOAT_BINARY_DIR}/libJvmDyLib.cmake")
include("${SQUIRRELJME_NANOCOAT_BINARY_DIR}/discoveredJvm.cmake")

execute_process(COMMAND "${CMAKE_COMMAND}"
	"--build" "${SQUIRRELJME_NANOCOAT_BINARY_DIR}"
	"--target" "BaseStatic" "libJvmDyLib"
	RESULT_VARIABLE baseStaticBuild)
if(NOT baseStaticBuild EQUAL "0")
	message(FATAL_ERROR "Could not build SquirrelJME base library!")
endif()

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
target_include_directories(libEmulatorBase PRIVATE
	"${CMAKE_SOURCE_DIR}/headers"
	"${SQUIRRELJME_NANOCOAT_SOURCE_DIR}/include"
	"$<TARGET_PROPERTY:BaseStatic,INTERFACE_INCLUDE_DIRECTORIES>"
	"$<TARGET_PROPERTY:discoveredJvm,INTERFACE_INCLUDE_DIRECTORIES>")

# Libraries for the build
target_link_directories(libEmulatorBase PRIVATE
	"$<TARGET_PROPERTY:BaseStatic,INTERFACE_LINK_DIRECTORIES>"
	"$<TARGET_PROPERTY:discoveredJvm,INTERFACE_LINK_DIRECTORIES>")
target_link_libraries(libEmulatorBase PUBLIC
	"$<TARGET_PROPERTY:BaseStatic,IMPORTED_LOCATION>"
	"$<TARGET_PROPERTY:BaseStatic,IMPORTED_LOCATION_$<UPPER_CASE:$<CONFIG>>>"
	"$<TARGET_PROPERTY:BaseStatic,IMPORTED_LOCATION_NOCONFIG>"
	"$<TARGET_PROPERTY:discoveredJvm,INTERFACE_LINK_LIBRARIES>")

# It is easier to find this when it is in the build root
squirreljme_target_binary_output(libEmulatorBase "${CMAKE_BINARY_DIR}")

# Use a more basic name for the library
squirreljme_target_binary_name(libEmulatorBase "emulator-base")

# Always make this position independent
squirreljme_always_fpic(libEmulatorBase)

# Needed for all platforms
squirreljme_target_link_libraries_required(libEmulatorBase)

# Library Exports
squirreljme_target_shared_library_exports(libEmulatorBase)

# Rule to bundle all of the natives together
set(nativesPath
	"${CMAKE_BINARY_DIR}/natives-${SQUIRRELJME_SYSTEM}-${SQUIRRELJME_ARCH}.zip")
add_custom_target(bundleNatives
	COMMAND "${CMAKE_COMMAND}" "-E"
		"tar" "c" "${nativesPath}" "--format=zip" "--"
		"$<TARGET_FILE:libEmulatorBase>"
		"$<TARGET_PROPERTY:BaseStatic,IMPORTED_LOCATION>"
		"$<TARGET_PROPERTY:BaseStatic,IMPORTED_LOCATION_$<UPPER_CASE:$<CONFIG>>>"
		"$<TARGET_PROPERTY:BaseStatic,IMPORTED_LOCATION_NOCONFIG>"
	DEPENDS libEmulatorBase
	SOURCES "$<TARGET_FILE:libEmulatorBase>"
	COMMAND_EXPAND_LISTS)

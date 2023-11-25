# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Defines the base project and the versioning info

# Echo commands accordingly
set(CMAKE_EXECUTE_PROCESS_COMMAND_ECHO STDERR)

# The directory where the utilities should exist
get_filename_component(SQUIRRELJME_UTIL_SOURCE_DIR
	"${CMAKE_SOURCE_DIR}/cmake/utils" ABSOLUTE)
get_filename_component(SQUIRRELJME_UTIL_DIR
	"${CMAKE_BINARY_DIR}/utils" ABSOLUTE)

# Only run this if the directory does not exist, because there might be a
# cache from a previous run?
if(NOT EXISTS "${SQUIRRELJME_UTIL_DIR}")
	# Make sure the directory exists
	file(MAKE_DIRECTORY "${SQUIRRELJME_UTIL_DIR}")

	# Note
	message("Bootstrapping utils into "
		"${SQUIRRELJME_UTIL_DIR}...")

	# Run nested CMake to build the utilities
	if(${CMAKE_VERSION} VERSION_GREATER_EQUAL "3.13")
		# CMake 3.13 added the -S and -B switches
		execute_process(
			COMMAND "${CMAKE_COMMAND}"
				"-E" "env"
				"--unset=CMAKE_TOOLCHAIN_FILE"
				"--unset=CMAKE_SOURCE_ROOT"
				"--unset=CMAKE_FRAMEWORK_PATH"
				"--unset=CMAKE_INCLUDE_PATH"
				"--unset=CMAKE_LIBRARY_PATH"
				"--unset=CMAKE_PROGRAM_PATH"
				"--unset=CMAKE_BUILD_TYPE"
				"--unset=CMAKE_GENERATOR"
				"--unset=CMAKE_GENERATOR_INSTANCE"
				"--unset=CMAKE_GENERATOR_PLATFORM"
				"--unset=CMAKE_GENERATOR_TOOLSET"
				"--unset=CMAKE_C_COMPILER_LAUNCHER"
				"--unset=CMAKE_C_LINKER_LAUNCHER"
				"${CMAKE_COMMAND}"
				"-DCMAKE_BUILD_TYPE=Debug"
				"-S" "${SQUIRRELJME_UTIL_SOURCE_DIR}"
				"-B" "${SQUIRRELJME_UTIL_DIR}"
			RESULT_VARIABLE cmakeUtilBuildResult)
	else()
		# Need to initialize the project the old way, by just being in
		# a different working directory and referring to the source
		execute_process(
			COMMAND "${CMAKE_COMMAND}"
				"-E" "env"
				"--unset=CMAKE_TOOLCHAIN_FILE"
				"--unset=CMAKE_SOURCE_ROOT"
				"--unset=CMAKE_FRAMEWORK_PATH"
				"--unset=CMAKE_INCLUDE_PATH"
				"--unset=CMAKE_LIBRARY_PATH"
				"--unset=CMAKE_PROGRAM_PATH"
				"--unset=CMAKE_BUILD_TYPE"
				"--unset=CMAKE_GENERATOR"
				"--unset=CMAKE_GENERATOR_INSTANCE"
				"--unset=CMAKE_GENERATOR_PLATFORM"
				"--unset=CMAKE_GENERATOR_TOOLSET"
				"--unset=CMAKE_C_COMPILER_LAUNCHER"
				"--unset=CMAKE_C_LINKER_LAUNCHER"
				"${CMAKE_COMMAND}"
				"-DCMAKE_BUILD_TYPE=Debug"
				"${SQUIRRELJME_UTIL_SOURCE_DIR}"
			WORKING_DIRECTORY "${SQUIRRELJME_UTIL_DIR}"
			RESULT_VARIABLE cmakeUtilBuildResult)
	endif()

	# Did this fail?
	if(cmakeUtilBuildResult)
		message(FATAL_ERROR
			"Cannot configure utils: ${cmakeUtilBuildResult}...")
	endif()
else()
	message("No need to configure utilities, already there...")
endif()

# Build the utilities, just in case it is out of date
message("Building utilities, if out of date...")
execute_process(
	COMMAND "${CMAKE_COMMAND}"
		"--build" "${SQUIRRELJME_UTIL_DIR}"
	RESULT_VARIABLE cmakeUtilBuildResult
	WORKING_DIRECTORY "${SQUIRRELJME_UTIL_DIR}")

# Did this fail?
if(cmakeUtilBuildResult)
	# Ignore for now
	message("Cannot build utils (CMake): "
		"${cmakeUtilBuildResult}...")

	# Try to find a compiler
	find_program(HOST_CC "cc")
	if(NOT HOST_CC)
		find_program(HOST_CC "gcc")
	endif()

	# Fallback to regular make, maybe it will work
	execute_process(
		COMMAND "${CMAKE_COMMAND}"
			"-E" "env"
			"--unset=CC"
			"--unset=CXX"
			"--unset=CPP"
			"--unset=LD"
			"make" "all"
			"OUTPUT_DIR=${SQUIRRELJME_UTIL_DIR}"
			"HOST_EXE_SUFFIX=${SQUIRRELJME_HOST_EXE_SUFFIX}"
		RESULT_VARIABLE makeUtilBuildResult
		WORKING_DIRECTORY "${SQUIRRELJME_UTIL_SOURCE_DIR}")

	# This failed too...
	if(makeUtilBuildResult)
		message(FATAL_ERROR
			"Cannot build utils (Make): "
				"${makeUtilBuildResult}...")
	endif()
endif()

# Determine executable suffix
file(STRINGS "${SQUIRRELJME_UTIL_DIR}/suffix"
	SQUIRRELJME_HOST_EXE_SUFFIX)
message("Host executable suffix is "
	"'${SQUIRRELJME_HOST_EXE_SUFFIX}'.")

# Add macro to determine the name of the utility
macro(squirreljme_util var what)
	set(${var}
		"${SQUIRRELJME_UTIL_DIR}/${what}${SQUIRRELJME_HOST_EXE_SUFFIX}")
endmacro()

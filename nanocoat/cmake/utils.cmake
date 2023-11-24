# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Defines the base project and the versioning info

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

	# Run nested CMake to build the utilities
	message("Bootstrapping utils into "
		"${SQUIRRELJME_UTIL_DIR}...")
	execute_process(
		COMMAND "${CMAKE_COMMAND}"
			"-DCMAKE_BUILD_TYPE=Debug"
			"-B" "${SQUIRRELJME_UTIL_DIR}"
			"-S" "${SQUIRRELJME_UTIL_SOURCE_DIR}"
		RESULT_VARIABLE cmakeUtilBuildResult)

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
	RESULT_VARIABLE cmakeUtilBuildResult)

# Did this fail?
if(cmakeUtilBuildResult)
	message(FATAL_ERROR
		"Cannot build utils: ${cmakeUtilBuildResult}...")
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

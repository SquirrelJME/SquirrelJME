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
get_filename_component(SQUIRRELJME_UTIL_BINARY_DIR
	"${CMAKE_BINARY_DIR}/utils" ABSOLUTE)

# Run nested CMake to build the utilities
message("Bootstrapping utils into "
	"${SQUIRRELJME_UTIL_BINARY_DIR}...")
execute_process(
	COMMAND "${CMAKE_COMMAND}"
		"-DCMAKE_BUILD_TYPE=Debug"
		"-B" "${SQUIRRELJME_UTIL_BINARY_DIR}"
		"-S" "${SQUIRRELJME_UTIL_SOURCE_DIR}"
	RESULT_VARIABLE cmakeUtilBuildResult)

# Did this fail?
if(cmakeUtilBuildResult)
	message(FATAL_ERROR
		"Cannot configure utils: ${cmakeUtilBuildResult}...")
endif()

# Build the utilities
execute_process(
	COMMAND "${CMAKE_COMMAND}"
		"--build" "${SQUIRRELJME_UTIL_BINARY_DIR}"
	RESULT_VARIABLE cmakeUtilBuildResult)

# Did this fail?
if(cmakeUtilBuildResult)
	message(FATAL_ERROR
		"Cannot build utils: ${cmakeUtilBuildResult}...")
endif()

# Add macro to determine the name of the utility
macro(squirreljme_util var what)
	set(${var}
		"${SQUIRRELJME_UTIL_BINARY_DIR}/${what}${CMAKE_EXECUTABLE_SUFFIX_C}")
endmacro()

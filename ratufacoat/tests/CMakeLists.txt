# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Tests for SquirrelJME

# Build executable just for testing
file(GLOB SQUIRRELJME_TEST_SOURCES "test*.c")
add_executable(SquirrelJMETests
    mainEntry.c
	${SQUIRRELJME_TEST_SOURCES})

# Additional includes
target_include_directories(SquirrelJMETests PUBLIC
	"${PROJECT_BINARY_DIR}" "${PROJECT_SOURCE_DIR}/include")

# Link in core SquirrelJME
target_link_libraries(SquirrelJMETests
	SquirrelJMECore)

# Enable support for testing
enable_testing()

# Scan for tests
foreach(SQUIRRELJME_TEST_SOURCE ${SQUIRRELJME_TEST_SOURCES})
	# CMake 3.20 has cmake_path, so we can use that.
	# We need to do this because on Windows, CLion+CMake just gives the full
	# path which we really do not want as it will mess up with tests
	if(${CMAKE_VERSION} VERSION_GREATER_EQUAL 3.20)
		cmake_path(GET "${SQUIRRELJME_TEST_SOURCE}" FILENAME ${SQUIRRELJME_TEST_SOURCE})
	else()
		get_filename_component(SQUIRRELJME_TEST_SOURCE "${SQUIRRELJME_TEST_SOURCE}" NAME)
	endif()

	# Remove the C to just get the base test
	string(REPLACE ".c" "" SQUIRRELJME_TEST_SOURCE ${SQUIRRELJME_TEST_SOURCE})

	# Note it and register it accordingly
	message("Adding test ${SQUIRRELJME_TEST_SOURCE}...")
	add_test(NAME ${SQUIRRELJME_TEST_SOURCE} COMMAND SquirrelJMETests ${SQUIRRELJME_TEST_SOURCE})
endforeach()

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
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_BINARY_DIR}"		# Generated sources!
	)

# Link in core SquirrelJME, make it static so there are no dependencies
target_link_libraries(SquirrelJMETests
	SquirrelJMECore -static)

# Auto-generate tests
configure_file(blank.h.in prototype.h)
configure_file(blank.h.in listing.h)

# Used to wrap for QEMU and otherwise
if(DEFINED SQUIRRELJME_TEST_WRAPPER)
	message("Wrapping tests in ${SQUIRRELJME_TEST_WRAPPER}...")
endif()

# Scan for tests
foreach(SQUIRRELJME_TEST_SOURCE ${SQUIRRELJME_TEST_SOURCES})
	# We need to do this because on Windows, CLion+CMake just gives the full
	# path which we really do not want as it will mess up with tests
	get_filename_component(SQUIRRELJME_TEST_SOURCE "${SQUIRRELJME_TEST_SOURCE}" NAME)

	# Remove the C to just get the base test
	string(REPLACE ".c" "" SQUIRRELJME_TEST_SOURCE ${SQUIRRELJME_TEST_SOURCE})

	# Note it and register it accordingly
	message("Adding test ${SQUIRRELJME_TEST_SOURCE}...")
	if(DEFINED SQUIRRELJME_TEST_WRAPPER)
		add_test(NAME ${SQUIRRELJME_TEST_SOURCE}
			COMMAND "${SQUIRRELJME_TEST_WRAPPER}" "$<TARGET_FILE:SquirrelJMETests>" ${SQUIRRELJME_TEST_SOURCE})
	else()
		add_test(NAME ${SQUIRRELJME_TEST_SOURCE}
			COMMAND SquirrelJMETests ${SQUIRRELJME_TEST_SOURCE})
	endif()

	# Code for skipped tests
	set_property(TEST ${SQUIRRELJME_TEST_SOURCE} PROPERTY SKIP_RETURN_CODE 42)
	set_tests_properties(${SQUIRRELJME_TEST_SOURCE} PROPERTIES SKIP_RETURN_CODE 42)

	# Add test to the generated sources, so that we need not manually store
	# every test we know about
	file(APPEND "${CMAKE_CURRENT_BINARY_DIR}/prototype.h"
		"SJME_TEST_PROTOTYPE(${SQUIRRELJME_TEST_SOURCE});\n")
	file(APPEND "${CMAKE_CURRENT_BINARY_DIR}/listing.h"
		"SJME_TEST(${SQUIRRELJME_TEST_SOURCE}),\n")
endforeach()

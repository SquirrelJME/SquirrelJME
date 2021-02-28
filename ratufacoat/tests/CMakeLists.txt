# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Tests for SquirrelJME

# Enable support for testing
enable_testing()

# Build executable just for testing
file(GLOB SQUIRRELJME_TEST_SOURCES "test*.c")
add_executable(SquirrelJMETests
    mainentry.c
	${SQUIRRELJME_TEST_SOURCES})

# Additional includes
target_include_directories(SquirrelJMETests PUBLIC
	"${PROJECT_BINARY_DIR}" "${PROJECT_SOURCE_DIR}/include")

# Link in core SquirrelJME
target_link_libraries(SquirrelJMETests
	SquirrelJMECore)

# Scan for tests
foreach(SQUIRRELJME_TEST_SOURCE ${SQUIRRELJME_TEST_SOURCES})
	string(REPLACE ".c" "" SQUIRRELJME_TEST_SOURCE ${SQUIRRELJME_TEST_SOURCE})
	add_test(NAME ${SQUIRRELJME_TEST_SOURCE} COMMAND SquirrelJMETests ${SQUIRRELJME_TEST_SOURCE})
endforeach()

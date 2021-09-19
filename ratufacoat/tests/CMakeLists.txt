# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Tests for SquirrelJME

# Build executable just for testing'
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

# Link in core SquirrelJME, it is preferred for it to be static!
# Mac OS does not support static here, it will give an error that says:
# ld: library not found for -lcrt0.o
if(NOT APPLE)
	target_link_libraries(SquirrelJMETests -static
		SquirrelJMECoreLib)
else()
	target_link_libraries(SquirrelJMETests -static
		SquirrelJMECoreLib)
endif()

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

# Go through an source-ize blobs so that they can be included, this can take
# awhile to do especially on slower systems so use a cache to take care of
# it accordingly. Although CMake has its own caching system we kinda just have
# to do this for simplicity purposes. This also really helps when using
# CLion as the immense lag when reloading the project config.
file(GLOB SQUIRRELJME_BLOB_FILES "${CMAKE_CURRENT_SOURCE_DIR}/blobs/*.blob")
foreach(SQUIRRELJME_BLOB_FILE ${SQUIRRELJME_BLOB_FILES})
	# Target blob file
	get_filename_component(BLOB_BASENAME "${SQUIRRELJME_BLOB_FILE}" NAME)
	string(REPLACE ".blob" "" BLOB_BASENAME ${BLOB_BASENAME})
	configure_file(blank.h.in "${BLOB_BASENAME}.h")
	set(BLOB_TARGET_FILE "${CMAKE_CURRENT_BINARY_DIR}/${BLOB_BASENAME}.h")

	# Checksum file for quicker checking
	set(BLOB_CHECKSUM_FILE "${BLOB_TARGET_FILE}.checksum")

	# Determine the checksum of the file, to determine if we can just skip
	# doing this very lengthy process!
	file(SHA1 "${SQUIRRELJME_BLOB_FILE}" BLOB_HASH)
	if(EXISTS "${BLOB_CHECKSUM_FILE}" AND
		EXISTS "${BLOB_TARGET_FILE}")
		# Read the old hash, if it exists
		file(READ "${BLOB_CHECKSUM_FILE}" BLOB_HASH_WAS)

		# Indicate for debugging and otherwise
		if("${BLOB_HASH}" STREQUAL "${BLOB_HASH_WAS}")
			message("Skipping blob ${BLOB_BASENAME}, is cached...")
			continue()
		else()
			message("Stale blob ${BLOB_BASENAME}...")
		endif()
	endif()

	# Read file as a hex string
	file(READ "${SQUIRRELJME_BLOB_FILE}" BLOB_HEX)

	# Length of the hex data
	string(LENGTH "${BLOB_HEX}" BLOB_HEX_LEN)

	# Notice, this can take awhile!
	math(EXPR BLOB_FILE_LEN "${BLOB_HEX_LEN} / 2")
	message("Packing blob ${BLOB_BASENAME} (${BLOB_FILE_LEN} bytes)...")

	# Start C struct
	file(APPEND "${BLOB_TARGET_FILE}"
		"#include \"sjmerc.h\"\n")
	file(APPEND "${BLOB_TARGET_FILE}"
		"static const uint8_t blob_${BLOB_BASENAME}[] = {\n")

	# The last index
	math(EXPR BLOB_LAST "${BLOB_HEX_LEN} - 2")

	# Run all the C data conversion
	set(BLOB_AT 0)
	while(BLOB_AT LESS BLOB_HEX_LEN)
		# Determine snippet to record
		string(SUBSTRING "${BLOB_HEX}" ${BLOB_AT} 2 BLOB_SNIP)

		# Add snippet
		file(APPEND "${BLOB_TARGET_FILE}"
			"0x${BLOB_SNIP}")

		# Comma?
		if(BLOB_AT LESS BLOB_LAST)
			file(APPEND "${BLOB_TARGET_FILE}"
				", ")
		endif()

		# Put on next row to make more easy to read
		math(EXPR BLOB_DIV "${BLOB_AT} % 24")
		if(BLOB_DIV EQUAL 0)
			file(APPEND "${BLOB_TARGET_FILE}"
				"\n")
		endif()

		# Count up
		math(EXPR BLOB_AT "${BLOB_AT} + 2")
	endwhile()

	# End C struct
	file(APPEND "${BLOB_TARGET_FILE}"
		"};\n")
	file(APPEND "${BLOB_TARGET_FILE}"
		"static const fileSize ${BLOB_BASENAME}_len = ${BLOB_FILE_LEN};\n")

	# Store checksum after everything is done, since we could have cancelled
	# in the middle of packing
	file(WRITE "${BLOB_CHECKSUM_FILE}" "${BLOB_HASH}")
endforeach()

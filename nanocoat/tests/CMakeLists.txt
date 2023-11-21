# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Testing framework for the main library

# Setup main test execution wrapper which runs within TAC
add_library(TACTestExecutorCore STATIC
	tac.c)

# Make sure we have our headers included accordingly
target_include_directories(TACTestExecutorCore PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_SOURCE_DIR}/tests/include")

# Depend on the core
target_link_libraries(TACTestExecutorCore PUBLIC
	Core
	${SQUIRRELJME_EXEC_LINK_LIBRARIES})

# Get a list of tests that are not TAC tests, but more general tests
file(GLOB basicTestSources
	"test*.c"
	"test*.cxx")
list(SORT basicTestSources
	COMPARE FILE_BASENAME)

# Setup base files for auto-generated test launching and method linking
set(outTestProtosH "${CMAKE_CURRENT_BINARY_DIR}/proto.h")
set(outTestStructH "${CMAKE_CURRENT_BINARY_DIR}/struct.h")
configure_file("${CMAKE_SOURCE_DIR}/cmake/blank.in.h"
	"${outTestProtosH}")
configure_file("${CMAKE_SOURCE_DIR}/cmake/blank.in.h"
	"${outTestStructH}")

# Include test header in the prototypes file
file(APPEND "${outTestProtosH}"
	"#include \"test.h\"\n")

# Initialize executable for basic tests, which contains everything
add_executable(BasicTest
	basic.c
	elevator.c
	unit.c
	${basicTestSources})

# Make it static
squirreljme_static_executable(BasicTest)

# Make sure we have our headers included accordingly
target_include_directories(BasicTest PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_BINARY_DIR}")

# Depend on the core
target_link_libraries(BasicTest PUBLIC
	Core)

# Process each individual test accordingly
foreach(basicTestSource ${basicTestSources})
	# We need to do this because on Windows, CLion+CMake just gives the full
	# path which we really do not want as it will mess up with tests
	set(initialBasicTestSource "${basicTestSource}")
	get_filename_component(basicTestSource
		"${basicTestSource}" NAME)

	# Remove the C/C++ to just get the base test
	string(REPLACE ".cxx" ""
		basicTestName "${basicTestSource}")
	string(REPLACE ".c" ""
		basicTestName "${basicTestSource}")

	# Long name of the test
	set(basicTestLongName "basic:${basicTestName}")

	# Full path, for message output
	get_filename_component(basicTestFullPath
		"${initialBasicTestSource}" ABSOLUTE)

	# Add test for this
	add_test(NAME "${basicTestLongName}"
		WORKING_DIRECTORY "${CMAKE_BINARY_DIR}"
		COMMAND "$<TARGET_FILE:BasicTest>" "${basicTestName}"
			"${basicTestFullPath}")

	# Code for skipped tests
	set_property(TEST "${basicTestLongName}"
		PROPERTY SKIP_RETURN_CODE 2)
	set_tests_properties("${basicTestLongName}"
		PROPERTIES SKIP_RETURN_CODE 2)

	# Test timeout (to prevent infinite loops)
	set_property(TEST "${basicTestLongName}"
		PROPERTY TIMEOUT 180)
	set_tests_properties("${basicTestLongName}"
		PROPERTIES TIMEOUT 180)

	# Label, name of test, used for filtering
	set_property(TEST "${basicTestLongName}"
		PROPERTY LABEL "${basicTestSource}")
	set_tests_properties("${basicTestLongName}"
		PROPERTIES LABEL "${basicTestSource}")

	# Add prototype and structures for test
	file(APPEND "${outTestProtosH}"
		"SJME_TEST_DECLARE(${basicTestName});\n")
	file(APPEND "${outTestStructH}"
		"{\"${basicTestName}\", ${basicTestName}},\n")
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
	configure_file(blank.h.in "${BLOB_BASENAME}.h"
		NEWLINE_STYLE LF)
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
		"#include \"sjme/nvm.h\"\n")
	file(APPEND "${BLOB_TARGET_FILE}"
		"static const uint8_t blob_${BLOB_BASENAME}[] = {\n")

	# The last index
	math(EXPR BLOB_LAST "${BLOB_HEX_LEN} - 2")

	# Run all the C data conversion
	set(BLOB_AT 0)
	while(BLOB_AT LESS BLOB_HEX_LEN)
		# Determine snippet to record
		string(SUBSTRING "${BLOB_HEX}" ${BLOB_AT}
			2 BLOB_SNIP)

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

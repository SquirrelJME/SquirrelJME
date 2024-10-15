# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Testing framework for the main library

# Decode all of the blobs to binary
squirreljme_decode_dir("${CMAKE_CURRENT_SOURCE_DIR}/blobs"
	"${CMAKE_CURRENT_BINARY_DIR}/blobs/binary")

# Then encode them all to source files, for use within tests
squirreljme_sourceize_dir("${CMAKE_CURRENT_BINARY_DIR}/blobs/binary"
	"${CMAKE_CURRENT_BINARY_DIR}/blobs")

# Setup main test execution wrapper which runs within TAC
add_library(TACTestExecutorCore STATIC
	$<TARGET_OBJECTS:Base>
	$<TARGET_OBJECTS:Core>
	src/tac.c)

# Make sure we have our headers included accordingly
target_include_directories(TACTestExecutorCore PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_SOURCE_DIR}/tests/include")

# Depend on the core
target_link_libraries(TACTestExecutorCore PUBLIC
	${SQUIRRELJME_EXEC_LINK_LIBRARIES})

# Get a list of tests that are not TAC tests, but more general tests
file(GLOB basicTestSources
	"test*.c"
	"test*.cxx")
squirreljme_list_file_sort(SORT basicTestSources)

# Get a list of all the supporting blobs
file(GLOB basicTestBlobs
	"${CMAKE_CURRENT_BINARY_DIR}/blobs/*.c")

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

# Basic test library
add_library(BasicTestLib OBJECT
	src/basic.c
	src/blobs.c
	src/mock.c
	src/unit.c
	${basicTestSources}
	${basicTestBlobs})

# Basic test library
add_library(BasicTestLibPIC OBJECT
	src/basic.c
	src/blobs.c
	src/mock.c
	src/unit.c
	${basicTestSources}
	${basicTestBlobs})

# Initialize executable for basic tests, which contains everything
add_executable(BasicTest
	src/basicMain.c
	$<TARGET_OBJECTS:Base>
	$<TARGET_OBJECTS:Core>
	$<TARGET_OBJECTS:BasicTestLib>)

# Make it static
squirreljme_static_executable(BasicTest)

# Make sure we have our headers included accordingly
target_include_directories(BasicTestLib PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_BINARY_DIR}"
	"${CMAKE_CURRENT_BINARY_DIR}/blobs")
target_include_directories(BasicTestLibPIC PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_BINARY_DIR}"
	"${CMAKE_CURRENT_BINARY_DIR}/blobs")

# Make sure the PIC libraries are position independent!
if(SQUIRRELJME_ENABLE_FPIC)
	set_target_properties(BasicTestLibPIC PROPERTIES
		POSITION_INDEPENDENT_CODE ON)
endif()

# Make sure we have our headers included accordingly
target_include_directories(BasicTest PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_BINARY_DIR}"
	"${CMAKE_CURRENT_BINARY_DIR}/blobs")

# Depend on the core
target_link_libraries(BasicTest PUBLIC
	Core)

# Include debugging info for tests
if(CMAKE_BUILD_TYPE STREQUAL "Release")
	if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
		target_compile_options(BasicTestLib PRIVATE
			"-g")
		target_compile_options(BasicTestLibPIC PRIVATE
			"-g")
	elseif(MSVC)
		target_compile_options(BasicTestLib PRIVATE
			"/DEBUG")
		target_compile_options(BasicTestLibPIC PRIVATE
			"/DEBUG")
	endif()
endif()

# Process each individual test accordingly
message(DEBUG "Looking for tests...")
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
	message(STATUS "Registering test ${basicTestLongName}...")
	add_test(NAME "${basicTestLongName}"
		WORKING_DIRECTORY "${CMAKE_BINARY_DIR}"
		COMMAND BasicTest "${basicTestName}"
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

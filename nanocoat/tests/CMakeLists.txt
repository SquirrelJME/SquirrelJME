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

# Nano executable
add_executable(NanoTest
	src/nano.c
	src/tacNal.c
	$<TARGET_OBJECTS:Base>
	$<TARGET_OBJECTS:Core>
	$<TARGET_OBJECTS:BasicTestLib>)

# TAC executable
add_executable(TACTest
	src/tac.c
	src/tacNal.c
	$<TARGET_OBJECTS:Base>
	$<TARGET_OBJECTS:Core>
	$<TARGET_OBJECTS:BasicTestLib>)

# Make them both static
squirreljme_static_executable(BasicTest)
squirreljme_static_executable(NanoTest)
squirreljme_static_executable(TACTest)

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

target_include_directories(NanoTest PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_BINARY_DIR}"
	"${CMAKE_CURRENT_BINARY_DIR}/blobs")

target_include_directories(TACTest PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_SOURCE_DIR}/include"
	"${CMAKE_CURRENT_BINARY_DIR}"
	"${CMAKE_CURRENT_BINARY_DIR}/blobs")

# Depend on the core
add_dependencies(BasicTest Core)
squirreljme_link_libraries_required(BasicTest PUBLIC
	Core)

add_dependencies(NanoTest Core)
squirreljme_link_libraries_required(NanoTest PUBLIC
	Core)

add_dependencies(TACTest Core)
squirreljme_link_libraries_required(TACTest PUBLIC
	Core)

# Much of the test targets are the same effectively
foreach(target IN ITEMS BasicTestLib BasicTest NanoTest TACTest)
	# Include debugging info for tests
	if(CMAKE_BUILD_TYPE STREQUAL "Release" OR
		CMAKE_BUILD_TYPE STREQUAL "RelWithDebInfo" OR
		CMAKE_BUILD_TYPE STREQUAL "Debug")
		if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
			target_compile_options(${target} PRIVATE
				"-g")
		elseif(MSVC)
			target_compile_options(${target} PRIVATE
				"/DEBUG")
		endif()
	endif()

	# Remove the vast majority of warnings for tests as they can be more
	# lenient
	if(MSVC)
		squirreljme_target_property_rewrite(${target} COMPILE_OPTIONS
			"/W[3210]" "/W0")
	elseif(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
		add_compile_options("-w")
	endif()
endforeach()

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
	message(STATUS "Basic Test: ${basicTestLongName}")
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

# Load in TAC/Nano tests from rom/squirreljme-(slow-)test.(list|nano)
foreach(testListFile
	"${CMAKE_SOURCE_DIR}/rom/squirreljme-test.list"
	"${CMAKE_SOURCE_DIR}/rom/squirreljme-test.nano"
	"${CMAKE_SOURCE_DIR}/rom/squirreljme-slow-test.list"
	"${CMAKE_SOURCE_DIR}/rom/squirreljme-slow-test.nano")
	# Not actually here?
	if (NOT EXISTS "${testListFile}")
		continue()
	endif()

	# Note it
	message(STATUS "Using tests from ${testListFile}...")

	# Are these nano tests?
	string(FIND "${testListFile}" ".nano"
		testIsNano)

	# Are these the debug test variants?
	string(FIND "${testListFile}" "-slow-"
		testDebugVariant)

	# Determine the prefix to use
	if("${testIsNano}" GREATER_EQUAL "0")
		if("${testDebugVariant}" GREATER_EQUAL "0")
			set(tacPrefix "nano-debug")
		else()
			set(tacPrefix "nano")
		endif()
	else()
		if("${testDebugVariant}" GREATER_EQUAL "0")
			set(tacPrefix "tac-debug")
		else()
			set(tacPrefix "tac")
		endif()
	endif()

	# Determine actual Jar name
	if("${testIsNano}" GREATER_EQUAL "0")
		string(REPLACE ".nano" ".jar" testJarFile "${testListFile}")
	else()
		string(REPLACE ".list" ".jar" testJarFile "${testListFile}")
	endif()

	# Read in all lines
	file(STRINGS "${testListFile}" testList)

	# Notice
	if("${testIsNano}" GREATER_EQUAL "0")
		message(DEBUG "Looking for Nano Tests...")
	else()
		message(DEBUG "Looking for TAC Tests...")
	endif()

	# Register TAC tests
	foreach(test ${testList})
		# Turn into list
		string(REPLACE "|" ";" tacTestNameList "${test}")
		list(GET tacTestNameList 0 tacTestJar)
		list(GET tacTestNameList 1 tacTestClassPath)
		list(GET tacTestNameList 2 tacTestClass)

		set(tacTestName "${test}")
		set(tacTestLongName "${tacPrefix}:${tacTestJar}:${tacTestClass}")

		# Notice and register it
		if("${testIsNano}" GREATER_EQUAL "0")
			message(STATUS "Nano Test: ${tacTestLongName}")

			# Register it
			add_test(NAME "${tacTestLongName}"
				WORKING_DIRECTORY "${CMAKE_BINARY_DIR}"
				COMMAND NanoTest "${testJarFile}"
					"${tacTestJar}" "${tacTestClassPath}" "${tacTestClass}")
		else()
			message(STATUS "TAC Test: ${tacTestLongName}")

			# Register it
			add_test(NAME "${tacTestLongName}"
				WORKING_DIRECTORY "${CMAKE_BINARY_DIR}"
				COMMAND TACTest "${testJarFile}"
					"${tacTestJar}" "${tacTestClassPath}" "${tacTestClass}")
		endif()

		# Code for skipped tests
		set_property(TEST "${tacTestLongName}"
			PROPERTY SKIP_RETURN_CODE 2)
		set_tests_properties("${tacTestLongName}"
			PROPERTIES SKIP_RETURN_CODE 2)

		# Test timeout (to prevent infinite loops)
		set_property(TEST "${tacTestLongName}"
			PROPERTY TIMEOUT 180)
		set_tests_properties("${tacTestLongName}"
			PROPERTIES TIMEOUT 180)

		# Label, name of test, used for filtering
		set_property(TEST "${tacTestLongName}"
			PROPERTY LABEL "${tacTestLongName}")
		set_tests_properties("${tacTestLongName}"
			PROPERTIES LABEL "${tacTestLongName}")
	endforeach()
endforeach()

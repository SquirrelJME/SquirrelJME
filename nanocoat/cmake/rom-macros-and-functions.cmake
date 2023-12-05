# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Functions and macros for ROM source code, allows all the
# ROM source information to be duplicated accordingly and to change all of
# at once instead of auto-generating new code.

# Initializes the structure and otherwise for ROMs themselves which
# depend on libraries
function(squirreljme_rom sourceSet clutterLevel)
	# Name of the ROM
	set(romTask "ROM_${sourceSet}_${clutterLevel}")

	# Add base object library
	add_library("${romTask}" STATIC
		${squirreljme_dynamic_objLibs})

	# If this is a test ROM, we need to actually link it in with the
	# TAC test execution core
	if ("${sourceSet}" STREQUAL "test")
		# Name of the TAC executable task to run the test in
		set(romTacTask "TACTestExecutor_${clutterLevel}")

		# Define executable which uses this ROM and the others
		add_executable("${romTacTask}"
			"${CMAKE_SOURCE_DIR}/tests/tacPayload.c")

		# Need to include our headers and such
		target_include_directories("${romTacTask}"
			PUBLIC "${CMAKE_SOURCE_DIR}/include"
			PUBLIC "${CMAKE_SOURCE_DIR}/rom/main_${clutterLevel}"
			PUBLIC "${CMAKE_SOURCE_DIR}/rom/testFixtures_${clutterLevel}"
			PUBLIC "${CMAKE_SOURCE_DIR}/rom/${sourceSet}_${clutterLevel}")

		# Include the libraries needed to run the ROM
		target_link_libraries("${romTacTask}" PUBLIC
			"TACTestExecutorCore"
			"ROM_main_${clutterLevel}"
			"ROM_testFixtures_${clutterLevel}"
			"ROM_${sourceSet}_${clutterLevel}")

		# Define ROMs available for use
		target_compile_definitions("${romTacTask}"
			PUBLIC SJME_CONFIG_ROM0=rom_main_${clutterLevel}
			PUBLIC SJME_CONFIG_ROM0_HEADER="rom_main_${clutterLevel}.h"
			PUBLIC SJME_CONFIG_ROM1=rom_testFixtures_${clutterLevel}
			PUBLIC SJME_CONFIG_ROM1_HEADER="rom_testFixtures_${clutterLevel}.h"
			PUBLIC SJME_CONFIG_ROM2=rom_${sourceSet}_${clutterLevel}
			PUBLIC SJME_CONFIG_ROM2_HEADER="rom_${sourceSet}_${clutterLevel}.h")
	endif()
endfunction()

# Include a previous instance of squirreljme_romLibrary
macro(squirreljme_romLibrary_include sourceSet
	clutterLevel libName libIdentifier)
	# Include directory where the module exists
	add_subdirectory("modules/${libIdentifier}")

	# Used for later inclusion
	list(APPEND squirreljme_dynamic_objLibs
		"$<$<TARGET_OBJECTS:${sourceSet}_${clutterLevel}_${libIdentifier}>>")
endmacro()

# Declares a class for a library
macro(squirreljme_romLibrary_class sourceSet clutterLevel
	libName libIdentifier className
	classIdentifier classHeader classSource)
	list(APPEND squirreljme_dynamic_classesFiles
		"${classSource}")
endmacro()

# Initializes the structure and otherwise needed for ROM libraries
# Remaining argv are sources
function(squirreljme_romLibrary sourceSet clutterLevel
	libName libIdentifier libHeader libSource
	classes)
	# Library task for simplicity
	set(libTask
		"ROMLib_${sourceSet}_${clutterLevel}_${libName}_Object")

	# Add object library which includes all the output object files
	add_library("${libTask}" OBJECT
		${filesList})

	# Include main headers
	target_include_directories("${libTask}" PUBLIC
		"${CMAKE_SOURCE_DIR}/include")

	# Disable warnings, since it gets extremely noisy when compiling libraries
	if(CMAKE_C_COMPILER_ID STREQUAL "MSVC")
		target_compile_options("${libTask}" PRIVATE
			"/W0")
	elseif(CMAKE_C_COMPILER_ID STREQUAL "GNU" OR
		CMAKE_C_COMPILER_ID STREQUAL "Clang")
		target_compile_options("${libTask}" PRIVATE
			"-w")
	endif()
endfunction()

# Defines a test for a ROM library entry using the TAC framework
function(squirreljme_romLibraryTest libNameStr sourceSet
	clutterLevel testName testClassPath)
	# Determine name for the actual test
	set(longName "${clutterLevel}:${libNameStr}:${testName}")

	# Register test
	message(STATUS
		"Adding test ${testName} in ${libNameStr}...")
	set(romTacTask "TACTestExecutor_${clutterLevel}")
	add_test(NAME "${longName}"
		WORKING_DIRECTORY "${CMAKE_BINARY_DIR}"
		COMMAND "${romTacTask}"
			"-classpath" "${testClassPath}"
			"net.multiphasicapps.tac.MainSingleRunner"
			"${testName}")

	# Code for skipped tests
	set_property(TEST "${longName}"
		PROPERTY SKIP_RETURN_CODE 2)
	set_tests_properties("${longName}"
		PROPERTIES SKIP_RETURN_CODE 2)

	# Test timeout (to prevent infinite loops)
	set_property(TEST "${longName}"
		PROPERTY TIMEOUT 180)
	set_tests_properties("${longName}"
		PROPERTIES TIMEOUT 180)
endfunction()

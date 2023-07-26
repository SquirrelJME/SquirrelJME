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
function(squirreljme_rom romName objectLibs)
	# Add base object library
	add_library("ROM_${romName}_Object" OBJECT
		${objectLibs})
endfunction()

# Initializes the structure and otherwise needed for ROM libraries
# Remaining argv are sources
function(squirreljme_romLibrary libNameStr filesList)
	# Add object library which includes all the output object files
	add_library("ROMLib_${libNameStr}_Object" OBJECT
		${filesList})

	# Include main headers
	target_include_directories("ROMLib_${libNameStr}_Object" PUBLIC
		"${CMAKE_SOURCE_DIR}/include")
endfunction()

# Defines a test for a ROM library entry
function(squirreljme_romLibraryTest libNameStr testName
	testClassPath)
	# Register test
	message("Adding test ${testName} in ${libNameStr}...")
	add_test(NAME "${libNameStr}:${testName}"
		COMMAND TestExecutor
			"-classpath" "${testClassPath}"
			"${testName}")

	# Code for skipped tests
	set_property(TEST "${libNameStr}:${testName}"
		PROPERTY SKIP_RETURN_CODE 42)
	set_tests_properties("${libNameStr}:${testName}"
		PROPERTIES SKIP_RETURN_CODE 42)

	# Test timeout (to prevent infinite loops)
	set_property(TEST "${libNameStr}:${testName}"
		PROPERTY TIMEOUT 180)
	set_tests_properties("${libNameStr}:${testName}"
		PROPERTIES TIMEOUT 180)
endfunction()

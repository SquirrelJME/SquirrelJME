# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Handling of building standalone natives for both emulator-base
# and NanoCoat, these are for later repackaging

# Directory where the natives are placed
define_property(TARGET PROPERTY SQUIRRELJME_EMULATOR_NATIVE_PATH
	BRIEF_DOCS "Path where emulator natives are placed.")

# Build natives for every known compiler on the system
foreach(compilerMap IN LISTS SQUIRRELJME_COMPILER_MAP)
	# Obtain back the system and architecture
	squirreljme_unmap(compilerType 0 "${compilerMap}")
	squirreljme_unmap(systemNormal 1 "${compilerMap}")
	squirreljme_unmap(archNormal 2 "${compilerMap}")

	# Debug
	message(STATUS "Checking compiler ${compilerType} "
		"${systemNormal}/${archNormal}...")

	# Where should the native build root be?
	file(TO_CMAKE_PATH
		"${CMAKE_BINARY_DIR}/nativeBuild/${systemNormal}/${archNormal}/"
		buildDir)
	file(MAKE_DIRECTORY "${buildDir}")

	# Where should the natives actually be placed?
	file(TO_CMAKE_PATH
		"${CMAKE_BINARY_DIR}/natives/${systemNormal}/${archNormal}/"
		nativesDir)
	file(MAKE_DIRECTORY "${nativesDir}")

	# Is this a GCC compiler?
	if("${compilerType}" STREQUAL "gcc")
		# Where is the GCC executable?
		set(compilerExe "${GCC_${systemNormal}_${archNormal}_EXECUTABLE}")

	# Unknown
	else()
		# Warn that this is not yet handled
		message(WARNING "Unsupported compiler ${compilerType}...")

		# Skip it
		continue()
	endif()

	# Configure CMake build for the compiler
	execute_process(COMMAND "${CMAKE_COMMAND}"
		"-DCMAKE_C_COMPILER=${compilerExe}"
		"-DSQUIRRELJME_BINARY_OUTPUT_DIR=${nativesDir}"
		"-DSQUIRRELJME_DYLIB_OUTPUT_DIR=${nativesDir}"
		"-B" "${buildDir}"
		"-S" "${CMAKE_SOURCE_DIR}/emulators/emulator-base-native"
		RESULT_VARIABLE nestedResult)

	# Was this successful?
	if("${nestedResult}" EQUAL "0")
		# Note that it was
		message(STATUS "Emulator Native ${systemNormal}/${archNormal} -> "
			"standaloneNatives_${systemNormal}_${archNormal}")

		# Add target which builds the natives
		add_custom_target(standaloneNatives_${systemNormal}_${archNormal}
			COMMAND "${CMAKE_COMMAND}"
				"--build" "${buildDir}"
				"--target" "libEmulatorBase"
			COMMAND_EXPAND_LISTS)

		# Set the emulator native path
		set_target_properties(standaloneNatives_${systemNormal}_${archNormal}
			PROPERTIES
			SQUIRRELJME_EMULATOR_NATIVE_PATH "${nativesDir}"
			ADDITIONAL_CLEAN_FILES "${buildDir};${nativesDir}")
	endif()
endforeach()

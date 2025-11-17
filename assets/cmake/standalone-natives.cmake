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
define_property(TARGET PROPERTY SQUIRRELJME_CORE_NATIVE_PATH
	BRIEF_DOCS "Path where core natives are placed.")
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

	# Where should NanoCoat core place its binaries?
	file(TO_CMAKE_PATH
		"${CMAKE_BINARY_DIR}/coreBuild/${systemNormal}/${archNormal}/"
		coreBuild)
	file(TO_CMAKE_PATH
		"${CMAKE_BINARY_DIR}/core/${systemNormal}/${archNormal}/"
		coreOut)

	# Where should libEmulatorBase place its binaries?
	file(TO_CMAKE_PATH
		"${CMAKE_BINARY_DIR}/emulatorBuild/${systemNormal}/${archNormal}/"
		emulatorBuild)
	file(TO_CMAKE_PATH
		"${CMAKE_BINARY_DIR}/emulator/${systemNormal}/${archNormal}/"
		emulatorOut)

	# Make sure all directories exist
	file(MAKE_DIRECTORY "${coreBuild}")
	file(MAKE_DIRECTORY "${coreOut}")
	file(MAKE_DIRECTORY "${emulatorBuild}")
	file(MAKE_DIRECTORY "${emulatorOut}")

	# Name of the rule
	set(ruleName "standaloneNatives_${systemNormal}_${archNormal}")

	# Is this a GCC compiler?
	if("${compilerType}" STREQUAL "gcc")
		# Where is the GCC executable?
		set(compilerExe "${GCC_${systemNormal}_${archNormal}_EXECUTABLE}")

		squirreljme_gcc_defines(boop "${compilerExe}")

	# Unknown
	else()
		# Warn that this is not yet handled
		message(WARNING "Unsupported compiler ${compilerType}...")

		# Skip it
		continue()
	endif()

	# Configure CMake build for NanoCoat Core
	execute_process(COMMAND "${CMAKE_COMMAND}"
		"-DCMAKE_C_COMPILER=${compilerExe}"
		"-DSQUIRRELJME_EMULATOR_BASE_IMPORT_DIR=${coreOut}"
		"-DSQUIRRELJME_BINARY_OUTPUT_DIR=${coreOut}"
		"-DSQUIRRELJME_DYLIB_OUTPUT_DIR=${coreOut}"
		"-B" "${coreBuild}"
		"-S" "${CMAKE_SOURCE_DIR}/nanocoat"
		RESULT_VARIABLE coreResult
		OUTPUT_FILE "${CMAKE_BINARY_DIR}/ruleName.core.out"
		ERROR_FILE "${CMAKE_BINARY_DIR}/ruleName.core.err")

	# Configure CMake build for libEmulatorBase
	if("${coreResult}" EQUAL "0")
		execute_process(COMMAND "${CMAKE_COMMAND}"
			"-DCMAKE_C_COMPILER=${compilerExe}"
			"-DSQUIRRELJME_EMULATOR_BASE_IMPORT_DIR=${coreOut}"
			"-DSQUIRRELJME_BINARY_OUTPUT_DIR=${emulatorOut}"
			"-DSQUIRRELJME_DYLIB_OUTPUT_DIR=${emulatorOut}"
			"-B" "${emulatorBuild}"
			"-S" "${CMAKE_SOURCE_DIR}/emulators/emulator-base-native"
			RESULT_VARIABLE emulatorResult
			OUTPUT_FILE "${CMAKE_BINARY_DIR}/ruleName.emulator.out"
			ERROR_FILE "${CMAKE_BINARY_DIR}/ruleName.emulator.err")
	else()
		set(emulatorResult "1")
	endif()

	# Was this successful?
	if("${coreResult}" EQUAL "0" AND
		"${emulatorResult}" EQUAL "0")
		# Note that it was
		message(STATUS "Emulator Native ${systemNormal}/${archNormal} -> "
			"${ruleName}")

		# Add target which builds the natives
		add_custom_target(${ruleName}
			COMMAND "${CMAKE_COMMAND}"
				"--build" "${coreBuild}"
				"--target" "BaseStatic" "libJvmDyLib"
					"ScritchUI" "ScritchAudio"
			COMMAND "${CMAKE_COMMAND}"
				"--build" "${emulatorBuild}"
				"--target" "libEmulatorBase"
			COMMAND_EXPAND_LISTS)

		# Add this rule to the standalone set
		list(APPEND SQUIRRELJME_STANDALONE_NATIVE_RULES
			"standaloneNatives_${systemNormal}_${archNormal}")

		# Set the emulator native path
		set_target_properties(standaloneNatives_${systemNormal}_${archNormal}
			PROPERTIES
			SQUIRRELJME_CORE_NATIVE_PATH "${emulatorOut}"
			SQUIRRELJME_EMULATOR_NATIVE_PATH "${emulatorOut}"
			SQUIRRELJME_SYSTEM "${systemNormal}"
			SQUIRRELJME_ARCH "${archNormal}"
			ADDITIONAL_CLEAN_FILES
				"${coreBuild};${coreOut};${emulatorBuild};${emulatorOut}")
	endif()
endforeach()

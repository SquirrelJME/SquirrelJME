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
	BRIEF_DOCS "Path where core natives are placed."
	FULL_DOCS "Path where core natives are placed.")
define_property(TARGET PROPERTY SQUIRRELJME_EMULATOR_NATIVE_PATH
	BRIEF_DOCS "Path where emulator natives are placed."
	FULL_DOCS "Path where emulator natives are placed.")

# Build natives for every known compiler on the system
foreach(compilerMap IN LISTS SQUIRRELJME_COMPILER_MAP)
	# Obtain back the system and architecture
	squirreljme_unmap(systemNormal 0 "${compilerMap}")
	squirreljme_unmap(archNormal 1 "${compilerMap}")

	# Progress indication
	message(STATUS "Looking at "
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

	# Make sure the output directories exist
	file(MAKE_DIRECTORY "${coreOut}")
	file(MAKE_DIRECTORY "${emulatorOut}")

	# Name of the rule
	set(ruleName "standaloneNatives_${systemNormal}_${archNormal}")

	# Progress indication
	message(STATUS "Configuring NanoCoat Core "
		"${systemNormal}/${archNormal}...")

	# Which arguments to use?
	unset(va)
	unset(vb)
	unset(vc)
	unset(vd)
	squirreljme_compiler_cmake_args(va vb vc vd
		"${systemNormal}" "${archNormal}")

	# Possibly broken configure?
	if(EXISTS "${coreBuild}/CMakeCache.txt" AND
		(NOT EXISTS "${coreBuild}/arch__.tgt" OR
		NOT EXISTS "${coreBuild}/system.tgt"))
		file(REMOVE_RECURSE "${coreBuild}")
	endif()

	# Configure CMake build for NanoCoat Core
	if(EXISTS "${coreBuild}/CMakeCache.txt" AND
		EXISTS "${coreBuild}/arch__.tgt" AND
		EXISTS "${coreBuild}/system.tgt")
		set(coreResult 0)
	else()
		file(MAKE_DIRECTORY "${coreBuild}")
		execute_process(COMMAND "${CMAKE_COMMAND}"
			"${va}" "${vb}" "${vc}" "${vd}"
			"-DSQUIRRELJME_EMULATOR_BASE_IMPORT_DIR=${coreOut}"
			"-DSQUIRRELJME_BINARY_OUTPUT_DIR=${coreOut}"
			"-DSQUIRRELJME_DYLIB_OUTPUT_DIR=${coreOut}"
			"-B" "${coreBuild}"
			"-S" "${CMAKE_SOURCE_DIR}/nanocoat"
			RESULT_VARIABLE coreResult
			OUTPUT_FILE "${CMAKE_BINARY_DIR}/${ruleName}.core.out"
			ERROR_FILE "${CMAKE_BINARY_DIR}/${ruleName}.core.err")
	endif()

	# Configure CMake build for libEmulatorBase
	if("${coreResult}" EQUAL "0")
		# Progress indication
		message(STATUS "Configuring libEmulatorBase "
			"${systemNormal}/${archNormal}...")

		# Possibly broken configure?
		if(EXISTS "${emulatorBuild}/CMakeCache.txt" AND
			(NOT EXISTS "${emulatorBuild}/arch__.tgt" OR
			NOT EXISTS "${emulatorBuild}/system.tgt"))
			file(REMOVE_RECURSE "${emulatorBuild}")
		endif()

		# Now do the configure for emulator-base
		if(EXISTS "${emulatorBuild}/CMakeCache.txt" AND
			EXISTS "${emulatorBuild}/arch__.tgt" AND
			EXISTS "${emulatorBuild}/system.tgt")
			set(emulatorResult 0)
		else()
			file(MAKE_DIRECTORY "${emulatorBuild}")
			execute_process(COMMAND "${CMAKE_COMMAND}"
				"${va}" "${vb}" "${vc}" "${vd}"
				"-DSQUIRRELJME_EMULATOR_BASE_IMPORT_DIR=${coreOut}"
				"-DSQUIRRELJME_BINARY_OUTPUT_DIR=${emulatorOut}"
				"-DSQUIRRELJME_DYLIB_OUTPUT_DIR=${emulatorOut}"
				"-B" "${emulatorBuild}"
				"-S" "${CMAKE_SOURCE_DIR}/emulators/emulator-base-native"
				RESULT_VARIABLE emulatorResult
				OUTPUT_FILE "${CMAKE_BINARY_DIR}/${ruleName}.emulator.out"
				ERROR_FILE "${CMAKE_BINARY_DIR}/${ruleName}.emulator.err")
		endif()
	else()
		set(emulatorResult "1")
	endif()

	# Was this successful?
	if("${coreResult}" EQUAL "0" AND
		"${emulatorResult}" EQUAL "0")
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

		# Add note for the rule that was generated
		message(STATUS "Standalone Native "
			"${systemNormal}/${archNormal} -> ${ruleName}")

		# Add this rule to the standalone set
		list(APPEND SQUIRRELJME_STANDALONE_NATIVE_RULES
			"${ruleName}")

		# Set the emulator native path
		set_target_properties(${ruleName}
			PROPERTIES
			SQUIRRELJME_CORE_NATIVE_PATH "${coreOut}"
			SQUIRRELJME_EMULATOR_NATIVE_PATH "${emulatorOut}"
			SQUIRRELJME_SYSTEM "${systemNormal}"
			SQUIRRELJME_ARCH "${archNormal}"
			ADDITIONAL_CLEAN_FILES
				"${coreBuild};${coreOut};${emulatorBuild};${emulatorOut}")

			# Register to CI/CD
			squirreljme_cicd_register(${ruleName})
	else()
		# Progress indication
		message(STATUS "Failed to configure "
			"${systemNormal}/${archNormal}: "
			"${coreResult} ${emulatorResult}!")

		# Core?
		if(EXISTS "${CMAKE_BINARY_DIR}/${ruleName}.core.err")
			file(STRINGS "${CMAKE_BINARY_DIR}/${ruleName}.core.err" coreErr)
			message(WARNING ${coreErr})
		endif()

		# Emulator?
		if(EXISTS "${CMAKE_BINARY_DIR}/${ruleName}.emulator.err")
			file(STRINGS "${CMAKE_BINARY_DIR}/${ruleName}.emulator.err" emuErr)
			message(WARNING ${emuErr})
		endif()
	endif()
endforeach()

# Go through all natives and package them individually
foreach(rule IN LISTS SQUIRRELJME_STANDALONE_NATIVE_RULES)
	# Where were the binaries and list files placed?
	get_target_property(coreNativePath ${rule}
		SQUIRRELJME_CORE_NATIVE_PATH)
	get_target_property(emulatorNativePath ${rule}
		SQUIRRELJME_EMULATOR_NATIVE_PATH)

	# Which system/arch does this target?
	get_target_property(systemNormal ${rule}
		SQUIRRELJME_SYSTEM)
	get_target_property(archNormal ${rule}
		SQUIRRELJME_ARCH)

	# Remember which actual natives were put in
	list(APPEND SQUIRRELJME_STANDALONE_NATIVES_AVAILABLE
		"${systemNormal}!${archNormal}")

	# The target name
	set(targetName "natives.${systemNormal}.${archNormal}")

	# Temporary
	set(workPath
		"${CMAKE_BINARY_DIR}/work-pack/${systemNormal}-${archNormal}")
	set(outputDir
		"${CMAKE_BINARY_DIR}/")
	set(outputZip
		"${outputDir}/natives-${systemNormal}-${archNormal}.zip")

	# Add the natives to their own individual archive
	file(MAKE_DIRECTORY "${outputDir}" "${workPath}")
	add_custom_target(${targetName}
		COMMAND "${CMAKE_COMMAND}" "-E"
			"make_directory"
			"${workPath}/natives/${systemNormal}/${archNormal}"
		COMMAND "${CMAKE_COMMAND}" "-E"
			"copy_directory"
			"${coreNativePath}/" "${emulatorNativePath}/"
			"${workPath}/natives/${systemNormal}/${archNormal}"
		COMMAND "${CMAKE_COMMAND}" "-E"
			"make_directory" "${outputDir}"
		COMMAND "${CMAKE_COMMAND}" "-E"
			"tar" "c" "${outputZip}" "--format=zip" "--" "."
		BYPRODUCTS "${outputZip}"
		WORKING_DIRECTORY "${workPath}"
		DEPENDS ${rule}
		COMMENT "Packaging ${systemNormal}/${archNormal}...")

	# Output type and location
	set_target_properties(${targetName} PROPERTIES
		SQUIRRELJME_OUTPUT_PATH "${outputZip}"
		SQUIRRELJME_OUTPUT_TYPE "natives")

	# These get uploaded into Fossil
	squirreljme_fossil_upload_register(${targetName})

	# Register to CI/CD
	squirreljme_cicd_register(${targetName})
endforeach()

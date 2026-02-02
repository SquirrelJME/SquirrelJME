# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Handling of building standalone natives for both emulator-base
# and NanoCoat, these are for later repackaging
# There are three methods for natives:
# - compiled: Compiled for the current and other systems that are found, then
#             uploaded to Fossil.
# - download: Download from the Fossil UV space.
# - cached: Exists in the output directory already.

# The download directory
file(TO_CMAKE_PATH "${CMAKE_BINARY_DIR}/download"
	"SQUIRRELJME_DOWNLOADS")

# The native combinations which are known, available in UV space as:
# unstable/0.3.0/natives-${systemNormal}-${archNormal}.zip
# unstable/0.3.0/natives-${systemNormal}-${archNormal}.zip.mkd
set(SQUIRRELJME_KNOWN_NATIVES)
list(APPEND SQUIRRELJME_KNOWN_NATIVES
	"linux!amd64"
	"linux!arm64l"
	"linux!ia32"
	"linux!mips32b"
	"linux!mips32b6"
	"linux!mips32l"
	"linux!mips32l6"
	"linux!mips64b"
	"linux!mips64b6"
	"linux!mips64l"
	"linux!mips64l6"
	"linux!powerpc32b"
	"linux!powerpc64l"
	"linux!riscv64"
	"macosx!arm64l"
	"macosx!amd64l"
	"macosx!ia32"
	"macosx!powerpc32b"
	"windows!amd64"
	"windows!ia32")

# Appends a native rule for a given method with the given system and
# architecture
macro(squirreljme_natives_append_rule newRule systemNormal archNormal method)
	# Determine the names
	squirreljme_natives_order_name(orderName ${systemNormal} ${archNormal})
	squirreljme_natives_rule_name(ruleName ${systemNormal} ${archNormal})

	# Make sure the target has the system and architecture and set
	set_target_properties(${newRule} PROPERTIES
		SQUIRRELJME_SYSTEM "${systemNormal}"
		SQUIRRELJME_ARCH "${archNormal}"
		SQUIRRELJME_NATIVES_METHOD "${method}")

	# Obtain the order list
	unset(orderList)
	get_property(orderList GLOBAL PROPERTY ${orderName})

	# Is this the first in the order/
	if("${orderList}" STREQUAL "")
		set(firstOrder YES)
	else()
		set(firstOrder NO)
	endif()

	# Append to the order list
	list(APPEND orderList
		"${method}!${newRule}")
	set_property(GLOBAL PROPERTY ${orderName} "${orderList}")

	# If this is the first order, then set it as the default rule
	if(firstOrder)
		# Note it
		message(STATUS "Target ${systemNormal}/${archNormal}: "
			"${newRule} (default)")

		# Add target and its properties
		add_custom_target(${ruleName}
			DEPENDS ${newRule})

		# Always build defaults
		set_target_properties(${ruleName} PROPERTIES
			EXCLUDE_FROM_ALL NO)

		# And copy all of its properties
		squirreljme_copy_properties(${newRule} ${ruleName}
			SQUIRRELJME_CORE_NATIVE_PATH
			SQUIRRELJME_EMULATOR_NATIVE_PATH
			SQUIRRELJME_SYSTEM
			SQUIRRELJME_ARCH
			SQUIRRELJME_OUTPUT_PATH
			SQUIRRELJME_OUTPUT_TYPE)

	# Not first order
	else()
		# Note it
		message(STATUS "Target ${systemNormal}/${archNormal}: "
			"${newRule}")
	endif()

	# Regardless of what we did, we want to upload this to Fossil if not
	# cached or download
	if("${method}" STREQUAL "compiler" AND
		NOT "${archNormal}" STREQUAL "base")
		squirreljme_fossil_upload(${newRule})
	endif()
endmacro()

# Add rules and detection steps for the three
squirreljme_include("standalone-natives-compiled.cmake")
squirreljme_include("standalone-natives-download.cmake")
squirreljme_include("standalone-natives-cached.cmake")

#
## Add alias targets for the preferred highest detected method
#
## Add rules to package resultant natives to upload to Fossil, if compiled
#
## Package natives
#macro(squirreljme_package_natives rule)
#	# Where were the binaries and list files placed?
#	get_target_property(coreNativePath ${rule}
#		SQUIRRELJME_CORE_NATIVE_PATH)
#	get_target_property(emulatorNativePath ${rule}
#		SQUIRRELJME_EMULATOR_NATIVE_PATH)
#
#	# Which system/arch does this target?
#	get_target_property(systemNormal ${rule}
#		SQUIRRELJME_SYSTEM)
#	get_target_property(archNormal ${rule}
#		SQUIRRELJME_ARCH)
#
#	# Remember which actual natives were put in
#	list(APPEND SQUIRRELJME_JAR_NATIVES_AVAILABLE
#		"${systemNormal}!${archNormal}")
#
#	# The target name
#	set(targetName "natives.${systemNormal}.${archNormal}")
#
#	# Temporary
#	set(workPath
#		"${CMAKE_BINARY_DIR}/work-pack/${systemNormal}-${archNormal}")
#	set(outputDir
#		"${CMAKE_BINARY_DIR}")
#	set(outputZip
#		"${outputDir}/natives-${systemNormal}-${archNormal}.zip")
#
#	# Add the natives to their own individual archive
#	file(MAKE_DIRECTORY "${outputDir}" "${workPath}")
#	add_custom_target(${targetName}
#		COMMAND "${CMAKE_COMMAND}" "-E"
#			"make_directory"
#			"${workPath}/natives/${systemNormal}/${archNormal}"
#		COMMAND "${CMAKE_COMMAND}" "-E"
#			"copy_directory"
#			"${coreNativePath}/" "${emulatorNativePath}/"
#			"${workPath}/natives/${systemNormal}/${archNormal}"
#		COMMAND "${CMAKE_COMMAND}" "-E"
#			"make_directory" "${outputDir}"
#		COMMAND "${CMAKE_COMMAND}" "-E"
#			"tar" "c" "${outputZip}" "--format=zip" "--" "."
#		BYPRODUCTS "${outputZip}"
#		WORKING_DIRECTORY "${workPath}"
#		DEPENDS ${rule}
#		COMMENT "Packaging ${systemNormal}/${archNormal}...")
#
#	# Output type and location
#	set_target_properties(${targetName} PROPERTIES
#		SQUIRRELJME_OUTPUT_PATH "${outputZip}"
#		SQUIRRELJME_OUTPUT_TYPE "natives")
#
#	# These get uploaded into Fossil
#	squirreljme_fossil_upload_register(${targetName})
#endmacro()
#
## Add natives via the compiler
#macro(squirreljme_natives_via_compiler compilerMap)
#	# Obtain back the system and architecture
#	squirreljme_unmap(systemNormal 0 "${compilerMap}")
#	squirreljme_unmap(archNormal 1 "${compilerMap}")
#
#	# Progress indication
#	message(STATUS "Looking at "
#		"${systemNormal}/${archNormal}...")
#
#	# Where should NanoCoat core place its binaries?
#	file(TO_CMAKE_PATH
#		"${CMAKE_BINARY_DIR}/coreBuild/${systemNormal}/${archNormal}/"
#		coreBuild)
#	file(TO_CMAKE_PATH
#		"${CMAKE_BINARY_DIR}/core/${systemNormal}/${archNormal}/"
#		coreOut)
#
#	# Where should libEmulatorBase place its binaries?
#	file(TO_CMAKE_PATH
#		"${CMAKE_BINARY_DIR}/emulatorBuild/${systemNormal}/${archNormal}/"
#		emulatorBuild)
#	file(TO_CMAKE_PATH
#		"${CMAKE_BINARY_DIR}/emulator/${systemNormal}/${archNormal}/"
#		emulatorOut)
#
#	# Make sure the output directories exist
#	file(MAKE_DIRECTORY "${coreOut}")
#	file(MAKE_DIRECTORY "${emulatorOut}")
#
#	# Name of the rule
#	set(ruleName "standaloneNatives_${systemNormal}_${archNormal}")
#
#	# Progress indication
#	message(STATUS "Configuring NanoCoat Core "
#		"${systemNormal}/${archNormal}...")
#
#	# Which arguments to use?
#	set(va "-DXXSJMEVAXX=1")
#	set(vb "-DXXSJMEVBXX=1")
#	set(vc "-DXXSJMEVCXX=1")
#	set(vd "-DXXSJMEVDXX=1")
#	squirreljme_compiler_cmake_args(va vb vc vd
#		"${systemNormal}" "${archNormal}")
#
#	# Possibly broken configure?
#	if(EXISTS "${coreBuild}/CMakeCache.txt" AND
#		(NOT EXISTS "${coreBuild}/arch__.tgt" OR
#		NOT EXISTS "${coreBuild}/system.tgt"))
#		file(REMOVE_RECURSE "${coreBuild}")
#	endif()
#
#	# Configure CMake build for NanoCoat Core
#	if(EXISTS "${coreBuild}/CMakeCache.txt" AND
#		EXISTS "${coreBuild}/arch__.tgt" AND
#		EXISTS "${coreBuild}/system.tgt")
#		set(coreResult 0)
#	else()
#		file(MAKE_DIRECTORY "${coreBuild}")
#		execute_process(COMMAND "${CMAKE_COMMAND}"
#			"${va}" "${vb}" "${vc}" "${vd}"
#			"-DSQUIRRELJME_EMULATOR_BASE_IMPORT_DIR=${coreOut}"
#			"-DSQUIRRELJME_BINARY_OUTPUT_DIR=${coreOut}"
#			"-DSQUIRRELJME_DYLIB_OUTPUT_DIR=${coreOut}"
#			"-B" "${coreBuild}"
#			"-S" "${CMAKE_SOURCE_DIR}/nanocoat"
#			RESULT_VARIABLE coreResult
#			OUTPUT_FILE "${CMAKE_BINARY_DIR}/${ruleName}.core.out"
#			ERROR_FILE "${CMAKE_BINARY_DIR}/${ruleName}.core.err")
#	endif()
#
#	# Configure CMake build for libEmulatorBase
#	if("${coreResult}" EQUAL "0")
#		# Progress indication
#		message(STATUS "Configuring libEmulatorBase "
#			"${systemNormal}/${archNormal}...")
#
#		# Possibly broken configure?
#		if(EXISTS "${emulatorBuild}/CMakeCache.txt" AND
#			(NOT EXISTS "${emulatorBuild}/arch__.tgt" OR
#			NOT EXISTS "${emulatorBuild}/system.tgt"))
#			file(REMOVE_RECURSE "${emulatorBuild}")
#		endif()
#
#		# Now do the configure for emulator-base
#		if(EXISTS "${emulatorBuild}/CMakeCache.txt" AND
#			EXISTS "${emulatorBuild}/arch__.tgt" AND
#			EXISTS "${emulatorBuild}/system.tgt")
#			set(emulatorResult 0)
#		else()
#			file(MAKE_DIRECTORY "${emulatorBuild}")
#			execute_process(COMMAND "${CMAKE_COMMAND}"
#				"${va}" "${vb}" "${vc}" "${vd}"
#				"-DSQUIRRELJME_EMULATOR_BASE_IMPORT_DIR=${coreOut}"
#				"-DSQUIRRELJME_BINARY_OUTPUT_DIR=${emulatorOut}"
#				"-DSQUIRRELJME_DYLIB_OUTPUT_DIR=${emulatorOut}"
#				"-B" "${emulatorBuild}"
#				"-S" "${CMAKE_SOURCE_DIR}/emulators/emulator-base-native"
#				RESULT_VARIABLE emulatorResult
#				OUTPUT_FILE "${CMAKE_BINARY_DIR}/${ruleName}.emulator.out"
#				ERROR_FILE "${CMAKE_BINARY_DIR}/${ruleName}.emulator.err")
#		endif()
#	else()
#		set(emulatorResult "1")
#	endif()
#
#	# Was this successful?
#	if("${coreResult}" EQUAL "0" AND
#		"${emulatorResult}" EQUAL "0")
#		# Add target which builds the natives
#		add_custom_target(${ruleName}
#			COMMAND "${CMAKE_COMMAND}"
#				"--build" "${coreBuild}"
#				"--target" "BaseStatic" "libJvmDyLib"
#					"ScritchUI" "ScritchAudio"
#			COMMAND "${CMAKE_COMMAND}"
#				"--build" "${emulatorBuild}"
#				"--target" "libEmulatorBase"
#			COMMAND_EXPAND_LISTS)
#
#		# Add note for the rule that was generated
#		message(STATUS "Standalone Native "
#			"${systemNormal}/${archNormal} -> ${ruleName}")
#
#		# Add this rule to the standalone set
#		list(APPEND SQUIRRELJME_JAR_NATIVE_RULES
#			"${ruleName}")
#		set(SQUIRRELJME_JAR_RULE_${systemNormal}_${archNormal}
#			"${ruleName}")
#
#		# Set the emulator native path
#		set_target_properties(${ruleName}
#			PROPERTIES
#			SQUIRRELJME_CORE_NATIVE_PATH "${coreOut}"
#			SQUIRRELJME_EMULATOR_NATIVE_PATH "${emulatorOut}"
#			SQUIRRELJME_SYSTEM "${systemNormal}"
#			SQUIRRELJME_ARCH "${archNormal}"
#			ADDITIONAL_CLEAN_FILES
#				"${coreBuild};${coreOut};${emulatorBuild};${emulatorOut}")
#
#		# Package these
#		squirreljme_package_natives(${ruleName})
#	else()
#		# Progress indication
#		message(STATUS "Failed to configure "
#			"${systemNormal}/${archNormal}: "
#			"${coreResult} ${emulatorResult}!")
#
#		# Core?
#		if(EXISTS "${CMAKE_BINARY_DIR}/${ruleName}.core.err")
#			file(STRINGS "${CMAKE_BINARY_DIR}/${ruleName}.core.err" coreErr)
#			message(WARNING ${coreErr})
#		endif()
#
#		# Emulator?
#		if(EXISTS "${CMAKE_BINARY_DIR}/${ruleName}.emulator.err")
#			file(STRINGS "${CMAKE_BINARY_DIR}/${ruleName}.emulator.err" emuErr)
#			message(WARNING ${emuErr})
#		endif()
#	endif()
#endmacro()
#
#if(Fossil_EXECUTABLE)
#	# Pull in natives available via Fossil, which are prebuild
#	macro(squirreljme_natives_via_fossil)
#	endmacro()
#endif()
#
## Build natives for every known compiler on the system
#foreach(compilerMap IN LISTS SQUIRRELJME_COMPILER_MAP)
#	squirreljme_natives_via_compiler(${compilerMap})
#endforeach()
#
#if(Fossil_EXECUTABLE)
#	# There may be pre-packaged natives in Fossil
#	# unstable/0.3.0/natives-${systemNormal}-${archNormal}.zip
#	# unstable/0.3.0/natives-${systemNormal}-${archNormal}.zip.mkd
#	set(SQUIRRELJME_PREMADE_NATIVES)
#	list(APPEND SQUIRRELJME_PREMADE_NATIVES
#		"linux!amd64"
#		"linux!arm64l"
#		"linux!ia32"
#		"linux!mips32b"
#		"linux!mips32b6"
#		"linux!mips32l"
#		"linux!mips32l6"
#		"linux!mips64b"
#		"linux!mips64b6"
#		"linux!mips64l"
#		"linux!mips64l6"
#		"linux!powerpc32b"
#		"linux!powerpc64l"
#		"linux!riscv64"
#		"macosx!arm64l"
#		"macosx!amd64l"
#		"macosx!ia32"
#		"macosx!powerpc32b"
#		"windows!amd64"
#		"windows!ia32")
#
#	# Go through premade natives
#	foreach(premade IN LISTS SQUIRRELJME_PREMADE_NATIVES)
#		# Obtain back the system and architecture
#		squirreljme_unmap(systemNormal 0 "${premade}")
#		squirreljme_unmap(archNormal 1 "${premade}")
#
#		# If we can compile a premade for a system, do not use it
#		if(NOT "${SQUIRRELJME_JAR_RULE_${systemNormal}_${archNormal}}"
#			STREQUAL "")
#			message(STATUS "Not using Fossil ${systemNormal}/${archNormal} "
#				"as it is being built natively.")
#			continue()
#		endif()
#
#		# Determine the fossil path
#		set(fossilUvPath
#			"${SQUIRRELJME_UV_DIR}/natives-${systemNormal}-${archNormal}.zip")
#		set(downloadPath
#			"${CMAKE_BINARY_DIR}/natives-${systemNormal}-${archNormal}.zip")
#
#		# Try downloading it
#		execute_process(
#			COMMAND "${Fossil_EXECUTABLE}"
#				"uv" "cat" "${fossilUvPath}"
#			OUTPUT_FILE "${downloadPath}"
#			WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
#			RESULT_VARIABLE downloadResult)
#
#		# Empty or command failed?
#		file(SIZE "${downloadPath}" downloadSize)
#		if(NOT "${downloadResult}" EQUAL "0" OR
#			NOT EXISTS "${downloadPath}" OR
#			"${downloadSize}" EQUAL "0")
#			# Notice
#			message(STATUS "Native via Fossil ${systemNormal}/${archNormal} "
#				"is not available, skipping.")
#
#			# Remove it
#			file(REMOVE "${downloadPath}")
#
#			# Try another
#			continue()
#		endif()
#
#		# Note that it is available
#		message(STATUS "Downloaded natives ${systemNormal}/${archNormal} "
#			"via Fossil.")
#	endforeach()
#endif()

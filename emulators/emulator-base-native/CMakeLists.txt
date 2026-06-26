# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake Helper for project

# Use the best version possible
# Otherwise, fallback to the lowest viable version
if(CMAKE_VERSION VERSION_GREATER_EQUAL 3.13)
	cmake_minimum_required(VERSION 3.13..3.31)
else()
	cmake_minimum_required(VERSION 3.0)
endif()

# Specify NanoCoat directory for includes
set(SQUIRRELJME_ROOT_DIR "${CMAKE_SOURCE_DIR}/../..")
set(SQUIRRELJME_NANOCOAT_SOURCE_DIR "${SQUIRRELJME_ROOT_DIR}/nanocoat")
include("${SQUIRRELJME_NANOCOAT_SOURCE_DIR}/cmake/easier-includes.cmake")

# Include backports
squirreljme_include_nanocoat("cmake-bp.cmake")

# External projects are needed
include(ExternalProject)

# Declare project
project(libEmulatorBase
	LANGUAGES C)

# Settings that adjust the build completely
squirreljme_include_nanocoat("environment-options.cmake")

# We want to forward as many CMAKE_ commands to the target NanoCoat as
# possible
get_cmake_property(allVariables VARIABLES)
set(cacheFile "${CMAKE_BINARY_DIR}/forward-cache.cmake")
file(REMOVE "${cacheFile}")
file(TOUCH "${cacheFile}")
foreach(var IN LISTS allVariables)
	# Only consider CMAKE_ specific variables
	# Also SQUIRRELJME_, because anything might be desired to be passed through
	string(FIND "${var}" "CMAKE_" foundAt)
	string(FIND "${var}" "SQUIRRELJME_" squirreljmeAt)
	if(NOT "${foundAt}" EQUAL "0" AND
		NOT "${squirreljmeAt}" EQUAL "0")
		continue()
	endif()

	# Quote the value, otherwise things will very much break
	string(REPLACE "\\" "\\\\" quotedValue "${${var}}")
	string(REPLACE "\"" "\\\"" quotedValue "${${var}}")

	# Ignore if blank
	if("${quotedValue}" STREQUAL "")
		continue()
	endif()

	# Specifically skip these
	# Ignore host compiler information, we do not care for that
	# Any Compiler ID information is often used internally by CMake
	# Having compiler IDs breaks things completely, there are also suffix
	# forms of them that need to go away as well
	# Executable and DyLib prefix/suffix are instead set by _LANG!
	# Ignore anything that indicates CMake is done with something
	# Ignore internals
	# Ignore versions
	# Any compiler works will ignore any CMake checks, which breaks system
	# roots and otherwise
	string(FIND "${var}" "CMAKE_ASM_COMPILER_ID_" asmCompilerIdAt)
	string(FIND "${var}" "CMAKE_C_COMPILER_ID_" cCompilerIdAt)
	string(FIND "${var}" "CMAKE_CXX_COMPILER_ID_" cxxCompilerIdAt)
	string(FIND "${var}" "CMAKE_OBJC_COMPILER_ID_" objCCompilerIdAt)
	string(FIND "${var}" "CMAKE_ASM_HOST_" asmHostAt)
	string(FIND "${var}" "CMAKE_C_HOST_" cHostAt)
	string(FIND "${var}" "CMAKE_CXX_HOST_" cxxHostAt)
	string(FIND "${var}" "CMAKE_OBJC_HOST_" objCHostAt)
	string(FIND "${var}" "_LOADED" loadedAt REVERSE)
	string(FIND "${var}" "_INTERNAL" internalAt REVERSE)
	string(FIND "${var}" "_VERSION" versionAt REVERSE)
	string(FIND "${var}" "_ID" idAt REVERSE)
	string(FIND "${var}" "_COMPILER_WORKS" compilerWorksAt REVERSE)
	string(FIND "${var}" "_COMPILER_PRODUCED_OUTPUT" compilerProduced REVERSE)
	if("${asmCompilerIdAt}" EQUAL "0" OR
		"${cCompilerIdAt}" EQUAL "0" OR
		"${cxxCompilerIdAt}" EQUAL "0" OR
		"${objCCompilerIdAt}" EQUAL "0" OR
		"${asmHostAt}" EQUAL "0" OR
		"${cHostAt}" EQUAL "0" OR
		"${cxxHostAt}" EQUAL "0" OR
		"${objCHostAt}" EQUAL "0" OR
		"${var}" STREQUAL "__TAILS__" OR
		"${loadedAt}" GREATER "0" OR
		"${internalAt}" GREATER "0" OR
		"${versionAt}" GREATER "0" OR
		"${idAt}" GREATER "0" OR
		"${compilerWorksAt}" GREATER "0" OR
		"${compilerProduced}" GREATER "0" OR
		"${var}" STREQUAL "CMAKE_SHARED_LIBRARY_PREFIX" OR
		"${var}" STREQUAL "CMAKE_SHARED_LIBRARY_SUFFIX" OR
		"${var}" STREQUAL "CMAKE_EXECUTABLE_PREFIX" OR
		"${var}" STREQUAL "CMAKE_EXECUTABLE_SUFFIX" OR
		"${var}" STREQUAL "CMAKE_C_ABI_COMPILED" OR
		"${var}" STREQUAL "__END_OF_EVERYTHING__")
		continue()
	endif()

	# Find specific variables
	string(FIND "${var}" "CMAKE_ASM_" asmAt)
	string(FIND "${var}" "CMAKE_BUILD_" buildAt)
	string(FIND "${var}" "CMAKE_C_" cAt)
	string(FIND "${var}" "CMAKE_CXX_" cxxAt)
	string(FIND "${var}" "CMAKE_DEPFILE_" depFileAt)
	string(FIND "${var}" "CMAKE_DL_" dlAt)
	string(FIND "${var}" "CMAKE_EXECUTABLE_" executableAt)
	string(FIND "${var}" "CMAKE_EXE_" exeAt)
	string(FIND "${var}" "CMAKE_GENERATOR_" genAt)
	string(FIND "${var}" "CMAKE_INSTALL_" installAt)
	string(FIND "${var}" "CMAKE_LINK_" linkAt)
	string(FIND "${var}" "CMAKE_MODULE_" moduleAt)
	string(FIND "${var}" "CMAKE_OBJC_" objcAt)
	string(FIND "${var}" "CMAKE_SHARED_" sharedAt)
	string(FIND "${var}" "CMAKE_SKIP_" skipAt)
	string(FIND "${var}" "CMAKE_STATIC_" staticAt)
	string(FIND "${var}" "SQUIRRELJME_" squirreljmeAt)
	if("${asmAt}" EQUAL "0" OR
		"${buildAt}" EQUAL "0" OR
		"${cAt}" EQUAL "0" OR
		"${cxxAt}" EQUAL "0" OR
		"${depFileAt}" EQUAL "0" OR
		"${dlAt}" EQUAL "0" OR
		"${executableAt}" EQUAL "0" OR
		"${exeAt}" EQUAL "0" OR
		"${genAt}" EQUAL "0" OR
		"${installAt}" EQUAL "0" OR
		"${linkAt}" EQUAL "0" OR
		"${moduleAt}" EQUAL "0" OR
		"${objcAt}" EQUAL "0" OR
		"${sharedAt}" EQUAL "0" OR
		"${skipAt}" EQUAL "0" OR
		"${staticAt}" EQUAL "0" OR
		"${squirreljmeAt}" EQUAL "0" OR
		"${var}" STREQUAL "CMAKE_ADDR2LINE" OR
		"${var}" STREQUAL "CMAKE_AR" OR
		"${var}" STREQUAL "CMAKE_DLLTOOL" OR
		"${var}" STREQUAL "CMAKE_LINKER" OR
		"${var}" STREQUAL "CMAKE_MAKE_PROGRAM" OR
		"${var}" STREQUAL "CMAKE_MT" OR
		"${var}" STREQUAL "CMAKE_NM" OR
		"${var}" STREQUAL "CMAKE_OBJCOPY" OR
		"${var}" STREQUAL "CMAKE_OBJDUMP" OR
		"${var}" STREQUAL "CMAKE_RANLIB" OR
		"${var}" STREQUAL "CMAKE_READELF" OR
		"${var}" STREQUAL "CMAKE_STRIP" OR
		"${var}" STREQUAL "CMAKE_TAPI" OR
		"${var}" STREQUAL "CMAKE_VERBOSE_MAKEFILE" OR
		"${var}" STREQUAL "__END_OF_EVERYTHING__")
		# Get the variable type
		if(IS_DIRECTORY "${quotedValue}")
			set(varType "PATH")
		elseif(EXISTS "${quotedValue}")
			set(varType "FILEPATH")
		elseif("${quotedValue}" STREQUAL "ON" OR
			"${quotedValue}" STREQUAL "YES" OR
			"${quotedValue}" STREQUAL "TRUE" OR
			"${quotedValue}" STREQUAL "Y" OR
			"${quotedValue}" STREQUAL "OFF" OR
			"${quotedValue}" STREQUAL "NO" OR
			"${quotedValue}" STREQUAL "FALSE" OR
			"${quotedValue}" STREQUAL "N" OR
			"${quotedValue}" STREQUAL "IGNORE" OR
			"${quotedValue}" STREQUAL "NOTFOUND")
			set(varType "BOOL")
		else()
			set(varType "STRING")
		endif()

		# Append to the file
		file(APPEND "${cacheFile}"
			"set(\"${var}\" \"${quotedValue}\" CACHE ${varType} "${var}")\n")
	endif()
endforeach()

# Determine options to forward
squirreljme_env_forward(forwardedEnv)
message("Forwarded options: ${forwardedEnv}")

# Setup NanoCoat Project
set(SQUIRRELJME_NANOCOAT_BINARY_DIR "${CMAKE_BINARY_DIR}/nanocoat")
file(MAKE_DIRECTORY "${SQUIRRELJME_NANOCOAT_BINARY_DIR}")
ExternalProject_Add(SquirrelJME
	BINARY_DIR "${SQUIRRELJME_NANOCOAT_BINARY_DIR}"
	SOURCE_DIR "${SQUIRRELJME_NANOCOAT_SOURCE_DIR}"
	CMAKE_CACHE_ARGS allDefines
	CONFIGURE_COMMAND "${CMAKE_COMMAND}"
		"-C" "${cacheFile}"
		"-G" "${CMAKE_GENERATOR}"
		"-T" "${CMAKE_GENERATOR_TOOLSET}"
		"-A" "${CMAKE_GENERATOR_PLATFORM}"
		"-S" "${SQUIRRELJME_NANOCOAT_SOURCE_DIR}"
		"-B" "${SQUIRRELJME_NANOCOAT_BINARY_DIR}"
		"${forwardedEnv}"
	BUILD_COMMAND ""
	INSTALL_COMMAND ""
	TEST_COMMAND ""
	TEST_EXCLUDE_FROM_MAIN YES
#	STEP_TARGETS RunConfigureStep BaseStatic libJvmDyLib SquirrelJME
#		ScritchAudio ScritchUi
	CONFIGURE_HANDLED_BY_BUILD NO)

# Configure step, since it seems to be missing
# Cannot be called Configure because build systems with case insensitive rules
# will clash with built-in "configure"
ExternalProject_Add_Step(SquirrelJME RunConfigureStep
	COMMENT "Configuring NanoCoat..."
	COMMAND "${CMAKE_COMMAND}"
		"-C" "${cacheFile}"
		"-G" "${CMAKE_GENERATOR}"
		"-T" "${CMAKE_GENERATOR_TOOLSET}"
		"-A" "${CMAKE_GENERATOR_PLATFORM}"
		"-S" "${SQUIRRELJME_NANOCOAT_SOURCE_DIR}"
		"-B" "${SQUIRRELJME_NANOCOAT_BINARY_DIR}"
		"${forwardedEnv}"
	INDEPENDENT)
ExternalProject_Add_StepTargets(SquirrelJME RunConfigureStep
	SquirrelJME-RunConfigureStep)

# Additional clean files for the configuration step
if(TARGET SquirrelJME-RunConfigureStep)
	set_target_properties(SquirrelJME-RunConfigureStep PROPERTIES
		ADDITIONAL_CLEAN_FILES "${SQUIRRELJME_NANOCOAT_BINARY_DIR}")
endif()

# Add macro since these are all the same
macro(squirreljme_external_step target)
	ExternalProject_Add_Step(SquirrelJME ${target}
		COMMENT "Building ${target}..."
		COMMAND "${CMAKE_COMMAND}"
			"--build" "${SQUIRRELJME_NANOCOAT_BINARY_DIR}"
			"--target" ${target}
		INDEPENDENT)
	ExternalProject_Add_StepTargets(SquirrelJME ${target}
		SquirrelJME-${target})
endmacro()

# Add steps
squirreljme_external_step(BaseStatic)
squirreljme_external_step(ScritchAudio)
squirreljme_external_step(ScritchUi)
squirreljme_external_step(libJvmDyLib)
squirreljme_external_step(SquirrelJME)

# Correct dependencies
ExternalProject_Add_StepDependencies(SquirrelJME BaseStatic
	SquirrelJME-RunConfigureStep)
ExternalProject_Add_StepDependencies(SquirrelJME ScritchAudio
	SquirrelJME-BaseStatic)
ExternalProject_Add_StepDependencies(SquirrelJME ScritchUi
	SquirrelJME-BaseStatic)
ExternalProject_Add_StepDependencies(SquirrelJME libJvmDyLib
	SquirrelJME-BaseStatic SquirrelJME-ScritchAudio SquirrelJME-ScritchUi)
ExternalProject_Add_StepDependencies(SquirrelJME SquirrelJME
	SquirrelJME-libJvmDyLib)

# Support bases
squirreljme_include_nanocoat("cmake-patch.cmake")
squirreljme_include_nanocoat("pre-fixes.cmake")
squirreljme_include_nanocoat("system-map.cmake")
squirreljme_include_nanocoat("fixes.cmake")
squirreljme_include_nanocoat("multilib.cmake")

# Declare library
add_library(libEmulatorBase SHARED
	c/jniHelper.c
	c/mle_audiostream.c
	c/mle_bucket.c
	c/mle_debug.c
	c/mle_jar.c
	c/mle_math.c
	c/mle_mathaccel.c
	c/mle_midi.c
	c/mle_nativearchive.c
	c/mle_object.c
	c/mle_pencil.c
	c/mle_pencilfont.c
	c/mle_reflection.c
	c/mle_runtime.c
	c/mle_scritchui.c
	c/mle_scritchuibaseobject.c
	c/mle_scritchuicallback.c
	c/mle_scritchuidylib.c
	c/mle_task.c
	c/mle_terminal.c
	c/mle_thread.c
	c/mle_type.c
	c/nativebinding.c
	c/utils.c)

# The base static library is needed first
# Dependencies are handled by DEPENDERS, since this cannot be reliably called
# especially for plain makefiles
add_dependencies(libEmulatorBase
	SquirrelJME-BaseStatic
	SquirrelJME-libJvmDyLib)

# Always make this position independent
squirreljme_always_fpic(libEmulatorBase)

# Includes for the build
target_include_directories(libEmulatorBase PRIVATE
	"${CMAKE_SOURCE_DIR}/headers"
	"${SQUIRRELJME_NANOCOAT_SOURCE_DIR}/include"
	"${SQUIRRELJME_NANOCOAT_SOURCE_DIR}/include/3rdparty/jni")

# Libraries for the build
target_link_directories(libEmulatorBase PRIVATE
	"${SQUIRRELJME_NANOCOAT_BINARY_DIR}/bin")
squirreljme_link_libraries_required(libEmulatorBase PUBLIC
	"${SQUIRRELJME_REQUIRED_LIBS}"
	BaseStatic
	jvm)

# It is easier to find this when it is in the build root
squirreljme_target_binary_name(libEmulatorBase
	"emulator-base")
squirreljme_target_binary_output(libEmulatorBase
	"${CMAKE_BINARY_DIR}/bin")

# Library Exports
squirreljme_shared_library_exports(libEmulatorBase)

# Where does the library exist?
squirreljme_library_dir(emulatorBaseDir libEmulatorBase)
squirreljme_library_path(emulatorBasePath libEmulatorBase)

# The ZIP goes into the emulator base directory
set(nativesZip
	"${emulatorBaseDir}/natives-${SQUIRRELJME_SYSTEM}-${SQUIRRELJME_ARCH}.zip")
set(nativesZipSimple
	"${emulatorBaseDir}/natives.zip")

# Determine target natives path and directories
set(nativesDir "${CMAKE_BINARY_DIR}/bundle/")
set(nativesSubDir
	"${nativesDir}/natives/${SQUIRRELJME_SYSTEM}/${SQUIRRELJME_ARCH}")

# Make sure it exists before we bundle
file(MAKE_DIRECTORY "${nativesSubDir}")

# Determine executable name
set(exeName "")
string(APPEND exeName
	"${CMAKE_EXECUTABLE_PREFIX_C}"
	"squirreljme"
	"${CMAKE_EXECUTABLE_SUFFIX_C}")
message(STATUS "NanoCoat should be called '${exeName}'...")

# Rule to bundle all of the natives together
add_custom_target(bundleNatives
	# Copy the spread out binaries
	COMMAND "${CMAKE_COMMAND}" "-P"
		"${CMAKE_SOURCE_DIR}/bundle-script.cmake" "--"
			"${SQUIRRELJME_NANOCOAT_SOURCE_DIR}"
			"${SQUIRRELJME_NANOCOAT_BINARY_DIR}"
			"${cacheFile}"
			"${nativesZip}"
			"${nativesZipSimple}"
			"${nativesDir}"
			"${nativesSubDir}"

			# Copy files
			"$<TARGET_FILE:libEmulatorBase>"
			"${SQUIRRELJME_NANOCOAT_BINARY_DIR}/bin/${exeName}"
	DEPENDS libEmulatorBase
	WORKING_DIRECTORY "${nativesDir}"
	BYPRODUCTS "${nativesZip}" "${nativesZipSimple}"
	COMMENT "Bundling natives..."
	COMMAND_EXPAND_LISTS)

# This is good to have in all
set_target_properties(bundleNatives PROPERTIES
	EXCLUDE_FROM_ALL NO)


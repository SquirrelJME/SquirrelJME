# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Defines the base project and the versioning info

# Echo commands accordingly
set(CMAKE_EXECUTE_PROCESS_COMMAND_ECHO STDERR)

# Debugging?
if(SQUIRRELJME_IS_DEBUG)
	add_compile_definitions(SJME_CONFIG_DEBUG=1)
endif()

# Where are we?
if(NOT DEFINED SQUIRRELJME_UTIL_CMAKE_WHERE)
	set(SQUIRRELJME_UTIL_CMAKE_WHERE "${CMAKE_CURRENT_LIST_DIR}")
endif()

if(NOT DEFINED SQUIRRELJME_UTIL_CMAKE_WHERE)
	set(SQUIRRELJME_UTIL_CMAKE_WHERE "${CMAKE_SOURCE_DIR}")
endif()

# The directory where the utilities should exist
get_filename_component(SQUIRRELJME_UTIL_SOURCE_DIR
	"${SQUIRRELJME_UTIL_CMAKE_WHERE}/utils" ABSOLUTE)
get_filename_component(SQUIRRELJME_UTIL_DIR
	"${CMAKE_BINARY_DIR}/utils" ABSOLUTE)

# Add macro to determine the path of a utility
macro(squirreljme_util var what)
	set(${var}
		"${SQUIRRELJME_UTIL_DIR}/${what}${SQUIRRELJME_HOST_EXE_SUFFIX}")
endmacro()

# If we are on Windows, we do want to pass our generator
message(STATUS "CMake Generator Platform: ${CMAKE_GENERATOR_PLATFORM}")
if("${CMAKE_HOST_SYSTEM_NAME}" STREQUAL "Windows")
	unset(SQUIRRELJME_SWITCH_CMAKE_GENERATOR_PLATFORM_UNSET)
	set(SQUIRRELJME_SWITCH_CMAKE_GENERATOR_PLATFORM_SET
		"-DCMAKE_GENERATOR_PLATFORM=${CMAKE_GENERATOR_PLATFORM}")
else()
	set(SQUIRRELJME_SWITCH_CMAKE_GENERATOR_PLATFORM_UNSET
		"--unset=CMAKE_GENERATOR_PLATFORM")
	unset(SQUIRRELJME_SWITCH_CMAKE_GENERATOR_PLATFORM_SET)
endif()

# Need to cleanup any previous configuration run before building
if(EXISTS "${SQUIRRELJME_UTIL_DIR}/CMakeCache.txt" OR
	(EXISTS "${SQUIRRELJME_UTIL_DIR}" AND
		IS_DIRECTORY "${SQUIRRELJME_UTIL_DIR}"))
	file(REMOVE_RECURSE "${SQUIRRELJME_UTIL_DIR}/")
endif()

# Make sure the resultant utility directory exists
file(MAKE_DIRECTORY "${SQUIRRELJME_UTIL_DIR}")

# Try to find the host CMake as a first choice
find_program(SJME_FIRST_CMAKE "cmake" NO_DEFAULT_PATH)
if(NOT SJME_FIRST_CMAKE)
	find_program(SJME_FIRST_CMAKE "cmake")

	if(NOT SJME_FIRST_CMAKE)
		set(SJME_FIRST_CMAKE "${CMAKE_COMMAND}")
	endif()
endif()

# Double check version
execute_process(COMMAND "${SJME_FIRST_CMAKE}" "-version"
	OUTPUT_FILE "${CMAKE_BINARY_DIR}/first-cmake-version")
file(STRINGS "${CMAKE_BINARY_DIR}/first-cmake-version" SJME_FIRST_CMAKE_VER
	LIMIT_COUNT 1)
string(TOLOWER "${SJME_FIRST_CMAKE_VER}" SJME_FIRST_CMAKE_VER)
string(REPLACE "cmake version " ""
	SJME_FIRST_CMAKE_VER "${SJME_FIRST_CMAKE_VER}")

# If the CMake we found is too old, ignore it and use our current one
message(STATUS "CMake ${SJME_FIRST_CMAKE} is ${SJME_FIRST_CMAKE_VER}")
if("${SJME_FIRST_CMAKE_VER}" VERSION_LESS 3.0)
	# Note
	message(STATUS "Using ${CMAKE_COMMAND} as it is too old...")

	# Just use our version
	set(SJME_FIRST_CMAKE "${CMAKE_COMMAND}")
endif()

# Setup command to run
set(SJME_UTIL_CFG)
list(APPEND SJME_UTIL_CFG "${SJME_FIRST_CMAKE}")
list(APPEND SJME_UTIL_CFG "-E")
list(APPEND SJME_UTIL_CFG "env")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_TOOLCHAIN")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_TOOLCHAIN_FILE")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_SOURCE_ROOT")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_FRAMEWORK_PATH")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_INCLUDE_PATH")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_LIBRARY_PATH")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_PROGRAM_PATH")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_BUILD_TYPE")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_GENERATOR")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_GENERATOR_INSTANCE")
list(APPEND SJME_UTIL_CFG ${SQUIRRELJME_SWITCH_CMAKE_GENERATOR_PLATFORM_UNSET})
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_GENERATOR_TOOLSET")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_C_COMPILER_LAUNCHER")
list(APPEND SJME_UTIL_CFG "--unset=CMAKE_C_LINKER_LAUNCHER")
list(APPEND SJME_UTIL_CFG "--unset=CC")
list(APPEND SJME_UTIL_CFG "--unset=CXX")
list(APPEND SJME_UTIL_CFG "--unset=CFLAGS")
list(APPEND SJME_UTIL_CFG "--unset=CXXFLAGS")
list(APPEND SJME_UTIL_CFG "--unset=LDFLAGS")

# Target CMake Command
list(APPEND SJME_UTIL_CFG "${SJME_FIRST_CMAKE}")
list(APPEND SJME_UTIL_CFG "-DCMAKE_BUILD_TYPE=Debug")
list(APPEND SJME_UTIL_CFG "-DCMAKE_SYSTEM_NAME=${CMAKE_HOST_SYSTEM_NAME}")
list(APPEND SJME_UTIL_CFG
	"-DCMAKE_SYSTEM_PROCESSOR=${CMAKE_HOST_SYSTEM_PROCESSOR}")
list(APPEND SJME_UTIL_CFG ${SQUIRRELJME_SWITCH_CMAKE_GENERATOR_PLATFORM_SET})

if("${CMAKE_VERSION}" VERSION_GREATER_EQUAL "3.13" AND
	"${SJME_FIRST_CMAKE_VER}"  VERSION_GREATER_EQUAL "3.13")
	list(APPEND SJME_UTIL_CFG "-S" "${SQUIRRELJME_UTIL_SOURCE_DIR}")
	list(APPEND SJME_UTIL_CFG "-B" "${SQUIRRELJME_UTIL_DIR}")
else()
	list(APPEND SJME_UTIL_CFG "${SQUIRRELJME_UTIL_SOURCE_DIR}")
endif()

# Note
message(STATUS "Bootstrapping utils into "
	"${SQUIRRELJME_UTIL_DIR}...")
message(STATUS "Current generator is ${CMAKE_GENERATOR}...")

# Divider
message(STATUS "CONFIGURE: -------------------------------------------------")

# Run Configuration
execute_process(
	COMMAND ${SJME_UTIL_CFG}
	WORKING_DIRECTORY "${SQUIRRELJME_UTIL_DIR}"
	RESULT_VARIABLE SJME_UTIL_CFG_RESULT)

# Configure Failed?
if(NOT SJME_UTIL_CFG_RESULT EQUAL 0)
	message(FATAL_ERROR "Configure failed with: ${SJME_UTIL_CFG_RESULT}")
endif()

# Divider
message(STATUS "BUILD: -----------------------------------------------------")

# Run build step
execute_process(
	COMMAND "${SJME_FIRST_CMAKE}"
		"--build" "${SQUIRRELJME_UTIL_DIR}"
	RESULT_VARIABLE SJME_UTIL_BLD_RESULT
	WORKING_DIRECTORY "${SQUIRRELJME_UTIL_DIR}")

# Build Failed?
if(NOT SJME_UTIL_BLD_RESULT EQUAL 0)
	message(FATAL_ERROR "Build failed with: ${SJME_UTIL_BLD_RESULT}")
endif()

# Divider
message(STATUS "PREFIX/SUFFIX: ---------------------------------------------")

# Determine executable suffix
if(EXISTS "${SQUIRRELJME_UTIL_DIR}/suffix")
	file(STRINGS "${SQUIRRELJME_UTIL_DIR}/suffix"
		SQUIRRELJME_HOST_EXE_SUFFIX)
	message(DEBUG "Host executable suffix is "
		"'${SQUIRRELJME_HOST_EXE_SUFFIX}'.")
endif()

# Determine dynamic library prefix
if(EXISTS "${SQUIRRELJME_UTIL_DIR}/dylibprefix")
	file(STRINGS "${SQUIRRELJME_UTIL_DIR}/dylibprefix"
		SQUIRRELJME_HOST_DYLIB_PREFIX)
	message(DEBUG "Host library prefix is "
		"'${SQUIRRELJME_HOST_DYLIB_PREFIX}'.")
endif()

# Determine dynamic library suffix
if(EXISTS "${SQUIRRELJME_UTIL_DIR}/dylibsuffix")
	file(STRINGS "${SQUIRRELJME_UTIL_DIR}/dylibsuffix"
		SQUIRRELJME_HOST_DYLIB_SUFFIX)
	message(DEBUG "Host library suffix is "
		"'${SQUIRRELJME_HOST_DYLIB_SUFFIX}'.")
endif()

# Divider
message(STATUS "SIMPLE CHECK: ----------------------------------------------")

# Try running simple utility to make sure it compiled under the host
squirreljme_util(SJME_UTIL_SIMPLE simple)
execute_process(COMMAND "${SJME_UTIL_SIMPLE}"
	RESULT_VARIABLE SJME_UTIL_CHK_RESULT
	WORKING_DIRECTORY "${SQUIRRELJME_UTIL_DIR}")

# Make sure it is actually valid
if(NOT SJME_UTIL_CHK_RESULT EQUAL 0)
	message(FATAL_ERROR "Simple run failed with: ${SJME_UTIL_CHK_RESULT}")
endif()

# Divider
message(STATUS "------------------------------------------------------------")

# ----------------------------------------------------------------------------

# Checks if a given file is out of date according to a checksum
function(squirreljme_check_file_checksum upToDateVar
	inputFile outputPath)
	# Get hash of input file
	file(SHA1 "${inputFile}" cacheHash)

	# Get last checksum, if any
	if(EXISTS "${outputPath}.checksum")
		file(STRINGS "${outputPath}.checksum"
			cacheHashLast)
	else()
		set(cacheHashLast "")
	endif()

	# Is the checksum out of date?
	if(NOT EXISTS "${outputPath}.checksum" OR
		NOT EXISTS "${outputPath}" OR
		NOT "${cacheHash}" STREQUAL "${cacheHashLast}")
		set(${upToDateVar} 0 PARENT_SCOPE)
	else()
		set(${upToDateVar} 1 PARENT_SCOPE)
	endif()
endfunction()

# Writes the checksum of the input file to the output
function(squirreljme_write_file_checksum inputFile
	outputPath)
	# Get hash of input file
	file(SHA1 "${inputFile}" cacheHash)

	# Store checksum
	file(WRITE "${outputPath}.checksum"
		"${cacheHash}")
endfunction()

# Decodes the given file
function(squirreljme_decode_file how
	inputPath outputPath)
	# Should be HEX or BASE64
	if(NOT how STREQUAL "HEX" AND
		NOT how STREQUAL "BASE64")
		message(FATAL_ERROR "squirreljme_decode_file() takes "
			"either HEX or BASE64")
	endif()

	# Where is the decoder?
	squirreljme_util(decodeExePath decode)

	# Run the command
	execute_process(COMMAND "${decodeExePath}" "${how}"
		INPUT_FILE "${inputPath}"
		OUTPUT_FILE "${outputPath}"
		RESULT_VARIABLE conversionExitCode
		TIMEOUT 16)

	# Failed
	if(conversionExitCode)
		message(FATAL_ERROR
			"Conversion failed: ${conversionExitCode}.")
	endif()
endfunction()

# Decodes a directory of encoded files
function(squirreljme_decode_dir inputDir outputDir)
	# Decode all files accordingly
	file(GLOB inFiles
		"${inputDir}/*.__hex"
		"${inputDir}/*.__mime")
	foreach(inFile ${inFiles})
		# Determine the base name of the file
		get_filename_component(baseName
			"${inFile}" NAME)

		# Is this Hex or MIME?
		string(FIND "${baseName}" ".__hex"
			isHexFile)

		# Remove extension from it
		string(REPLACE ".__hex" ""
			baseName
			"${baseName}")
		string(REPLACE ".__mime" ""
			baseName
			"${baseName}")

		# Make sure the target directory exists first
		file(MAKE_DIRECTORY "${outputDir}")

		# Determine input and output
		get_filename_component(inFileAbs
			"${inFile}" ABSOLUTE)
		get_filename_component(outFileAbsPath
			"${outputDir}/${baseName}" ABSOLUTE)

		# Check if file is up to date
		squirreljme_check_file_checksum(upToDate
			"${inFileAbs}" "${outFileAbsPath}")

		# Does decoding need to be rerun?
		if(NOT upToDate)
			# Run decoding sequence
			message(STATUS
				"Decoding ${inFileAbs} to "
				"${outFileAbsPath}...")
			file(REMOVE "${outFileAbsPath}")
			if(isHexFile LESS 0)
				squirreljme_decode_file(BASE64
					"${inFileAbs}" "${outFileAbsPath}")
			else()
				squirreljme_decode_file(HEX
					"${inFileAbs}" "${outFileAbsPath}")
			endif()

			# Store checksum
			squirreljme_write_file_checksum(
				"${inFileAbs}" "${outFileAbsPath}")
		else()
			message(STATUS
				"File ${outFileAbsPath} already decoded...")
		endif()
	endforeach()
endfunction()

# Sourceize a single file
function(squirreljme_sourceize_file inputPath
	outputCPath outputHPath)
	# Get the base name of the input file
	get_filename_component(inputPathBaseName
		"${inputPath}" NAME)

	# Where is the encoder?
	squirreljme_util(sourceizeExePath sourceize)

	# Run the command
	execute_process(COMMAND "${sourceizeExePath}"
			"${inputPathBaseName}" "C"
		INPUT_FILE "${inputPath}"
		OUTPUT_FILE "${outputCPath}"
		RESULT_VARIABLE sourceizeExitCode
		TIMEOUT 16)
	execute_process(COMMAND "${sourceizeExePath}"
		"${inputPathBaseName}" "H"
		INPUT_FILE "${inputPath}"
		OUTPUT_FILE "${outputHPath}"
		RESULT_VARIABLE sourceizeExitCode
		TIMEOUT 16)

	# Failed
	if(sourceizeExitCode)
		message(FATAL_ERROR
			"Sourceize failed: ${sourceizeExitCode}.")
	endif()
endfunction()

# Sourceize an entire directory
function(squirreljme_sourceize_dir inputDir outputDir)
	# Encode all file accordingly
	file(GLOB inFiles "${inputDir}/*")
	foreach(inFile ${inFiles})
		# Determine the base name of the file
		get_filename_component(baseName
			"${inFile}" NAME)

		# Make sure the target directory exists first
		file(MAKE_DIRECTORY "${outputDir}")

		# Determine input and output
		get_filename_component(inFileAbs
			"${inFile}" ABSOLUTE)
		get_filename_component(outFileAbsPath
			"${outputDir}/${baseName}" ABSOLUTE)

		# Check if source file is up to date
		squirreljme_check_file_checksum(upToDate
			"${inFileAbs}" "${outFileAbsPath}.c")
		if(upToDate)
			# Do the same for the header file
			squirreljme_check_file_checksum(upToDate
				"${inFileAbs}" "${outFileAbsPath}.h")
		endif()

		# Does decoding need to be rerun?
		if(NOT upToDate)
			# Run decoding sequence
			message(STATUS
				"Sourceizing ${inFileAbs} to "
				"${outFileAbsPath}.[ch]...")
			file(REMOVE "${outFileAbsPath}.c")
			file(REMOVE "${outFileAbsPath}.h")
			squirreljme_sourceize_file("${inFileAbs}"
				"${outFileAbsPath}.c" "${outFileAbsPath}.h")

			# Store checksum
			squirreljme_write_file_checksum(
				"${inFileAbs}" "${outFileAbsPath}.c")
			squirreljme_write_file_checksum(
				"${inFileAbs}" "${outFileAbsPath}.h")
		else()
			message(STATUS
				"File ${outFileAbsPath}.c already sourceized...")
		endif()
	endforeach()
endfunction()

# Set variable for dynamic library import
macro(squirreljme_library_set var target)
	if(MSVC)
		set(${var}
			"${SQUIRRELJME_UTIL_DIR}/${SQUIRRELJME_HOST_DYLIB_PREFIX}${target}.lib")
	else()
		set(${var}
			"${SQUIRRELJME_UTIL_DIR}/${SQUIRRELJME_HOST_DYLIB_PREFIX}${target}${SQUIRRELJME_HOST_DYLIB_SUFFIX}")
	endif()
endmacro()

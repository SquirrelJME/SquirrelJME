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

# The directory where the utilities should exist
get_filename_component(SQUIRRELJME_UTIL_SOURCE_DIR
	"${CMAKE_SOURCE_DIR}/cmake/utils" ABSOLUTE)
get_filename_component(SQUIRRELJME_UTIL_DIR
	"${CMAKE_BINARY_DIR}/utils" ABSOLUTE)

# Add macro to determine the path of a utility
macro(squirreljme_util var what)
	set(${var}
		"${SQUIRRELJME_UTIL_DIR}/${what}${SQUIRRELJME_HOST_EXE_SUFFIX}")
endmacro()

# Only run this if the directory does not exist, because there might be a
# cache from a previous run?
if(NOT EXISTS "${SQUIRRELJME_UTIL_DIR}" OR
	NOT EXISTS "${SQUIRRELJME_UTIL_DIR}/CMakeCache.txt")
	# Make sure the directory exists
	file(MAKE_DIRECTORY "${SQUIRRELJME_UTIL_DIR}")

	# Emscripten breaks here, so do not use it with nested CMake
	# Also nested CMake breaks here as well
	if(EMSCRIPTEN OR SQUIRRELJME_CROSS_BUILD)
		set(cmakeUtilConfigResult 1)
	else()
		# Note
		message(STATUS "Bootstrapping utils into "
			"${SQUIRRELJME_UTIL_DIR}...")
		message(STATUS "Current generator is ${CMAKE_GENERATOR}...")

		# Run nested CMake to build the utilities
		if(${CMAKE_VERSION} VERSION_GREATER_EQUAL "3.13")
			# CMake 3.13 added the -S and -B switches
			execute_process(
				COMMAND "${CMAKE_COMMAND}"
					"-E" "env"
					"--unset=CMAKE_TOOLCHAIN_FILE"
					"--unset=CMAKE_SOURCE_ROOT"
					"--unset=CMAKE_FRAMEWORK_PATH"
					"--unset=CMAKE_INCLUDE_PATH"
					"--unset=CMAKE_LIBRARY_PATH"
					"--unset=CMAKE_PROGRAM_PATH"
					"--unset=CMAKE_BUILD_TYPE"
					"--unset=CMAKE_GENERATOR"
					"--unset=CMAKE_GENERATOR_INSTANCE"
					"--unset=CMAKE_GENERATOR_PLATFORM"
					"--unset=CMAKE_GENERATOR_TOOLSET"
					"--unset=CMAKE_C_COMPILER_LAUNCHER"
					"--unset=CMAKE_C_LINKER_LAUNCHER"
					"--unset=LDFLAGS"
					"${CMAKE_COMMAND}"
					"-DCMAKE_BUILD_TYPE=Debug"
					"-G" "${CMAKE_GENERATOR}"
					"-S" "${SQUIRRELJME_UTIL_SOURCE_DIR}"
					"-B" "${SQUIRRELJME_UTIL_DIR}"
				RESULT_VARIABLE cmakeUtilBuildResult)
		else()
			# Need to initialize the project the old way, by just being in
			# a different working directory and referring to the source
			execute_process(
				COMMAND "${CMAKE_COMMAND}"
					"-E" "env"
					"--unset=CMAKE_TOOLCHAIN_FILE"
					"--unset=CMAKE_SOURCE_ROOT"
					"--unset=CMAKE_FRAMEWORK_PATH"
					"--unset=CMAKE_INCLUDE_PATH"
					"--unset=CMAKE_LIBRARY_PATH"
					"--unset=CMAKE_PROGRAM_PATH"
					"--unset=CMAKE_BUILD_TYPE"
					"--unset=CMAKE_GENERATOR"
					"--unset=CMAKE_GENERATOR_INSTANCE"
					"--unset=CMAKE_GENERATOR_PLATFORM"
					"--unset=CMAKE_GENERATOR_TOOLSET"
					"--unset=CMAKE_C_COMPILER_LAUNCHER"
					"--unset=CMAKE_C_LINKER_LAUNCHER"
					"--unset=LDFLAGS"
					"${CMAKE_COMMAND}"
					"-DCMAKE_BUILD_TYPE=Debug"
					"-G" "${CMAKE_GENERATOR}"
					"${SQUIRRELJME_UTIL_SOURCE_DIR}"
				WORKING_DIRECTORY "${SQUIRRELJME_UTIL_DIR}"
				RESULT_VARIABLE cmakeUtilConfigResult)
		endif()
	endif()

	# Did this fail?
	if(cmakeUtilConfigResult)
		message(WARNING "Cannot configure utils: "
			"${cmakeUtilConfigResult}...")
	else()
		# Determine executable suffix
		if(EXISTS "${SQUIRRELJME_UTIL_DIR}/suffix")
			file(STRINGS "${SQUIRRELJME_UTIL_DIR}/suffix"
				SQUIRRELJME_HOST_EXE_SUFFIX)
			message(DEBUG "Host executable suffix is "
				"'${SQUIRRELJME_HOST_EXE_SUFFIX}'.")
		endif()
	endif()
else()
	message(STATUS
		"No need to configure utilities, already there...")
	set(cmakeUtilConfigResult 0)
endif()

if (NOT cmakeUtilConfigResult AND
	NOT EMSCRIPTEN)
	# Build the utilities, just in case it is out of date
	message(STATUS "Building utilities, if out of date...")
	execute_process(
		COMMAND "${CMAKE_COMMAND}"
			"--build" "${SQUIRRELJME_UTIL_DIR}"
		RESULT_VARIABLE cmakeUtilBuildResult
		WORKING_DIRECTORY "${SQUIRRELJME_UTIL_DIR}")

	# Make sure the executable actually runs since it might have built
	if(NOT cmakeUtilBuildResult)
		# Determine path where simple exists
		squirreljme_util(cmakeSimpleExe simple)

		# Execute it and check if it works
		execute_process(COMMAND "${cmakeSimpleExe}"
			RESULT_VARIABLE cmakeUtilBuildResult
			WORKING_DIRECTORY "${SQUIRRELJME_UTIL_DIR}")

		if(cmakeUtilBuildResult)
			message(WARNING "Failed to run simple test utility.")
		else()
			message(STATUS "Simple test ran okay.")
		endif()
	endif()
else()
	set(cmakeUtilBuildResult 1)
endif()

# Did this fail?
if(cmakeUtilBuildResult)
	# Ignore for now
	message(WARNING "Cannot build and run utils (CMake): "
		"${cmakeUtilBuildResult}...")

	# Try to find a compiler
	find_program(HOST_CC "cc")
	if(NOT HOST_CC)
		find_program(HOST_CC "gcc")
	endif()

	# Fallback to regular make, maybe it will work
	execute_process(
		COMMAND "${CMAKE_COMMAND}"
			"-E" "env"
			"--unset=CC"
			"--unset=CFLAGS"
			"--unset=CXX"
			"--unset=CPP"
			"--unset=LD"
			"--unset=LDFLAGS"
			"make" "all"
			"OUTPUT_DIR=${SQUIRRELJME_UTIL_DIR}"
			"HOST_EXE_SUFFIX=${SQUIRRELJME_HOST_EXE_SUFFIX}"
		RESULT_VARIABLE makeUtilBuildResult
		WORKING_DIRECTORY "${SQUIRRELJME_UTIL_SOURCE_DIR}")

	# This failed too...
	if(makeUtilBuildResult)
		message(FATAL_ERROR
			"Cannot build utils (Make): "
				"${makeUtilBuildResult}...")
	endif()
endif()

# If there is no suffix and we are on Windows, assume .exe
if(NOT DEFINED SQUIRRELJME_HOST_EXE_SUFFIX AND
	CMAKE_HOST_SYSTEM_NAME STREQUAL "Windows")
	set(SQUIRRELJME_HOST_EXE_SUFFIX ".exe")
endif()

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

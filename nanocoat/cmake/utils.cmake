# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Defines the base project and the versioning info

# Finds multiple programs in PATH
function(squirreljme_find_programs result inNames)
	# Get names to find
	set(names "${ARGV}")
	list(REMOVE_AT names 0)

	# Ensure the result is cleared
	set(${result})

	# Get the system path and convert it to a CMake list
	set(sysPaths "$ENV{PATH}")
	file(TO_CMAKE_PATH "${sysPaths}" sysPaths)

	# Go through each system path and try to find a path with each name
	foreach(sysPath IN ITEMS ${sysPaths})
		foreach(name IN ITEMS ${names})
			# Determine the actual path to check
			file(TO_CMAKE_PATH
				"${sysPath}/${name}${CMAKE_HOST_EXECUTABLE_SUFFIX}"
				checkPath)

			# Does this exist?
			if(EXISTS "${checkPath}")
				# CMake 3.29+ has executable check
				if(squirreljme_bp_version_3_29)
					if(NOT IS_EXECUTABLE "${checkPath}")
						continue()
					endif()
				endif()

				# Add it to the resultant path
				list(APPEND ${result} "${checkPath}")
			endif()
		endforeach()
	endforeach()

	# Return the found executables
	squirreljme_bp_return_propagate(${result})
endfunction()

# Does the host system have make?
squirreljme_find_programs(hostMakes
	"gmake" "make")

# Does the host system have a C compiler?
squirreljme_find_programs(hostCompilers
	"cc" "c99" "c89" "gcc" "c99-gcc" "c89-gcc" "llvm")

# Try to find the host's own CMake installation, in the event this is some
# toolchain build
# Always make sure our current CMake is there
squirreljme_find_programs(hostCMakes
	"cmake")
list(APPEND hostCMakes "${CMAKE_COMMAND}")

# Debugging
message(STATUS "-- For building host utilities...")
foreach(hostMake IN ITEMS ${hostMakes})
	message(STATUS "- Host make: ${hostMake}")
endforeach()

foreach(hostCompiler IN ITEMS ${hostCompilers})
	message(STATUS "- Host compiler: ${hostCompiler}")
endforeach()

foreach(hostCMake IN ITEMS ${hostCMakes})
	message(STATUS "- Host CMake: ${hostCMake}")
endforeach()
message(STATUS "...these were found")

# File is used for debugging
find_program(HOST_FILE "file")

# Checks that the utility can be executed by passing --probe, this does nothing
# in the utility except returns success always. If this fails to run then
# something is wrong with the host environment or there is something else
# going on...
function(squirreljme_build_util_check_probe name)
	if(EXISTS "${sjmeUtilExe_${name}}")
		# Notice
		message(STATUS
			"Checking if ${sjmeUtilExe_${name}} is executable...")

		# Check using host's file, if it exists, for debugging
		if(NOT "${HOST_FILE}" STREQUAL "" AND
			NOT "${HOST_FILE}" STREQUAL "HOST_FILE-NOTFOUND")
			execute_process(COMMAND "${HOST_FILE}" "${sjmeUtilExe_${name}}"
				RESULT_VARIABLE probeExitCode
				TIMEOUT 16)
		endif()

		# Execute the command
		execute_process(COMMAND "${sjmeUtilExe_${name}}" "--probe"
			RESULT_VARIABLE probeExitCode
			TIMEOUT 16)

		# Probe failed?
		if(NOT "${probeExitCode}" EQUAL "0")
			# Emit warning
			message(WARNING
				"--probe of ${name} at ${sjmeUtilExe_${name}} failed.")

			# Delete the executable since we cannot launch it anyway
			file(REMOVE "${sjmeUtilExe_${name}}")
		endif()
	endif()
endfunction()

# Builds the given utility
# sets sjmeUtilExe_${name}
function(squirreljme_build_util name)
	# Determine output path
	set(sjmeUtilDir_${name}
		"${CMAKE_BINARY_DIR}/util/${name}")
	set(sjmeUtilExe_${name}
		"${sjmeUtilDir_${name}}/${name}${CMAKE_HOST_EXECUTABLE_SUFFIX}")

	# Make sure the output directory exists
	file(MAKE_DIRECTORY "${sjmeUtilDir_${name}}")

	# Perform an initial probe incase this was copied elsewhere or something
	# else happened between runs
	squirreljme_build_util_check_probe(${name})

	# Is it valid?
	if(EXISTS "${sjmeUtilExe_${name}}")
		squirreljme_bp_return_propagate(sjmeUtilExe_${name})
	endif()

	# Use found host makes first
	foreach(hostMake IN ITEMS ${hostMakes})
		# Notice
		message(STATUS
			"Building ${name} with ${hostMake}...")

		# Run make
		if(squirreljme_bp_version_3_24)
			execute_process(
				COMMAND "${CMAKE_COMMAND}" "-E" "env"
					"OUTPUT_DIR=${sjmeUtilDir_${name}}"
					"HOST_EXE_SUFFIX=${CMAKE_HOST_EXECUTABLE_SUFFIX}"
					"--" "${hostMake}"
					"OUTPUT_DIR=${sjmeUtilDir_${name}}"
					"HOST_EXE_SUFFIX=${CMAKE_HOST_EXECUTABLE_SUFFIX}"
				WORKING_DIRECTORY
					"${SQUIRRELJME_BP_LIST_DIR}/utils/${name}")
		else()
			execute_process(
				COMMAND "${CMAKE_COMMAND}" "-E" "env"
					"OUTPUT_DIR=${sjmeUtilDir_${name}}"
					"HOST_EXE_SUFFIX=${CMAKE_HOST_EXECUTABLE_SUFFIX}"
					"${hostMake}"
					"OUTPUT_DIR=${sjmeUtilDir_${name}}"
					"HOST_EXE_SUFFIX=${CMAKE_HOST_EXECUTABLE_SUFFIX}"
				WORKING_DIRECTORY
					"${SQUIRRELJME_BP_LIST_DIR}/utils/${name}")
		endif()

		# Probe this to see if we can actually execute it
		squirreljme_build_util_check_probe(${name})

		# Is it valid?
		if(EXISTS "${sjmeUtilExe_${name}}")
			squirreljme_bp_return_propagate(sjmeUtilExe_${name})
		endif()
	endforeach()

	# Try again, but with different found "default" compilers
	foreach(hostMake IN ITEMS ${hostMakes})
		foreach(hostCompiler IN ITEMS ${hostCompilers})
			# Notice
			message(STATUS
				"Building ${name} with ${hostMake} and ${hostCompiler}...")

			# Run make
			if(squirreljme_bp_version_3_24)
				execute_process(
					COMMAND "${CMAKE_COMMAND}" "-E" "env"
						"OUTPUT_DIR=${sjmeUtilDir_${name}}"
						"HOST_EXE_SUFFIX=${CMAKE_HOST_EXECUTABLE_SUFFIX}"
						"CC=${hostCompiler}"
						"--" "${hostMake}"
						"OUTPUT_DIR=${sjmeUtilDir_${name}}"
						"HOST_EXE_SUFFIX=${CMAKE_HOST_EXECUTABLE_SUFFIX}"
						"CC=${hostCompiler}"
					WORKING_DIRECTORY
						"${SQUIRRELJME_BP_LIST_DIR}/utils/${name}")
			else()
				execute_process(
					COMMAND "${CMAKE_COMMAND}" "-E" "env"
						"OUTPUT_DIR=${sjmeUtilDir_${name}}"
						"HOST_EXE_SUFFIX=${CMAKE_HOST_EXECUTABLE_SUFFIX}"
						"CC=${hostCompiler}"
						"${hostMake}"
						"OUTPUT_DIR=${sjmeUtilDir_${name}}"
						"HOST_EXE_SUFFIX=${CMAKE_HOST_EXECUTABLE_SUFFIX}"
						"CC=${hostCompiler}"
					WORKING_DIRECTORY
						"${SQUIRRELJME_BP_LIST_DIR}/utils/${name}")
			endif()

			# Probe this to see if we can actually execute it
			squirreljme_build_util_check_probe(${name})

			# Is it valid?
			if(EXISTS "${sjmeUtilExe_${name}}")
				squirreljme_bp_return_propagate(sjmeUtilExe_${name})
			endif()
		endforeach()
	endforeach()

	# Try again with different found CMakes
	foreach(hostCMake IN ITEMS ${hostCMakes})
		# Notice
		message(STATUS "Building ${name} with ${hostCMake}...")

		# Configure first
		if(squirreljme_bp_version_3_13)
			execute_process(
				COMMAND "${hostCMake}"
					"-B"
					"${sjmeUtilDir_${name}}"
					"-S"
					"${SQUIRRELJME_BP_LIST_DIR}/utils/${name}"
				WORKING_DIRECTORY "${sjmeUtilDir_${name}}")
		else()
			execute_process(
				COMMAND "${hostCMake}"
					"${SQUIRRELJME_BP_LIST_DIR}/utils/${name}"
				WORKING_DIRECTORY "${sjmeUtilDir_${name}}")
		endif()

		# Then build
		execute_process(
			COMMAND "${hostCMake}"
				"--build"
				"${sjmeUtilDir_${name}}")

		# Probe this to see if we can actually execute it
		squirreljme_build_util_check_probe(${name})

		# Is it valid?
		if(EXISTS "${sjmeUtilExe_${name}}")
			squirreljme_bp_return_propagate(sjmeUtilExe_${name})
		endif()
	endforeach()

	# Sanity final check
	squirreljme_build_util_check_probe(${name})

	# Does it exist after everything?
	if(EXISTS "${sjmeUtilExe_${name}}")
		squirreljme_bp_return_propagate(sjmeUtilExe_${name})
	endif()

	# Fail otherwise
	message(FATAL_ERROR "Could not build host utility ${name}...")
endfunction()

# Only these commands exists
squirreljme_build_util(decode)
squirreljme_build_util(sourceize)

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

	# Run the command
	execute_process(COMMAND "${sjmeUtilExe_decode}" "${how}"
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
	
	# Run the command
	execute_process(COMMAND "${sjmeUtilExe_sourceize}"
			"${inputPathBaseName}" "C"
		INPUT_FILE "${inputPath}"
		OUTPUT_FILE "${outputCPath}"
		RESULT_VARIABLE sourceizeExitCode
		TIMEOUT 16)
	execute_process(COMMAND "${sjmeUtilExe_sourceize}"
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

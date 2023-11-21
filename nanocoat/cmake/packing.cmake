# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake Packaging

# Packing asset base directories
set(SQUIRRELJME_ASSET_RAW
	"${CMAKE_SOURCE_DIR}/cmake/packing")
set(SQUIRRELJME_ASSET_BIN
	"${CMAKE_BINARY_DIR}/packing")

# Determine decoding utility
squirreljme_util(SQUIRRELJME_DECODE_EXE decode)

# Decode all files accordingly
file(GLOB SQUIRRELJME_ASSET_FILES
	"${SQUIRRELJME_ASSET_RAW}/*.__hex")
foreach(assetHexFile ${SQUIRRELJME_ASSET_FILES})
	# Determine the base name of the file
	get_filename_component(assetFileBase
		"${assetHexFile}" NAME)
	string(REPLACE ".__hex" ""
		assetFileBase
		${assetFileBase})

	# Make sure the target directory exists first
	file(MAKE_DIRECTORY "${SQUIRRELJME_ASSET_BIN}")

	# Determine input and output
	get_filename_component(assetHexFileAbs
		"${assetHexFile}" ABSOLUTE)
	get_filename_component(assetOutFileAbs
		"${SQUIRRELJME_ASSET_BIN}/${assetFileBase}" ABSOLUTE)

	# Get hash of input file
	file(SHA1 "${assetHexFileAbs}" assetHexHash)

	# Get last checksum, if any
	if(EXISTS "${assetOutFileAbs}.checksum")
		file(STRINGS "${assetOutFileAbs}.checksum"
			assetHexHashLast)
	else()
		set(assetHexHashLast "")
	endif()

	# Does decoding need to be rerun?
	if(NOT EXISTS "${assetOutFileAbs}.checksum" OR
		NOT EXISTS "${assetOutFileAbs}" OR
		NOT "${assetHexHash}" STREQUAL "${assetHexHashLast}")
		# Run decoding sequence
		message("Decoding ${assetHexFileAbs} to "
			"${assetOutFileAbs}...")
		file(REMOVE "${assetOutFileAbs}")
		execute_process(COMMAND "${SQUIRRELJME_DECODE_EXE}"
			INPUT_FILE "${assetHexFileAbs}"
			OUTPUT_FILE "${assetOutFileAbs}"
			ENCODING NONE
			RESULT_VARIABLE conversionExitCode
			TIMEOUT 16)

		# Failed
		if(conversionExitCode)
			message(FATAL_ERROR "Conversion failed: "
				"${conversionExitCode}.")
		endif()

		# Store checksum
		file(WRITE "${assetOutFileAbs}.checksum"
			"${assetHexHash}")
	else()
		message("File ${assetOutFileAbs} already decoded...")
	endif()
endforeach()

# Icons and Branding
set(CPACK_NSIS_MUI_ICON
	"${SQUIRRELJME_ASSET_BIN}/win32.ico")

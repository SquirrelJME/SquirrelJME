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

	# Run decoding sequence
	get_filename_component(assetHexFileAbs
		"${assetHexFile}" ABSOLUTE)
	get_filename_component(assetFileAbs
		"${SQUIRRELJME_ASSET_BIN}/${assetFileBase}" ABSOLUTE)
	message("Decoding ${assetHexFileAbs} to ${assetFileAbs}...")
	execute_process(COMMAND "${SQUIRRELJME_DECODE_EXE}"
		INPUT_FILE "${assetHexFileAbs}"
		OUTPUT_FILE "${assetFileAbs}"
		RESULT_VARIABLE conversionExitCode
		ERROR_VARIABLE conversionError
		TIMEOUT 16)

	# Failed
	if(conversionExitCode)
		message(FATAL_ERROR "Conversion failed: "
			"${conversionExitCode} "
			"${conversionError}.")
	endif()
endforeach()

# Icons and Branding
set(CPACK_NSIS_MUI_ICON
	"${SQUIRRELJME_ASSET_BIN}/win32.ico")

# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Asset support


# Packing asset base directories
set(SQUIRRELJME_ASSET_RAW
	"${CMAKE_SOURCE_DIR}/cmake/packing")
set(SQUIRRELJME_ASSET_BIN
	"${CMAKE_BINARY_DIR}/packing")

# Decode all files accordingly
file(GLOB SQUIRRELJME_ASSET_FILES
	"${SQUIRRELJME_ASSET_RAW}/*.__hex"
	"${SQUIRRELJME_ASSET_RAW}/*.__mime")
foreach(assetHexFile ${SQUIRRELJME_ASSET_FILES})
	# Determine the base name of the file
	get_filename_component(assetFileBase
		"${assetHexFile}" NAME)

	# Is this Hex or MIME?
	string(FIND "${assetFileBase}" ".__hex"
		isHexFile)

	# Remove extension from it
	string(REPLACE ".__hex" ""
		assetFileBase
		"${assetFileBase}")
	string(REPLACE ".__mime" ""
		assetFileBase
		"${assetFileBase}")

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
		message(STATUS
			"Decoding ${assetHexFileAbs} to "
			"${assetOutFileAbs}...")
		file(REMOVE "${assetOutFileAbs}")
		if(isHexFile LESS 0)
			squirreljme_decode_file(BASE64
				"${assetHexFileAbs}" "${assetOutFileAbs}")
		else()
			squirreljme_decode_file(HEX
				"${assetHexFileAbs}" "${assetOutFileAbs}")
		endif()

		# Store checksum
		file(WRITE "${assetOutFileAbs}.checksum"
			"${assetHexHash}")
	else()
		message(STATUS
			"File ${assetOutFileAbs} already decoded...")
	endif()
endforeach()

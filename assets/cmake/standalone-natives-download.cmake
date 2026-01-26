# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Allows for the download of natives from Fossil so that all
# platforms can be built for.
# unstable/0.3.0/natives-${systemNormal}-${archNormal}.zip
# unstable/0.3.0/natives-${systemNormal}-${archNormal}.zip.mkd

macro(squirreljme_natives_download systemNormal archNormal uvPath)
	# Determine the rule name
	unset(ruleName)
	squirreljme_natives_rule_name(ruleName ${systemNormal} ${archNormal})
	set(ruleName "${ruleName}.download")

	# Create the download directory (if missing)
	file(MAKE_DIRECTORY "${SQUIRRELJME_DOWNLOADS}")

	# Figure out where to put it
	set(downloadPath
		"${SQUIRRELJME_DOWNLOADS}/natives-${systemNormal}-${archNormal}.zip")

	# Create rule for catting it
	add_custom_target(${ruleName}
		COMMAND "uv" "cat" "${uvPath}" ">" "${downloadPath}"
		BYPRODUCTS "${downloadPath}"
		VERBATIM
		COMMENT "Downloading to ${downloadPath}..."
		COMMAND_EXPAND_LISTS)

	# Register the output path
	set_target_properties(${ruleName} PROPERTIES
		ADDITIONAL_CLEAN_FILES "${downloadPath}"
		SQUIRRELJME_OUTPUT_PATH "${downloadPath}"
		SQUIRRELJME_OUTPUT_TYPE "natives")

	# Add to the order
	squirreljme_natives_append_rule(${ruleName} ${systemNormal} ${archNormal}
		"download")
endmacro()

# Checks that a download is valid
macro(squirreljme_natives_download_check systemNormal archNormal)
	# Determine the UV path
	unset(uvPath)
	squirreljme_natives_download_uv_path(uvPath ${systemNormal} ${archNormal})

	# We need somewhere to put it first
	unset(tempFile)
	squirreljme_temp_path(tempFile)

	# Try catting it from the UV space, note that invalid files will have zero
	# size
	execute_process(
		COMMAND "${Fossil_EXECUTABLE}"
			"uv" "cat" "${uvPath}"
		OUTPUT_FILE "${tempFile}"
		WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
		RESULT_VARIABLE catResult)

	# Determine file size
	set(tempSize "0")
	if(EXISTS "${tempFile}")
		file(SIZE "${tempFile}" tempSize)
	endif()

	# Remove the file, we might get a more up-to-date one with a rule run
	# post-configuration
	file(REMOVE "${tempFile}")

	# This is only valid if Fossil did not fail and the file was non-zero
	if("${catResult}" EQUAL "0" AND
		"${tempSize}" GREATER "0")
		squirreljme_natives_download(${systemNormal} ${archNormal} ${uvPath})
	else()
		message(STATUS "No download for ${systemNormal}/${archNormal} "
			"(${catResult}/${tempSize})!")
	endif()
endmacro()

# Process each native
foreach(compilerMap IN LISTS SQUIRRELJME_KNOWN_NATIVES)
	# Obtain back the system and architecture
	squirreljme_unmap(systemNormal 0 "${compilerMap}")
	squirreljme_unmap(archNormal 1 "${compilerMap}")

	# Check and possibly process
	if(Fossil_EXECUTABLE)
		squirreljme_natives_download_check(${systemNormal} ${archNormal})
	endif()
endforeach()

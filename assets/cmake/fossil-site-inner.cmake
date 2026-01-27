# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Fossil site inner script

# Upload to fossil
macro(squirreljme_fossil_upload target)
	# Get the output path and type for the target
	get_target_property(outputPath ${target} SQUIRRELJME_OUTPUT_PATH)
	get_target_property(outputType ${target} SQUIRRELJME_OUTPUT_TYPE)

	# These are required
	if("${outputPath}" STREQUAL "" OR
		"${outputType}" STREQUAL "")
		message(FATAL_ERROR "Target ${target} is missing either "
			"SQUIRRELJME_OUTPUT_PATH (${outputPath}) or "
			"SQUIRRELJME_OUTPUT_TYPE (${outputType})!")
	endif()

	# Determine the upload target name
	set(uploadTarget "fossilUpload.${target}")

	# Determine the source native path
	file(TO_NATIVE_PATH "${outputPath}" outputPathNative)

	# Determine the base name of the output
	squirreljme_basename_path(outputBase "${outputPath}")

	# Determine the UV path for the target
	squirreljme_uv_path(uvPath "${outputBase}")

	# Notice
	message(STATUS "Upload '${outputPath}' -> '${uvPath}'...")
	message(DEBUG "Source: ${target} ${outputPath} ${outputType}")

	# Add in the upload command
	add_custom_target(${uploadTarget}
		COMMAND "${Fossil_EXECUTABLE}"
			"uv" "add" "${outputPathNative}"
				"--as" "${uvPath}"
		COMMAND "${Fossil_EXECUTABLE}"
			"uv" "add" "${uvDateNative}"
				"--as" "${uvPath}.mkd"
		WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
		DEPENDS ${target}
		COMMENT "Uploading '${outputPath}' to '${uvPath}'..."
		COMMAND_EXPAND_LISTS)

	# Have the general rules depend on this
	add_dependencies(fossilUpload
		${uploadTarget})
	if("${outputType}" STREQUAL "natives")
		add_dependencies(fossilUpload.onlyNatives
			${uploadTarget})
	endif()
endmacro()

# Fossil download available and exists?
function(squirreljme_fossil_downloadable result uvPath)
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

	# Remove the file, we might get a more up-to-date later
	file(REMOVE "${tempFile}")

	# This is only valid if Fossil did not fail and the file was non-zero
	if("${catResult}" EQUAL "0" AND
		"${tempSize}" GREATER "0")
		set(${result} "YES" PARENT_SCOPE)
	else()
		set(${result} "NO" PARENT_SCOPE)
	endif()
endfunction()

# Creates a target for downloading from Fossil
function(squirreljme_fossil_download ruleName outputType uvPath downloadPath)
	# Create rule for catting it
	add_custom_target(${ruleName}
		COMMAND "${Fossil_EXECUTABLE}"
			"uv" "cat" "${uvPath}" ">" "${downloadPath}"
		BYPRODUCTS "${downloadPath}"
		VERBATIM
		WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
		COMMENT "Downloading '${uvPath}' to '${downloadPath}'..."
		COMMAND_EXPAND_LISTS)

	# Register the output path
	set_target_properties(${ruleName} PROPERTIES
		ADDITIONAL_CLEAN_FILES "${downloadPath}"
		SQUIRRELJME_OUTPUT_PATH "${downloadPath}"
		SQUIRRELJME_OUTPUT_TYPE "${outputType}")
endfunction()

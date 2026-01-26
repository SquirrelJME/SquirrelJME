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
	message(STATUS "Upload '${outputBase}' -> '${uvPath}'...")
	message(STATUS "Source: ${target} ${outputPath} ${outputType}")

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

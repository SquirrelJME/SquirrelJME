# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Fossil site updates such as documentation and otherwise

# What is the base directory for upload?
set(SQUIRRELJME_UV_DIR
	"${SQUIRRELJME_VERSION_STABILITY}/${SQUIRRELJME_VERSION}")
message("Fossil uploads to `${SQUIRRELJME_UV_DIR}`.")

# Make a date file for uploads
unset(SQUIRRELJME_UV_DATE)
## Timestamp?
string(TIMESTAMP uvTime "%Y-%m-%dT%H:%M:%SZ" UTC)
list(APPEND SQUIRRELJME_UV_DATE "date:${uvTime}")

## Fossil?
if(SQUIRRELJME_VERSION_ID_FOSSIL)
	list(APPEND SQUIRRELJME_UV_DATE "fossil:${SQUIRRELJME_VERSION_ID_FOSSIL}")
endif()

## Git?
if(SQUIRRELJME_VERSION_ID_GIT)
	list(APPEND SQUIRRELJME_UV_DATE "git:${SQUIRRELJME_VERSION_ID_GIT}")
endif()

## Install4J?
# TODO: How to handle this? If we even care?
# TODO: install4j:0.3.0_1763022885

# Fixup
string(REPLACE ";" " " SQUIRRELJME_UV_DATE "${SQUIRRELJME_UV_DATE}")
message(STATUS "Fossil UV Timestamp: ${SQUIRRELJME_UV_DATE}")

# Write the date file
file(WRITE "${CMAKE_BINARY_DIR}/uvDate.mkd" "${SQUIRRELJME_UV_DATE}")

# Determine the basename of a path
function(squirreljme_basename_path dest src)
	# Get positions of the last slashes
	string(FIND "${src}" "/" fs REVERSE)
	string(FIND "${src}" "\\" bs REVERSE)

	# Bump both up by one, to exclude the slash
	math(EXPR fs "${fs} + 1")
	math(EXPR bs "${bs} + 1")

	# Has forward slash last
	if("${fs}" GREATER "${bs}")
		string(SUBSTRING "${src}" ${fs} -1 result)

	# Has backslash last
	elseif("${bs}" GREATER "${fs}")
		string(SUBSTRING "${src}" ${bs} -1 result)

	# Has neither last, or not found (both -1)
	else()
		set(result "${src}")
	endif()

	# Return the result of it
	set(${dest} "${result}" PARENT_SCOPE)
endfunction()

# Only possible if Fossil exists
if(Fossil_EXECUTABLE)
	# Add pseudo target which depends on all upload targets
	add_custom_target(fossilUpload)

	# Register to CI/CD
	squirreljme_cicd_register(fossilUpload)

	# This macro makes it much easier to actually upload to the destination
	# since it is used in many locations
	# fromPath is usually the binary
	# toPath is usually where goes in the UV space
	macro(squirreljme_add_fossil_upload target itemBase fromPath toPath)
		# Determine native paths, which is needed by Fossil
		file(TO_NATIVE_PATH "${fromPath}" fromPathNative)
		file(TO_NATIVE_PATH "${CMAKE_BINARY_DIR}/uvDate.mkd" uvDateNative)

		# Debug
		message(STATUS "Mapped UV: (${target}) ${fromPath} -> ${toPath}")

		if(NOT itemBase OR itemBase STREQUAL "")
			set(uploadTarget fossilUpload.${target})
		else()
			set(uploadTarget fossilUpload.${target}.${itemBase})
		endif()

		# Add in the upload command
		add_custom_target(${uploadTarget}
			COMMAND "${Fossil_EXECUTABLE}"
				"uv" "add" "${fromPathNative}"
					"--as" "${toPath}"
			COMMAND "${Fossil_EXECUTABLE}"
				"uv" "add" "${uvDateNative}"
					"--as" "${toPath}.mkd"
			WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
			DEPENDS ${target}
			COMMENT "Uploading ${toPath} (${uploadTarget})..."
			COMMAND_EXPAND_LISTS)

		# Have fossil upload depend on this
		add_dependencies(fossilUpload
			${uploadTarget})
	endmacro()

	# Register a target for uploading to Fossil
	macro(squirreljme_fossil_upload_register)
		set(targetsList "${ARGV}")
		foreach(target IN LISTS targetsList)
			# Get what to upload and how to handle the upload
			get_target_property(uploadWhat ${target}
				SQUIRRELJME_OUTPUT_PATH)
			get_target_property(uploadHow ${target}
				SQUIRRELJME_OUTPUT_TYPE)

			# Determine the base name of the target
			squirreljme_basename_path(uploadWhatBase "${uploadWhat}")

			# List based
			if("${uploadHow}" STREQUAL "install4j")
				foreach(item IN LISTS uploadWhat)
					# Determine the base name of the target
					squirreljme_basename_path(itemBase "${item}")

					# Add to the upload
					squirreljme_add_fossil_upload(${target} ${itemBase}
						"${item}"
						"${SQUIRRELJME_UV_DIR}/${itemBase}")
				endforeach()

			# Simply only single binaries
			elseif("${uploadHow}" STREQUAL "flatpak" OR
				"${uploadHow}" STREQUAL "natives" OR
				"${uploadHow}" STREQUAL "rom" OR
				"${uploadHow}" STREQUAL "source" OR
				"${uploadHow}" STREQUAL "standalone")
				# Add to the upload
				squirreljme_add_fossil_upload(${target} ""
					"${uploadWhat}"
					"${SQUIRRELJME_UV_DIR}/${uploadWhatBase}")
			else()
				message(STATUS "TODO: Upload ${target} via ${uploadHow}.")
			endif()
		endforeach()
	endmacro()
else()
	macro(squirreljme_fossil_upload_register)
		message(STATUS "Ignoring fossilUpload for ${ARGV}...")
	endmacro()
endif()

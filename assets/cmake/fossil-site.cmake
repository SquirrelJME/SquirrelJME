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
message(STATUS "Fossil uploads to `${SQUIRRELJME_UV_DIR}`.")

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
file(TO_NATIVE_PATH "${CMAKE_BINARY_DIR}/uvDate.mkd" uvDateNative)

# Only possible if Fossil exists
if(SQUIRRELJME_REPO_FOSSIL)
	# Pseudo targets for uploading everything and only natives
	add_custom_target(fossilUpload)
	add_custom_target(fossilUpload.onlyNatives)
	add_custom_target(fossilUpload.install4j)

	# Never add these to all!
	set_target_properties(fossilUpload PROPERTIES
		EXCLUDE_FROM_ALL YES)
	set_target_properties(fossilUpload.onlyNatives PROPERTIES
		EXCLUDE_FROM_ALL YES)
	set_target_properties(fossilUpload.install4j PROPERTIES
		EXCLUDE_FROM_ALL YES)

	# Inner-script
	squirreljme_include("fossil-site-inner.cmake")

# Does not exist? Ignore
else()
	# Upload to fossil
	macro(squirreljme_fossil_upload target)
		message(STATUS "Ignoring fossilUpload for ${target}...")
	endmacro()

	# Fossil download available and exists?
	function(squirreljme_fossil_downloadable result uvPath)
		set(${result} "NO" PARENT_SCOPE)
	endfunction()
endif()

# Register multiple targets for Fossil uploading
macro(squirreljme_fossil_upload_register)
	set(targetList "${ARGV}")
	foreach(target IN LISTS targetList)
		squirreljme_fossil_upload(${target})
	endforeach()
endmacro()

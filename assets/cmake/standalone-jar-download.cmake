# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Downloaded Standalone Jar

# Setup download directory for downloaded standalone
set(SQUIRRELJME_JAR_DOWNLOAD_DIR
	"${CMAKE_BINARY_DIR}/download")
set(SQUIRRELJME_JAR_DOWNLOAD_PATH
	"${SQUIRRELJME_JAR_DOWNLOAD_DIR}/${SQUIRRELJME_JAR_BASENAME}")

# Can only download if Fossil is available
if(Fossil_EXECUTABLE)
	# Determine the UV path
	unset(uvPath)
	squirreljme_uv_path(uvPath "${SQUIRRELJME_JAR_BASENAME}")

	# Is this available?
	unset(available)
	squirreljme_fossil_downloadable(available "${uvPath}")

	# Is this actually available?
	if(available)
		# Setup rule for download
		squirreljme_fossil_download(standalone.base.download "standalone"
			"${uvPath}" "${SQUIRRELJME_JAR_DOWNLOAD_PATH}")

		# Do not download by default
		set_target_properties(standalone.base.download PROPERTIES
			EXCLUDE_FROM_ALL YES)

		# Add to the order
		squirreljme_natives_append_rule(standalone.base.download
			"standalone" "base" "download")
	endif()
endif()

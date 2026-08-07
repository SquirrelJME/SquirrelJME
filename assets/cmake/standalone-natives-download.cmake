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

function(squirreljme_natives_download systemNormal archNormal uvPath)
	# Do not try adding potentially multiple valid targets
	squirreljme_natives_check_order(hasOrder ${systemNormal} ${archNormal})
	if(${hasOrder})
		return()
	endif()

	# Determine the rule name
	unset(ruleName)
	squirreljme_natives_rule_name(ruleName ${systemNormal} ${archNormal})
	set(ruleName "${ruleName}.download")

	# Create the download directory (if missing)
	file(MAKE_DIRECTORY "${SQUIRRELJME_DOWNLOADS}")

	# Figure out where to put it
	set(downloadPath
		"${SQUIRRELJME_DOWNLOADS}/natives-${systemNormal}-${archNormal}.zip")

	# Setup rule
	squirreljme_fossil_download(${ruleName} "natives"
		"${uvPath}" "${downloadPath}")

	# Add to the order
	squirreljme_natives_append_rule(${ruleName} ${systemNormal} ${archNormal}
		"download")
endfunction()

# Checks that a download is valid
function(squirreljme_natives_download_check systemNormal archNormal)
	# Determine the UV path
	unset(uvPath)
	squirreljme_natives_download_uv_path(uvPath ${systemNormal} ${archNormal})

	# Is this available?
	unset(available)
	squirreljme_fossil_downloadable(available "${uvPath}")

	# This is only valid if Fossil did not fail and the file was non-zero
	if(available)
		squirreljme_natives_download(${systemNormal} ${archNormal} ${uvPath})
	else()
		message(STATUS "No download for ${systemNormal}/${archNormal}!")
	endif()
endfunction()

# Process each native
foreach(compilerMap IN ITEMS ${SQUIRRELJME_KNOWN_NATIVES})
	# Obtain back the system and architecture
	squirreljme_unmap(systemNormal 0 "${compilerMap}")
	squirreljme_unmap(archNormal 1 "${compilerMap}")

	# Check and possibly process
	if(SQUIRRELJME_REPO_FOSSIL)
		squirreljme_natives_download_check(${systemNormal} ${archNormal})
	endif()
endforeach()

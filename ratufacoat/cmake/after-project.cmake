# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: After project usage

# Linux specific
if(LINUX)
	# `uname -o`
	execute_process(COMMAND uname -o
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		ERROR_QUIET
		RESULT_VARIABLE SQUIRRELJME_UNAME_O_RESULT
		OUTPUT_VARIABLE SQUIRRELJME_UNAME_O_OUTPUT
		ERROR_VARIABLE SQUIRRELJME_UNAME_O_ERROR
		OUTPUT_STRIP_TRAILING_WHITESPACE)

	# Was successful?
	if("${SQUIRRELJME_UNAME_O_RESULT}" EQUAL 0)
		# Is this running on an actual Android system, i.e. Termux?
		if(SQUIRRELJME_UNAME_O_OUTPUT STREQUAL "android" OR
			SQUIRRELJME_UNAME_O_OUTPUT STREQUAL "Android")
			set(SQUIRRELJME_ON_ANDROID_BUILD YES)
		endif()
	endif()
endif()
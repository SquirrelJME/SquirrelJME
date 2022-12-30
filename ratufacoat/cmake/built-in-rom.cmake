# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Built-in ROM check and inclusion

set(SQUIRRELJME_BUILTIN_TEST_CHECK "${PROJECT_SOURCE_DIR}/build/builtinTest.c")
set(SQUIRRELJME_BUILTIN_FILE_CHECK "${PROJECT_SOURCE_DIR}/build/builtin.c")
# Testing ROM
if(SQUIRRELJME_USE_BUILTIN AND EXISTS "${SQUIRRELJME_BUILTIN_TEST_CHECK}")
	message("SquirrelJME: Including Built-In ROM (Testing).")

	set(SQUIRRELJME_HAS_BUILTIN ON)
	set(SQUIRRELJME_BUILTIN_FILE "${SQUIRRELJME_BUILTIN_TEST_CHECK}")

	# Release ROM
elseif(SQUIRRELJME_USE_BUILTIN AND EXISTS "${SQUIRRELJME_BUILTIN_FILE_CHECK}")
	message("SquirrelJME: Including Built-In ROM.")

	set(SQUIRRELJME_HAS_BUILTIN ON)
	set(SQUIRRELJME_BUILTIN_FILE "${SQUIRRELJME_BUILTIN_FILE_CHECK}")
else()
	if(EXISTS "${SQUIRRELJME_BUILTIN_FILE_CHECK}")
		message("SquirrelJME: Built-In ROM Available, but disabled.")
	else()
		message("SquirrelJME: No Built-In ROM Available.")
	endif()

	set(SQUIRRELJME_HAS_BUILTIN OFF)
	set(SQUIRRELJME_BUILTIN_FILE)
endif()
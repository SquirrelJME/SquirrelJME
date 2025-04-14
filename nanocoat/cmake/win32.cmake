# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Win32 Specific Helpers

# On by default if on Win32?
if(WIN32)
	set(SQUIRRELJME_ON_IF_WIN32 ON)
	set(SQUIRRELJME_OFF_IF_WIN32 OFF)
else()
	set(SQUIRRELJME_ON_IF_WIN32 OFF)
	set(SQUIRRELJME_OFF_IF_WIN32 ON)
endif()

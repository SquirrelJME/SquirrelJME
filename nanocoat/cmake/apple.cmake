# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Apple Specific Helpers

# On by default if Apple Desktop?
if(SQUIRRELJME_IS_APPLE_DESKTOP)
	set(SQUIRRELJME_ON_IF_APPLE_DESKTOP ON)
	set(SQUIRRELJME_OFF_IF_APPLE_DESKTOP OFF)
else()
	set(SQUIRRELJME_ON_IF_APPLE_DESKTOP OFF)
	set(SQUIRRELJME_OFF_IF_APPLE_DESKTOP ON)
endif()
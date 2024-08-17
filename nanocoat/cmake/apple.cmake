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

# On by default if GNUStep Desktop?
if(SQUIRRELJME_IS_GNUSTEP_DESKTOP)
	set(SQUIRRELJME_ON_IF_GNUSTEP_DESKTOP ON)
	set(SQUIRRELJME_OFF_IF_GNUSTEP_DESKTOP OFF)
else()
	set(SQUIRRELJME_ON_IF_GNUSTEP_DESKTOP OFF)
	set(SQUIRRELJME_OFF_IF_GNUSTEP_DESKTOP ON)
endif()

# Either or?
if(SQUIRRELJME_IS_APPLE_DESKTOP OR SQUIRRELJME_IS_GNUSTEP_DESKTOP)
	set(SQUIRRELJME_ON_IF_APPLE_OR_GNUSTEP_DESKTOP ON)
	set(SQUIRRELJME_OFF_IF_APPLE_OR_GNUSTEP_DESKTOP OFF)
else()
	set(SQUIRRELJME_ON_IF_APPLE_OR_GNUSTEP_DESKTOP OFF)
	set(SQUIRRELJME_OFF_IF_APPLE_OR_GNUSTEP_DESKTOP ON)
endif()

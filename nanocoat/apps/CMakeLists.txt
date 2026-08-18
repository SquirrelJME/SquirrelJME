# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Ancient Build Contraption Device
if(SQUIRRELJME_ENABLE_APP_ABCD)
	add_subdirectory(abcd)
endif()

# ScritchUi Demo
if(SQUIRRELJME_ENABLE_APP_DEMO_SCRITCHUI)
	add_subdirectory(demo-scritchui)
endif()

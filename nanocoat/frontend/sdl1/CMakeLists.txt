# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# CPack
if(SQUIRRELJME_ENABLE_PACKING)
	cpack_add_component(SDL1
		DISPLAY_NAME "SquirrelJME SDL1"
		DESCRIPTION "SquirrelJME running on SDL1.")
	set(CPACK_DEBIAN_SDL1_PACKAGE_PROVIDES
		"squirreljme")
	set(CPACK_DEBIAN_SDL1_PACKAGE_DEPENDS
		"libsdl1.2debian | libsdl1.2-compat")
endif()

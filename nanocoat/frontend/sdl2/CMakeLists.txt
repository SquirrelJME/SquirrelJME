# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# CPack
cpack_add_component(SDL2
	DISPLAY_NAME "SquirrelJME SDL2"
	DESCRIPTION "SquirrelJME running on SDL2.")
set(CPACK_DEBIAN_SDL2_PACKAGE_PROVIDES
	"squirreljme")
set(CPACK_DEBIAN_SDL2_PACKAGE_DEPENDS
	"libsdl2-2.0-0")

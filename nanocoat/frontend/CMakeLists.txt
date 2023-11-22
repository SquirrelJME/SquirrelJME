# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# RetroArch/LibRetro
add_subdirectory(libretro)

# Not doing a special build for RetroArch only...
if(NOT DEFINED ENV{SQUIRRELJME_SPECIAL_BUILD_LIBRETRO} OR
	NOT ENV{SQUIRRELJME_SPECIAL_BUILD_LIBRETRO})
	# SDL1
	add_subdirectory(sdl1)

	# SDL2
	add_subdirectory(sdl2)
endif()

# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
squirreljme_multilib_add_static_library(ScritchUIPure
	pureCore.c)

# Include main headers
squirreljme_multilib_static_target_include_directories(ScritchUIPure PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIPure
	"squirreljme-scritchui-pure")

# Do not include any of these for RetroArch
if(NOT DEFINED ENV{SQUIRRELJME_SPECIAL_BUILD_LIBRETRO} OR
	NOT ENV{SQUIRRELJME_SPECIAL_BUILD_LIBRETRO})
	# Allegro 4
	add_subdirectory(allegro4)

	# Allegro 5
	add_subdirectory(allegro5)

	# GLU1
	add_subdirectory(glu1)

	# SDL1
	add_subdirectory(sdl1)

	# SDL2
	add_subdirectory(sdl2)
endif()

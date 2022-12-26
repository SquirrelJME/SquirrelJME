# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Front ends

# We need package config!
find_package(PkgConfig)

if (NOT SQUIRRELJME_ANCIENT AND
	NOT CMAKE_SYSTEM_NAME STREQUAL "ieee1275")
	set(SQUIRRELJME_ALLOW_SDL2 YES)
	set(SQUIRRELJME_ALLOW_LIBRETRO YES)

	if(FALSE AND MSVC AND NOT ${CMAKE_VERSION} VERSION_LESS "3.8")
		set(SQUIRRELJME_ALLOW_UWP YES)
	endif()
endif()

# SDL2
if(SQUIRRELJME_ALLOW_SDL2)
	# Try to find SDL2
	if (PKG_CONFIG_FOUND)
		pkg_search_module(SDL2 sdl2)
	else()
		find_package(SDL2)
	endif()

	# Do we have SDL2?
	if(SDL2_FOUND)
		add_subdirectory(sdl2)
	else()
		message("SDL2 not available as it was not found.")
	endif()
else()
	message("SDL2 is not supported for ancient systems.")
endif()

# RetroArch
if(SQUIRRELJME_ALLOW_LIBRETRO)
	message("RetroArch: Enabled")
	add_subdirectory(libretro)
else()
	message("RetroArch: Disabled")
endif()

# Universal Windows Platform
if(SQUIRRELJME_ALLOW_UWP)
	message("UWP: Enabled")
	add_subdirectory(uwp)
else()
	message("UWP: Disabled")
endif()

# IEEE1275: Open Firmware
add_subdirectory(ieee1275)

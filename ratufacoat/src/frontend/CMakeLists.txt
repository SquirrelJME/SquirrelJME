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

# SDL2
if(NOT SQUIRRELJME_ANCIENT)
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
if(NOT SQUIRRELJME_ANCIENT)
	add_subdirectory(libretro)
else()
	message("RetroArch is not supported for ancient systems.")
endif()

# Universal Windows Platform
if(FALSE AND MSVC AND NOT ${CMAKE_VERSION} VERSION_LESS "3.8")
	add_subdirectory(uwp)
else()
	message("UWP is only supported with MSVC Compilers and CMake 3.8+!")
endif()

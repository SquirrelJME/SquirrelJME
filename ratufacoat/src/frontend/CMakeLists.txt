# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Front ends

# SDL2 and RetroArch Front-Ends
add_subdirectory(sdl2)
add_subdirectory(libretro)

# Universal Windows Platform
if(FALSE AND MSVC AND NOT ${CMAKE_VERSION} VERSION_LESS "3.8")
	add_subdirectory(uwp)
else()
	message("UWP is only supported with MSVC Compilers and CMake 3.8+!")
endif()

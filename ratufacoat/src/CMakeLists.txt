# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Main project source code

# Base core library
add_library(SquirrelJMECore STATIC
	sjmerc.c sjmercnm.c sjmercvm.c stringies.c syscall.c)
target_include_directories(SquirrelJMECore PUBLIC
	"${PROJECT_BINARY_DIR}" "${PROJECT_SOURCE_DIR}/include")

# Add the SDL2 Implementation
add_subdirectory(sdl2)

# And the libRetro Implementation (Testing)
add_subdirectory(libretro)

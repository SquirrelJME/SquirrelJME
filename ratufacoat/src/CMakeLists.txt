# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Main project source code

# Does the static ROM file exist? If it does we can pack it into the binary
if(EXISTS "${PROJECT_SOURCE_DIR}/build/builtin.c")
	message("SquirrelJME: Including Built-In ROM.")
	set(SQUIRRELJME_HAS_BUILTIN)

	set(SQUIRRELJME_BUILTIN_FILE "${PROJECT_SOURCE_DIR}/build/builtin.c")
else()
	message("SquirrelJME: No Built-In ROM Available.")

	set(SQUIRRELJME_BUILTIN_FILE)
endif()

# Base core library
add_library(SquirrelJMECore STATIC
	sjmerc.c sjmercnm.c sjmercvm.c stringies.c syscall.c
	${SQUIRRELJME_BUILTIN_FILE})
target_include_directories(SquirrelJMECore PUBLIC
	"${PROJECT_BINARY_DIR}" "${PROJECT_SOURCE_DIR}/include")

# Does the static ROM file exist? If it does we can pack it into the binary
if(EXISTS "${PROJECT_SOURCE_DIR}/build/builtin.c")
	add_compile_definitions(SQUIRRELJME_HAS_BUILTIN)
endif()

# Add the SDL2 Implementation
add_subdirectory(sdl2)

# And the libRetro Implementation (Testing)
add_subdirectory(libretro)

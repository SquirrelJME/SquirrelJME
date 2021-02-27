# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Main project source code

# Does the static ROM file exist? If it does we can pack it into the binary
if(SQUIRRELJME_HAS_BUILTIN)
	set(SQUIRRELJME_BUILTIN_FILE "${PROJECT_SOURCE_DIR}/build/builtin.c")
endif()

# Base core library
add_library(SquirrelJMECore STATIC
	cpu.c
	handles.c
	sjmerc.c
	sjmercnm.c
	sjmercvm.c
	stringies.c
	syscall.c
	${SQUIRRELJME_BUILTIN_FILE})
target_include_directories(SquirrelJMECore PUBLIC
	"${PROJECT_BINARY_DIR}" "${PROJECT_SOURCE_DIR}/include")

# Does the static ROM file exist? If it does we can pack it into the binary
if(SQUIRRELJME_HAS_BUILTIN)
	add_compile_definitions(SQUIRRELJME_HAS_BUILTIN=1)
endif()

# Add the SDL2 Implementation
add_subdirectory(sdl2)

# And the libRetro Implementation (Testing)
add_subdirectory(libretro)

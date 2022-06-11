# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SDL2 front-end to SquirrelJME

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# We need package config!
find_package(PkgConfig)

# Resource file on Windows
if(WIN32)
	set(SQUIRRELJME_SDL_RC "win32.rc")
endif()

add_executable(SquirrelJME
	sys_stdc.c
	${SQUIRRELJME_SDL_RC})

# Try to find SDL2
if (PKG_CONFIG_FOUND)
	pkg_search_module(SDL2 sdl2 REQUIRED)
else()
	find_package(SDL2 REQUIRED)
endif()

# This is written only in C!
set_target_properties(SquirrelJME PROPERTIES LINKER_LANGUAGE C)

target_include_directories(SquirrelJME PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include"
	"${SDL2_INCLUDE_DIRS}")

target_link_libraries(SquirrelJME PUBLIC
	SquirrelJMECoreLib
	"${SDL2_LIBRARIES}")

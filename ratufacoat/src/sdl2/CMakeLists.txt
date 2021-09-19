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

add_executable(SquirrelJME
	sys_stdc.c
	$<TARGET_OBJECTS:SquirrelJMECore>)

target_include_directories(SquirrelJME PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

target_link_libraries(SquirrelJME
	PUBLIC SquirrelJMECore)

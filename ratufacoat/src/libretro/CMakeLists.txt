# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: LibRetro CMake Layer, solely for IDE and quick testing purposes

# Define shared library
add_library(squirreljme-libretro SHARED
	lrjoypad.c
	sys_libr.c)

# Remove the "lib" prefix on Windows, it is incorrect
if(CMAKE_SYSTEM_NAME STREQUAL Windows)
	set_target_properties(squirreljme-libretro PROPERTIES PREFIX "")
endif()

# Include all of these
target_include_directories(squirreljme-libretro PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include"
	"${PROJECT_SOURCE_DIR}/libretro")

# Include the SquirrelJME Core as all of the JVM code is there
target_link_libraries(squirreljme-libretro
	SquirrelJMECore)

# If Static ROM is being used, extra RetroArch options are available
if(SQUIRRELJME_HAS_BUILTIN)
	add_compile_definitions(SQUIRRELJME_HAS_BUILTIN=1)
endif()

# Custom launching the core, tries to find RetroArch on the system
## Determine RetroArch directory
if(CMAKE_SYSTEM_NAME STREQUAL Windows)
	set(SQUIRRELJME_LIBRETRO_EXTENSION ".exe")

	if (EXISTS "$ENV{APPDATA}/RetroArch")
		set(SQUIRRELJME_LIBRETRO_DIR "$ENV{APPDATA}/RetroArch")
	endif()
else()
	set(SQUIRRELJME_LIBRETRO_EXTENSION "")
endif()

## Target to run RetroArch with the SquirrelJME Core
if(DEFINED SQUIRRELJME_LIBRETRO_DIR)
	add_custom_target(RetroArch
		DEPENDS squirreljme-libretro
		COMMAND "${SQUIRRELJME_LIBRETRO_DIR}/retroarch${SQUIRRELJME_LIBRETRO_EXTENSION}" "-L" "$<TARGET_FILE:squirreljme-libretro>"
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		COMMENT "Starting RetroArch with SquirrelJME")
endif()

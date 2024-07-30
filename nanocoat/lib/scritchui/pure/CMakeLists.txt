# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare base library
add_library(ScritchUIJAWT SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:ScritchUICorePIC>
	$<TARGET_OBJECTS:ScritchUIFBPIC>
	pureCore.c)

# Always make it PIC
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET ScritchUIJAWT
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# Include main headers
target_include_directories(ScritchUIJAWT PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${JAVA_AWT_INCLUDE_PATH}")

target_link_libraries(ScritchUIJAWT PUBLIC
	"${JAVA_AWT_LIBRARY}")

# ScritchUI requires a normalized library name
squirreljme_target_binary_name(ScritchUIJAWT
	"squirreljme-scritchui-jawt")

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

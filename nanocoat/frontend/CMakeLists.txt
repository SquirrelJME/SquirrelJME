# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# SquirrelJME Java SE Emulator Front-End
if(NOT EMSCRIPTEN AND NOT SQUIRRELJME_CROSS_BUILD)
	squirreljme_include("jni.cmake")

	if(JNI_FOUND)
		# Emulator front end used by the Java base emulator
		add_subdirectory(emulator)

		# Native JVM library to use with OpenJDK?
		add_subdirectory(libjvm)
	endif()
endif()

# Netscape Plugin API (NPAPI)
if(SQUIRRELJME_IS_UNIX OR SQUIRRELJME_IS_WINDOWS)
	add_subdirectory(npapi)
endif()

# RetroArch/LibRetro
add_subdirectory(libretro)

# Not doing a special build for RetroArch only...
if(NOT DEFINED ENV{SQUIRRELJME_SPECIAL_BUILD_LIBRETRO} OR
	NOT ENV{SQUIRRELJME_SPECIAL_BUILD_LIBRETRO})
	# SDL1
	add_subdirectory(sdl1)

	# SDL2
	add_subdirectory(sdl2)
endif()

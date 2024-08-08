# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# SquirrelJME Java SE Emulator Front-End
if(NOT EMSCRIPTEN AND
	NOT SQUIRRELJME_IS_CROSS_COMPILE AND
	SQUIRRELJME_ENABLE_DYLIB)
	squirreljme_include("jni.cmake")

	if(JNI_FOUND)
		# Native JVM library to use with OpenJDK?
		add_subdirectory(libjvm)
	endif()

	# Standard C front-end
	add_subdirectory(stdc)
endif()

# Emulator front end used by the Java base emulator
if(NOT SQUIRRELJME_GRADLE STREQUAL "")
	add_subdirectory(emulator)
endif()

# Netscape JRI (Usually for retro systems such as MacOS Classic)
if(SQUIRRELJME_ENABLE_FRONTEND_JRI)
	add_subdirectory(libjri)
endif()

# Netscape Plugin API (NPAPI)
if(SQUIRRELJME_ENABLE_DYLIB AND
	(SQUIRRELJME_IS_UNIX OR SQUIRRELJME_IS_WINDOWS))
	add_subdirectory(npapi)
endif()

# RetroArch/LibRetro
if(SQUIRRELJME_ENABLE_FRONTEND_LIBRETRO)
	add_subdirectory(libretro)
endif()

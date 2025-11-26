# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# SquirrelJME Java SE Emulator Front-End
if(NOT EMSCRIPTEN AND NOT LIBRETRO_REALLY_STATIC)
	squirreljme_include("find-jni.cmake")

	if(SQUIRRELJME_HAS_JAVA_JNI)
		# Native JVM library to use with OpenJDK?
		# Or with the native SquirrelJME libjvm launcher?
		add_subdirectory(libjvm)
	endif()
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

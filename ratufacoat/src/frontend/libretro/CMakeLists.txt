# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: LibRetro CMake Layer!

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Libraries that might be included in the build
get_property(SQUIRRELJME_CORE_TARGET_OBJECTS
	GLOBAL PROPERTY SquirrelJMECoreTargetObjects)

# Is the RetroArch build dynamic or static?
# Some targets rely on a shared library
if(LIBRETRO_STATIC)
	set(SQUIRRELJME_LIBRETRO_TYPE STATIC)
	set(SQUIRRELJME_LIBRETRO_STATIC ON)
	set(SQUIRRELJME_LIBRETRO_CORELIB ${SQUIRRELJME_CORE_TARGET_OBJECTS})
else()
	set(SQUIRRELJME_LIBRETRO_TYPE SHARED)
	set(SQUIRRELJME_LIBRETRO_STATIC OFF)
	set(SQUIRRELJME_LIBRETRO_CORELIB)
endif()

# Resource file on Windows
if(WIN32)
	set(SQUIRRELJME_LIBRETRO_RC "win32.rc")
endif()

# Define shared library
add_library(squirreljme_libretro ${SQUIRRELJME_LIBRETRO_TYPE}
	lraudio.c
	lrcheat.c
	lrfreeze.c
	lrinfo.c
	lrjoypad.c
	lrnet.c
	lrscreen.c
	lrvfs.c
	sys_libr.c
	${SQUIRRELJME_LIBRETRO_RC}
	${SQUIRRELJME_LIBRETRO_CORELIB})

# This is written only in C!
set_target_properties(squirreljme_libretro PROPERTIES LINKER_LANGUAGE C)

# Remove the "lib" prefix as it is not used by RetroArch
set_target_properties(squirreljme_libretro PROPERTIES
	PREFIX "")

# If not static, then link against our own static library
if(NOT SQUIRRELJME_LIBRETRO_STATIC)
	target_link_libraries(squirreljme_libretro PUBLIC
		SquirrelJMECore)
endif()

# Some LibRetro targets set a suffix at the end of the base name for the
# library
if(DEFINED LIBRETRO_SUFFIX AND NOT "${LIBRETRO_SUFFIX}" EQUAL "")
	set_target_properties(squirreljme_libretro PROPERTIES
		OUTPUT_NAME "squirreljme_libretro${LIBRETRO_SUFFIX}")

# On Android, any LibRetro target requires the _android suffix
elseif(CMAKE_SYSTEM_NAME MATCHES "^(Android)$")
    set_target_properties(squirreljme_libretro PROPERTIES
    	OUTPUT_NAME "squirreljme_libretro_android")
endif()

# Include all of these
target_include_directories(squirreljme_libretro PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# RetroArch must be built with position independent code for any kind of
# library.
set_property(TARGET squirreljme_libretro
	PROPERTY POSITION_INDEPENDENT_CODE ON)

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
		DEPENDS squirreljme_libretro
		COMMAND "${SQUIRRELJME_LIBRETRO_DIR}/retroarch${SQUIRRELJME_LIBRETRO_EXTENSION}" "-L" "$<TARGET_FILE:squirreljme_libretro>"
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		COMMENT "Starting RetroArch with SquirrelJME")
endif()

# Determine the objects and sources for the RetroArch build
squirreljme_object_and_sources(SquirrelJMELibRetroTarget
	squirreljme_libretro)

# Get sources used for the legacy makefile
get_property(LEGACY_CORE_SOURCE
	GLOBAL PROPERTY SquirrelJMECoreTargetSources)
get_property(LEGACY_LIBRETRO_SOURCE
	GLOBAL PROPERTY SquirrelJMELibRetroTargetSources)

# Generate legacy makefile sources
string(RANDOM
	LENGTH 4
	ALPHABET qwertyuiopasdfghjklzxcvbnm1234567890
	GENX)
set(LEGACY_TEMP "${CMAKE_CURRENT_SOURCE_DIR}/legacy/Makefile.legacy.${GENX}.tmp")
file(REMOVE "${LEGACY_TEMP}")
configure_file("${CMAKE_CURRENT_SOURCE_DIR}/legacy/Makefile.legacy.in"
	"${LEGACY_TEMP}"
	NEWLINE_STYLE LF)
message("Generating RetroArch Legacy Makefile (Temp ${LEGACY_TEMP})...")
foreach(legacySource ${LEGACY_CORE_SOURCE} ${LEGACY_LIBRETRO_SOURCE})
	file(APPEND "${LEGACY_TEMP}"
		"SOURCES_C   := $(SOURCES_C) $(CORE_DIR)/${legacySource}\n")
endforeach()

# Include compile definitions for version IDs and otherwise, so that it
# sticks in and otherwise with the build
file(APPEND "${LEGACY_TEMP}"
	"COREDEFINES := $(COREDEFINES) -DSQUIRRELJME_VERSION=\"${SQUIRRELJME_VERSION}\"\n")

# Replace the file we just made!
# file(APPEND) uses the system line endings (i.e. CRLF on Windows) so to get
# around this, we just use a different configure file input and say we want
# LFs used. This way whenever the CMake files are generated this will just
# not cause newline and this file conversions every single time. Hopefully
# however this does not cause issues with CLion trying to run CMake when this
# file is changed.
configure_file("${LEGACY_TEMP}"
	"${CMAKE_CURRENT_SOURCE_DIR}/legacy/Makefile.legacy"
	NEWLINE_STYLE LF)
file(REMOVE "${LEGACY_TEMP}")

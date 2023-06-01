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
	lrenv.c
	lrfreeze.c
	lrinfo.c
	lrjar.c
	lrjoypad.c
	lrloop.c
	lrmemcard.c
	lrnet.c
	lrscreen.c
	lrvfs.c
	${SQUIRRELJME_LIBRETRO_RC}
	${SQUIRRELJME_LIBRETRO_CORELIB})

# Common C Directives
squirreljme_common_c(squirreljme_libretro)

# Remove the "lib" prefix as it is not used by RetroArch
set_target_properties(squirreljme_libretro PROPERTIES
	PREFIX "")

# If not static, then link against our own static library
if(NOT SQUIRRELJME_LIBRETRO_STATIC)
	target_link_libraries(squirreljme_libretro PUBLIC
		SquirrelJMELibStatic)
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
	"${PROJECT_SOURCE_DIR}/include/frontend/libretro/api")

# RetroArch must be built with position independent code for any kind of
# library.
squirreljme_enable_pic(squirreljme_libretro)

# Check if Steam is a valid option
message("System ${CMAKE_SYSTEM_NAME}...")
if(APPLE OR "${CMAKE_SYSTEM_NAME}" STREQUAL "Linux")
	# Note it
	message("Checking if Steam exists...")

	# Check to see if the script works
	execute_process(COMMAND "${CMAKE_CURRENT_SOURCE_DIR}/steam.sh"
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		ERROR_QUIET
		RESULT_VARIABLE STEAM_RESULT
		OUTPUT_VARIABLE STEAM_OUTPUT
		ERROR_VARIABLE STEAM_ERROR
		OUTPUT_STRIP_TRAILING_WHITESPACE)

	# Script is valid, so use that
	if("${STEAM_RESULT}" EQUAL 0)
		set(STEAM_SCRIPT "${CMAKE_CURRENT_SOURCE_DIR}/steam.sh")
	else()
		message("Steam script said: ${STEAM_OUTPUT} ${STEAM_ERROR}")
	endif()
endif()

# Are we using the Steam helper?
if(EXISTS "${STEAM_SCRIPT}")
	message("Using Steam helper '${STEAM_SCRIPT}'.")
else()
	message("Steam does not appear to exist.")
endif()

# Custom launching the core, tries to find RetroArch on the system
## Determine RetroArch directory
if(NOT DEFINED SQUIRRELJME_LIBRETRO_COMMAND AND
	CMAKE_SYSTEM_NAME STREQUAL Windows)
	set(SQUIRRELJME_LIBRETRO_EXTENSION ".exe")

	if (EXISTS "$ENV{APPDATA}/RetroArch")
		set(SQUIRRELJME_LIBRETRO_COMMAND
			"$ENV{APPDATA}/RetroArch/retroarch${SQUIRRELJME_LIBRETRO_EXTENSION}")
	elseif (EXISTS "C:/RetroArch-Win64")
		set(SQUIRRELJME_LIBRETRO_COMMAND
			"C:/RetroArch-Win64/retroarch${SQUIRRELJME_LIBRETRO_EXTENSION}")
	endif()
endif()

# Mac OS Standard Install
if(NOT DEFINED SQUIRRELJME_LIBRETRO_COMMAND AND
	APPLE)
	set(SQUIRRELJME_POSSIBLE_RETROARCH
		"/Applications/RetroArch.app/Contents/MacOS/RetroArch")

	if (EXISTS "${SQUIRRELJME_POSSIBLE_RETROARCH}")
		set(SQUIRRELJME_LIBRETRO_COMMAND "${SQUIRRELJME_POSSIBLE_RETROARCH}")
	endif()
endif()

# Check if RetroArch is somewhere in PATH (Linux and Mac OS)
if(NOT DEFINED SQUIRRELJME_LIBRETRO_COMMAND)
	set(SQUIRRELJME_LIBRETRO_EXTENSION "")

	# Which binaries are available?
	execute_process(COMMAND which retroarch
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		ERROR_QUIET
		RESULT_VARIABLE SQUIRRELJME_LIBRETRO_WHICH_RESULT
		OUTPUT_VARIABLE SQUIRRELJME_LIBRETRO_WHICH_OUTPUT
		ERROR_VARIABLE SQUIRRELJME_LIBRETRO_WHICH_ERROR
		OUTPUT_STRIP_TRAILING_WHITESPACE)

	# It is somewhere in PATH? If it is, then use that path
	if("${SQUIRRELJME_LIBRETRO_WHICH_RESULT}" EQUAL 0)
		set(SQUIRRELJME_LIBRETRO_COMMAND
			"${SQUIRRELJME_LIBRETRO_WHICH_OUTPUT}")
	endif()
endif()

## Target to run RetroArch with the SquirrelJME Core
# The command is available on the system so we can just use that
if(DEFINED SQUIRRELJME_LIBRETRO_COMMAND)
	add_custom_target(RetroArch
		DEPENDS squirreljme_libretro
		COMMAND "${SQUIRRELJME_LIBRETRO_COMMAND}"
			"--verbose" "--log-file" "${PROJECT_SOURCE_DIR}/libretro-log.txt"
			"-L" "$<TARGET_FILE:squirreljme_libretro>"
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		COMMENT "Starting RetroArch with SquirrelJME")

	# Do not run this when "all" is done
	set_target_properties(RetroArch PROPERTIES
		EXCLUDE_FROM_DEFAULT_BUILD TRUE)
	set_target_properties(RetroArch PROPERTIES
		EXCLUDE_FROM_ALL TRUE)

# Is it possible that Steam can be used?
elseif(EXISTS "${STEAM_SCRIPT}")
	message("RetroArch launching via Steam...")

	# We need to percent encode these arguments
	get_target_property(OUTPUT_DIR squirreljme_libretro LIBRARY_OUTPUT_DIRECTORY_DEBUG)
	message("Target ${OUTPUT_DIR}")

	# Run helper script
	add_custom_target(RetroArch
		DEPENDS squirreljme_libretro
		COMMAND "${STEAM_SCRIPT}"
			"$<TARGET_FILE:squirreljme_libretro>"
		WORKING_DIRECTORY "${PROJECT_SOURCE_DIR}"
		COMMENT "Starting RetroArch with SquirrelJME")

	# Do not run this when "all" is done
	set_target_properties(RetroArch PROPERTIES
		EXCLUDE_FROM_DEFAULT_BUILD TRUE)
	set_target_properties(RetroArch PROPERTIES
		EXCLUDE_FROM_ALL TRUE)
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
	get_filename_component(legacyExtension ${legacySource} EXT)

	if("${legacyExtension}" STREQUAL ".cxx")
		file(APPEND "${LEGACY_TEMP}"
			"SOURCES_CXX   := $(SOURCES_CXX) $(CORE_DIR)/${legacySource}\n")
	else()
		file(APPEND "${LEGACY_TEMP}"
			"SOURCES_C   := $(SOURCES_C) $(CORE_DIR)/${legacySource}\n")
	endif()
endforeach()

# C++
foreach(legacySource ${LEGACY_CORE_SOURCE_CXX} ${LEGACY_LIBRETRO_SOURCE_CXX})
	file(APPEND "${LEGACY_TEMP}"
		"SOURCES_CXX   := $(SOURCES_CXX) $(CORE_DIR)/${legacySource}\n")
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

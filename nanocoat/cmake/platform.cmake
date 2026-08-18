# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Platform flags

# Is this RetroArch? Any kind of RetroArch build?
if(DEFINED RETROARCH OR
	DEFINED LIBRETRO_SUFFIX OR
	DEFINED LIBRETRO_STATIC OR
	DEFINED ENV{RETROARCH} OR
	DEFINED ENV{LIBRETRO_STATIC} OR
	DEFINED ENV{LIBRETRO_SUFFIX} OR
	DEFINED ENV{LIBRETRO} OR
	DEFINED ENV{SQUIRRELJME_SPECIAL_BUILD_LIBRETRO} OR
	DEFINED SQUIRRELJME_SPECIAL_BUILD_LIBRETRO)
	set(SQUIRRELJME_IS_LIBRETRO TRUE)
else()
	set(SQUIRRELJME_IS_LIBRETRO FALSE)
endif()

# Mac OS X Desktop?
if("${SQUIRRELJME_SYSTEM}" STREQUAL "macosx" AND
	NOT SQUIRRELJME_IS_LIBRETRO)
	set(SQUIRRELJME_IS_APPLE_DESKTOP TRUE)
else()
	set(SQUIRRELJME_IS_APPLE_DESKTOP FALSE)
endif()

# Can we be GNUStep?
if(NOT SQUIRRELJME_IS_APPLE_DESKTOP AND
	NOT SQUIRRELJME_IS_LIBRETRO AND
	(SQUIRRELJME_IS_UNIX OR SQUIRRELJME_IS_WINDOWS))
	# Is there gnu-step?
	find_program(GNUSTEP_EXE_PATH gnustep-config)

	# If there is, then GNUStep is available
	if(GNUSTEP_EXE_PATH)
		set(SQUIRRELJME_IS_GNUSTEP_DESKTOP TRUE)
	else()
		set(SQUIRRELJME_IS_GNUSTEP_DESKTOP FALSE)
	endif()
else()
	set(SQUIRRELJME_IS_GNUSTEP_DESKTOP FALSE)
endif()

# Is this capable of dynamic properties?
get_property(SQUIRRELJME_IS_DYLIB_CAPABLE
	GLOBAL PROPERTY TARGET_SUPPORTS_SHARED_LIBS)

# Output results of what is above
message(STATUS "?? Cross Compiled? ${SQUIRRELJME_IS_CROSS_COMPILE}")
message(STATUS "?? Supports DyLibs? ${SQUIRRELJME_IS_DYLIB_CAPABLE}")
message(STATUS "?? Bare Metal Hardware (No-OS)? ${SQUIRRELJME_IS_BARE_METAL}")
message(STATUS "?? Absurd System (Xkcd Rocks)? ${SQUIRRELJME_IS_ABSURD}")
message(STATUS "?? Historic System (Pre-1985)? ${SQUIRRELJME_IS_HISTORIC}")
message(STATUS "?? Ancient System (Pre-1995)? ${SQUIRRELJME_IS_ANCIENT}")
message(STATUS "?? Retro System (Pre-2005)? ${SQUIRRELJME_IS_RETRO}")
message(STATUS "?? macOS Desktop? ${SQUIRRELJME_IS_APPLE_DESKTOP}")
message(STATUS "?? GNUStep Desktop? ${SQUIRRELJME_IS_GNUSTEP_DESKTOP}")
message(STATUS "?? libretro? ${SQUIRRELJME_IS_LIBRETRO}")
message(STATUS "?? UNIX-ish? ${SQUIRRELJME_IS_UNIX}")
message(STATUS "?? Windows-ish? ${SQUIRRELJME_IS_WINDOWS}")

# Options
## Dynamic libraries?
# CMake can also tell us if this is not supported
if(SQUIRRELJME_IS_BARE_METAL OR
	NOT SQUIRRELJME_IS_DYLIB_CAPABLE OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "amiga" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "emscripten" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "dos" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "palmos" OR
	LIBRETRO_STATIC OR ENV{LIBRETRO_STATIC})
	set(SQUIRRELJME_ENABLE_DYLIB_DEFAULT FALSE)
else()
	set(SQUIRRELJME_ENABLE_DYLIB_DEFAULT TRUE)
endif()

## Netscape JRI Interface?
if((SQUIRRELJME_IS_RETRO OR SQUIRRELJME_ENABLE_DYLIB_DEFAULT) AND
	NOT "${SQUIRRELJME_SYSTEM}" STREQUAL "wine")
	set(SQUIRRELJME_ENABLE_FRONTEND_JRI_DEFAULT TRUE)
else()
	set(SQUIRRELJME_ENABLE_FRONTEND_JRI_DEFAULT FALSE)
endif()

## LibRetro?
# Note that on a libretro build, we really want libretro!
if(SQUIRRELJME_IS_LIBRETRO)
	set(SQUIRRELJME_ENABLE_FRONTEND_LIBRETRO_DEFAULT TRUE)
elseif(SQUIRRELJME_IS_RETRO OR SQUIRRELJME_IS_BARE_METAL OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "wine")
	set(SQUIRRELJME_ENABLE_FRONTEND_LIBRETRO_DEFAULT FALSE)
else()
	set(SQUIRRELJME_ENABLE_FRONTEND_LIBRETRO_DEFAULT TRUE)
endif()

## Testing?
if(SQUIRRELJME_IS_CROSS_COMPILE OR
	${CMAKE_VERSION} VERSION_LESS_EQUAL "3.12")
	set(SQUIRRELJME_ENABLE_TESTING_DEFAULT FALSE)
else()
	set(SQUIRRELJME_ENABLE_TESTING_DEFAULT TRUE)
endif()

## FPIC?
if("${SQUIRRELJME_SYSTEM}" STREQUAL "playstation2" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "psp" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "sdcc" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "vita" OR
	"${SQUIRRELJME_SYSTEM}" STREQUAL "windows")
	set(SQUIRRELJME_ENABLE_FPIC_DEFAULT FALSE)
else()
	set(SQUIRRELJME_ENABLE_FPIC_DEFAULT TRUE)
endif()

## Enable libjvm by default?
if(NOT EMSCRIPTEN AND NOT SQUIRRELJME_IS_LIBRETRO)
	set(SQUIRRELJME_ENABLE_FRONTEND_LIBJVM_DEFAULT TRUE)
else()
	set(SQUIRRELJME_ENABLE_FRONTEND_LIBJVM_DEFAULT FALSE)
endif()

## Enable Netscape Plugin API by default?
if(SQUIRRELJME_ENABLE_DYLIB AND
	(SQUIRRELJME_IS_UNIX OR SQUIRRELJME_IS_WINDOWS) AND
	NOT SQUIRRELJME_IS_LIBRETRO)
	set(SQUIRRELJME_ENABLE_FRONTEND_NPAPI_DEFAULT TRUE)
else()
	set(SQUIRRELJME_ENABLE_FRONTEND_NPAPI_DEFAULT FALSE)
endif()

# If not libretro/etc. then include system interfaces based on the current
# target. Otherwise for libretro/etc., we do not care about anything else
if(NOT SQUIRRELJME_IS_LIBRETRO)
	# Enable all applications by default
	set(SQUIRRELJME_ENABLE_APP_DEFAULTS TRUE)

	# ScritchAudion: WinMM
	set(SQUIRRELJME_ENABLE_AUDIO_WINMM_DEFAULT
		"${SQUIRRELJME_ON_IF_WIN32}")

	# ScritchUI: Win32
	set(SQUIRRELJME_ENABLE_GUI_WIN32_DEFAULT
		"${SQUIRRELJME_ON_IF_WIN32}")
else()
	# Disable all applications by default
	set(SQUIRRELJME_ENABLE_APP_DEFAULTS FALSE)

	# ScritchAudion: WinMM
	set(SQUIRRELJME_ENABLE_AUDIO_WINMM_DEFAULT FALSE)

	# ScritchUI: Win32
	set(SQUIRRELJME_ENABLE_GUI_WIN32_DEFAULT FALSE)
endif()

# Application: ABCD
set(SQUIRRELJME_ENABLE_APP_ABCD_DEFAULT
	"${SQUIRRELJME_ENABLE_APP_DEFAULTS}")

# Application: Demo ScritchUI
set(SQUIRRELJME_ENABLE_APP_DEMO_SCRITCHUI_DEFAULT
	"${SQUIRRELJME_ENABLE_APP_DEFAULTS}")

# Macro for defining and debugging options
macro(squirreljme_define_option baseVar description)
	# Define the option
	option(${baseVar}
		"${description}"
		${${baseVar}_DEFAULT})

	# Then verbosity on it, the extra conditions are for older CMake
	if (${${baseVar}} OR
		"${${baseVar}}" STREQUAL "1" OR
		"${${baseVar}}" STREQUAL "ON" OR
		"${${baseVar}}" STREQUAL "TRUE" OR
		"${${baseVar}}" STREQUAL "YES")
		message(STATUS "++ ${description}? ${${baseVar}}")
	else()
		message(STATUS "-- ${description}? ${${baseVar}}")
	endif()
endmacro()

# Option flags which are available
squirreljme_define_option(SQUIRRELJME_ENABLE_DYLIB
	"Enable Dynamic Libraries")
squirreljme_define_option(SQUIRRELJME_ENABLE_FPIC
	"Enable FPIC")
squirreljme_define_option(SQUIRRELJME_ENABLE_TESTING
	"Enable Host System Tests")

# Applications
squirreljme_define_option(SQUIRRELJME_ENABLE_APP_ABCD
	"Enable Application: ABCD")
squirreljme_define_option(SQUIRRELJME_ENABLE_APP_DEMO_SCRITCHUI
	"Enable Application: Demo ScritchUI")

# Front-ends
squirreljme_define_option(SQUIRRELJME_ENABLE_FRONTEND_JRI
	"Enable Front End: JRI")
squirreljme_define_option(SQUIRRELJME_ENABLE_FRONTEND_LIBJVM
	"Enable Front End: libjvm")
squirreljme_define_option(SQUIRRELJME_ENABLE_FRONTEND_LIBRETRO
	"Enable Front End: LibRetro")
squirreljme_define_option(SQUIRRELJME_ENABLE_FRONTEND_NPAPI
	"Enable Front End: Netscape Plugin API")

# ScritchAudio Implementation
squirreljme_define_option(SQUIRRELJME_ENABLE_AUDIO_WINMM
	"Enable ScritchAudio: WinMM")

# ScritchUI Implementations
squirreljme_define_option(SQUIRRELJME_ENABLE_GUI_WIN32
	"Enable ScritchUI: Win32")


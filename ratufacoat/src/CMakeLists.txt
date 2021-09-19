# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Main project source code

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Base core library
# Note that OBJECT libraries are used to define groups of object files for
# linking and otherwise. CMake does not support linking multiple static
# libraries together into one, however using OBJECT libraries allows them
# to include all the objects. We need to do this for RetroArch's static
# targets since they are linked directly against RetroArch executables.
add_library(SquirrelJMECore OBJECT
	accelgfx.c
	atomic.c
	audio.c
	bootrom.c
	btooth.c
	cellfunc.c
	corefont.c
	cpu.c
	datatype.c
	debug.c
	error.c
	gpio.c
	handles.c
	ipnet.c
	jdwp.c
	jvm.c
	memory.c
	oldstuff.c
	opcode.c
	random.c
	sjmerc.c
	softmath.c
	stringies.c
	syscall.c
	video.c
	xbuiltin.c
	${SQUIRRELJME_BUILTIN_FILE}
	$<TARGET_OBJECTS:SquirrelJMEEngine>
	$<TARGET_OBJECTS:SquirrelJMEFormat>)

target_include_directories(SquirrelJMECore PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMECore
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Internal support libraries and modules
add_subdirectory(engine)
add_subdirectory(format)

# Add the SDL2 Implementation
add_subdirectory(sdl2)

# And the libRetro Implementation (Testing)
add_subdirectory(libretro)

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
# It also changes how linking is done completely:
# CMake Error at tests/CMakeLists.txt:25 (target_link_libraries):
#  Target "SquirrelJMECore" of type OBJECT_LIBRARY may not be linked into
#  another target.  One may link only to STATIC or SHARED libraries, or to
#  executables with the ENABLE_EXPORTS property set.
add_library(SquirrelJMECore OBJECT
	atomic.c
	corefont.c
	counter.c
	crc.c
	cpu.c
	debug.c
	error.c
	lock.c
	memchunk.c
	memory.c
	oldstuff.c
	random.c
	softmath.c
	stream.c
	stringies.c
	utf.c
	util.c
	xbuiltin.c
	${SQUIRRELJME_BUILTIN_FILE})

# This is written only in C!
set_target_properties(SquirrelJMECore PROPERTIES LINKER_LANGUAGE C)

# Includes as needed
target_include_directories(SquirrelJMECore PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMECore
	PROPERTY POSITION_INDEPENDENT_CODE ${SQUIRRELJME_FPIC_MODE})

# Bundled C library?
if(SQUIRRELJME_BUNDLED_STDC)
	add_subdirectory(libstdc)
endif()

# Internal support libraries and modules
add_subdirectory(engine)
add_subdirectory(format)
add_subdirectory(miniz)
add_subdirectory(nuklear)
add_subdirectory(sjmejni)

# Execute this with all of the targets we want to collect
squirreljme_object_and_sources(SquirrelJMECoreTarget
	SquirrelJMECore
	SquirrelJMEEngine
	SquirrelJMEEngineSpringCoat
	SquirrelJMEFormat
	SquirrelJMENuklearUI
	SquirrelJMEMiniZ
	SquirrelJMEJNI)

# Front ends
add_subdirectory(frontend)

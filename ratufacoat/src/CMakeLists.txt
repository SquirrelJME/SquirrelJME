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
	debug.c
	error.c
	lock.c
	memchunk.c
	memory.c
	random.c
	softmath.c
	stream.c
	stringies.c
	utf.c
	util.c
	xbuiltin.c
	${SQUIRRELJME_BUILTIN_FILE})

# Common C Directives
squirreljme_common_c(SquirrelJMECore)

# Make this position independent so it can be linked into shared libraries
squirreljme_enable_pic(SquirrelJMECore)

# Bundled C library?
if(SQUIRRELJME_BUNDLED_STDC)
	add_subdirectory(libstdc)
endif()

# Supporting UI libraries and whatnot, as needed
add_subdirectory(libminiz)
add_subdirectory(libnuklear)

# Internal support libraries and modules
add_subdirectory(engine)
add_subdirectory(format)
add_subdirectory(sjmejni)

# Execute this with all of the targets we want to collect
squirreljme_object_and_sources(SquirrelJMECoreTarget
	SquirrelJMECore
	SquirrelJMEEngine
	SquirrelJMEEngineSpringCoat
	SquirrelJMEFormat
	SquirrelJMELibNuklear
	SquirrelJMELibMiniZ
	SquirrelJMEJNI)

# Front ends
add_subdirectory(frontend)

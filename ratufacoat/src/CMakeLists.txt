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
	accelgfx.c
	atomic.c
	atomiclist.c
	audio.c
	bootrom.c
	btooth.c
	cellfunc.c
	corefont.c
	counter.c
	crc.c
	cpu.c
	datatype.c
	debug.c
	error.c
	gpio.c
	handles.c
	ipnet.c
	jdwp.c
	jvm.c
	lock.c
	memchunk.c
	memory.c
	oldstuff.c
	opcode.c
	random.c
	sjmerc.c
	softmath.c
	stream.c
	stringies.c
	syscall.c
	utf.c
	util.c
	video.c
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
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Internal support libraries and modules
add_subdirectory(collections)
add_subdirectory(engine)
add_subdirectory(format)
add_subdirectory(miniz)
add_subdirectory(nuklear)
add_subdirectory(refptr)
add_subdirectory(sjmejni)

# Function to generate object files and sources for cross building
function(squirreljme_object_and_sources coreTargetVar)
	# Make sure these are cleared
	set(CORE_OBJECTS_TEMP)
	set(CORE_SOURCES_TEMP)

	# Go through each target to add the various sources together
	foreach(cmakeTarget ${ARGV})
		# We need to skip the first argument because it is the target variable
		if("${cmakeTarget}" STREQUAL "${coreTargetVar}")
			continue()
		endif()

		# Add the object file targets accordingly, this is for the static
		# library
		list(APPEND CORE_OBJECTS_TEMP
			"$<TARGET_OBJECTS:${cmakeTarget}>")

		# Get relative directory from the source tree root
		get_target_property(cmakeTargetSourceDir ${cmakeTarget} SOURCE_DIR)
		file(RELATIVE_PATH cmakeTargetSourceDir
			"${CMAKE_SOURCE_DIR}" "${cmakeTargetSourceDir}")

		# Go through sources for the target and add them accordingly
		get_target_property(cmakeTargetSources ${cmakeTarget} SOURCES)
		foreach(cmakeTargetSource ${cmakeTargetSources})
			get_filename_component(cmakeTargetSourceExt ${cmakeTargetSource}
				EXT)
			if(NOT "${cmakeTargetSourceExt}" STREQUAL ".c" AND
				NOT "${cmakeTargetSourceExt}" STREQUAL ".cxx")
				continue()
			endif()

			# If this file does not exist, then ignore it!
			if(NOT EXISTS "${CMAKE_SOURCE_DIR}/${cmakeTargetSourceDir}/${cmakeTargetSource}")
				continue()
			endif()

			list(APPEND CORE_SOURCES_TEMP
				${cmakeTargetSourceDir}/${cmakeTargetSource})
		endforeach()
	endforeach()

	# Set global property for the object and source files
	set_property(GLOBAL PROPERTY ${coreTargetVar}Objects
		${CORE_OBJECTS_TEMP})
	set_property(GLOBAL PROPERTY ${coreTargetVar}Sources
		${CORE_SOURCES_TEMP})

	# Debugging
	if(VERBOSE)
		get_property(CORE_OBJECTS_CHECK
			GLOBAL PROPERTY ${coreTargetVar}Objects)
		get_property(CORE_SOURCES_CHECK
			GLOBAL PROPERTY ${coreTargetVar}Sources)
		message("Set ${coreTargetVar}Objects: ${CORE_OBJECTS_CHECK}")
		message("Set ${coreTargetVar}Sources: ${CORE_SOURCES_CHECK}")
	endif()
endfunction()

# Execute this with all of the targets we want to collect
squirreljme_object_and_sources(SquirrelJMECoreTarget
	SquirrelJMECollections
	SquirrelJMECore
	SquirrelJMEEngine
	SquirrelJMEEngineSpringCoat
	SquirrelJMEFormat
	SquirrelJMENuklearUI
	SquirrelJMEMiniZ
	SquirrelJMERefPtr
	SquirrelJMEJNI)

# Static library
add_subdirectory(lib)

# Front ends
add_subdirectory(frontend)

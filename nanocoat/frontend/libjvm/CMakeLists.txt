# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# We need JNI for this to work
squirreljme_include("jni.cmake")

# Docker is used for testing
squirreljme_include("docker.cmake")

# Define library
add_library(libJvm SHARED
	$<TARGET_OBJECTS:CorePIC>
	revProto.c)

# Always make this position independent
set_property(TARGET libJvm
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# The base SquirrelJME core needs to be built
add_dependencies(libJvm
	CorePIC)

# We need to find all of the includes and such
target_include_directories(libJvm PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	${JNI_INCLUDE_DIRS})

# Use a conforming name for the library
set_target_properties(libJvm PROPERTIES
	RUNTIME_OUTPUT_DIRECTORY
	"jvm"
	LIBRARY_OUTPUT_NAME
	"jvm"
	ARCHIVE_OUTPUT_NAME
	"jvm")


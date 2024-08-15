# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Docker is used for testing
squirreljme_include("docker.cmake")

# Define library
add_library(libJvm SHARED
	$<TARGET_OBJECTS:BasePIC>
	$<TARGET_OBJECTS:CorePIC>
	revProto.c
	vmInit.c
	vmNativeLib.c)

# Always make this position independent
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET libJvm
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# The base SquirrelJME core needs to be built
add_dependencies(libJvm
	CorePIC)

# We need to find all of the includes and such
target_include_directories(libJvm PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${JNI_INCLUDE_DIRS}"
	"${JAVA_INCLUDE_PATH}"
	"${JAVA_INCLUDE_PATH2}")

# Libraries needed to be linked with for the DyLib to function
target_link_libraries(libJvm PUBLIC
	${SQUIRRELJME_LINK_LIBRARIES_Base}
	${SQUIRRELJME_LINK_LIBRARIES_Core})

# Use a conforming name for the library
squirreljme_target_binary_name(libJvm
	"jvm")

# Exports for Windows, etc.
squirreljme_target_shared_library_exports(libJvm)


# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SquirrelJME emulator interface (Java SE)

# We need JNI for this to work
find_package(JNI)

# Declare library
add_library(squirreljmeEmulatorLib SHARED
	$<TARGET_OBJECTS:CorePIC>
	emu.c)

# Always make this position independent
set_property(TARGET squirreljmeEmulatorLib
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# The base SquirrelJME core needs to be built
add_dependencies(squirreljmeEmulatorLib
	CorePIC)

# We need to find all of the includes and such
target_include_directories(squirreljmeEmulatorLib PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	${JNI_INCLUDE_DIRS})

# It is easier to find this when it is in the build root
set_target_properties(squirreljmeEmulatorLib PROPERTIES
	RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	ARCHIVE_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}")

# Use a simpler name for the library
set_target_properties(squirreljmeEmulatorLib PROPERTIES
	LIBRARY_OUTPUT_NAME
	"emulator-nanocoat"
	ARCHIVE_OUTPUT_NAME
	"emulator-nanocoat")

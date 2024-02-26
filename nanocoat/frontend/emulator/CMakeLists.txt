# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SquirrelJME emulator interface (Java SE)

# We need JNI for this to work
squirreljme_include("jni.cmake")

# Declare library
add_library(libEmulator SHARED
	$<TARGET_OBJECTS:CorePIC>
	emu.c
	jniHelper.c
	nativeAllocLink.c
	nativeAllocPool.c
	nativeAllocSizeOf.c
	nativeFlatList.c
	nativeNvmBootParam.c
	nativeNvmFrame.c
	nativeNvmState.c
	nativeNvmThread.c
	nativeVirtualLibrary.c
	nativeVirtualSuite.c)

# Always make this position independent
set_property(TARGET libEmulator
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# The base SquirrelJME core needs to be built
add_dependencies(libEmulator
	CorePIC)

# We need to find all of the includes and such
target_include_directories(libEmulator PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	${JNI_INCLUDE_DIRS})

# It is easier to find this when it is in the build root
set_target_properties(libEmulator PROPERTIES
	RUNTIME_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	LIBRARY_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}"
	ARCHIVE_OUTPUT_DIRECTORY "${CMAKE_BINARY_DIR}")

# Use a simpler name for the library
set_target_properties(libEmulator PROPERTIES
	LIBRARY_OUTPUT_NAME
	"emulator-nanocoat"
	ARCHIVE_OUTPUT_NAME
	"emulator-nanocoat")

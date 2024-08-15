# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SquirrelJME emulator interface (Java SE)

# Declare library
add_library(libEmulator SHARED
	$<TARGET_OBJECTS:BasePIC>
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
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET libEmulator
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# The base SquirrelJME core needs to be built
add_dependencies(libEmulator
	CorePIC)

# We need to find all of the includes and such
target_include_directories(libEmulator PUBLIC
	"${CMAKE_SOURCE_DIR}/include"
	"${JNI_INCLUDE_DIRS}"
	"${JAVA_INCLUDE_PATH}"
	"${JAVA_INCLUDE_PATH2}")

# Libraries needed to be linked with for the DyLib to function
target_link_libraries(libEmulator PUBLIC
	${SQUIRRELJME_LINK_LIBRARIES_Base}
	${SQUIRRELJME_LINK_LIBRARIES_Core})

# It is easier to find this when it is in the build root
squirreljme_target_binary_output(libEmulator
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Use a simpler name for the library
squirreljme_target_binary_name(libEmulator
	"emulator-nanocoat")

# Exports for Windows, etc.
squirreljme_target_shared_library_exports(libEmulator)

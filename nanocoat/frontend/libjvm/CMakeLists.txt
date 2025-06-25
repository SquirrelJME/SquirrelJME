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
	niInit.c
	vmArray.c
	vmCompiler.c
	vmCore.c
	vmDebug.c
	vmFile.c
	vmFloat.c
	vmIoNet.c
	vmObject.c
	vmObjectInput.c
	vmOther.c
	vmPackage.c
	vmReflect.c
	vmRuntime.c
	vmSecurity.c
	vmString.c
	vmSystem.c
	vmThread.c
	vmThrowable.c)

# Always make this position independent
if(SQUIRRELJME_ENABLE_FPIC)
	set_property(TARGET libJvm
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endif()

# The base SquirrelJME core needs to be built
add_dependencies(libJvm
	CorePIC)

# If using official JNI/JVM headers we need this definition to be set
target_compile_definitions(libJvm PRIVATE _JNI_IMPLEMENTATION_=1)
target_compile_definitions(libJvm PRIVATE SJME_JNI_IMPLEMENTATION=1)

# We need to find all of the includes and such
target_include_directories(libJvm PUBLIC
	"${CMAKE_SOURCE_DIR}/include")
target_include_directories(libJvm SYSTEM PRIVATE
	${JNI_INCLUDE_DIRS}
	${JAVA_INCLUDE_PATH}
	${JAVA_INCLUDE_PATH2}
	${JVM_INCLUDE_DIRS})

# Libraries needed to be linked with for the DyLib to function
target_link_libraries(libJvm PUBLIC
	${SQUIRRELJME_LINK_LIBRARIES_Base}
	${SQUIRRELJME_LINK_LIBRARIES_Core})

# Where should this library be output?
squirreljme_target_binary_output(libJvm
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Use a conforming name for the library
squirreljme_target_binary_name(libJvm
	"jvm")

# Exports for Windows, etc.
squirreljme_target_shared_library_exports(libJvm)


# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Define library
squirreljme_multilib_add_library(libJvm
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

# The base SquirrelJME core needs to be built
squirreljme_multilib_add_dependency(libJvm
	Core)
squirreljme_multilib_add_dependency(libJvm
	Base)

# If using official JNI/JVM headers we need this definition to be set
squirreljme_multilib_target_compile_definitions(libJvm PRIVATE
	_JNI_IMPLEMENTATION_=1)
squirreljme_multilib_target_compile_definitions(libJvm PRIVATE
	SJME_JNI_IMPLEMENTATION=1)

# We need to find all of the includes and such
squirreljme_multilib_target_include_directories(libJvm PRIVATE
	"${CMAKE_SOURCE_DIR}/include")
squirreljme_multilib_target_include_directories(libJvm SYSTEM PRIVATE
	${JNI_INCLUDE_DIRS}
	${JAVA_INCLUDE_PATH}
	${JAVA_INCLUDE_PATH2}
	${JVM_INCLUDE_DIRS})

# Libraries needed to be linked with for the DyLib to function
squirreljme_multilib_target_link_libraries(libJvm PUBLIC
	${SQUIRRELJME_LINK_LIBRARIES_Base}
	${SQUIRRELJME_LINK_LIBRARIES_Core})

# Link required libraries
squirreljme_target_link_libraries_required(libJvm)

# Where should this library be output?
squirreljme_multilib_target_binary_output(libJvm
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Specify name for the JVM library
squirreljme_multilib_target_binary_name(libJvm
	"sjmejvm")

# Exports for Windows, etc.
squirreljme_target_shared_library_exports(libJvmDyLib)


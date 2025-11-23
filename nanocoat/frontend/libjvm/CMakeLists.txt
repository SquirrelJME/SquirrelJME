# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Define library
squirreljme_multilib_add_library(libJvm
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Base_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_Core_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUICore_FPIC}>
	$<TARGET_OBJECTS:${SQUIRRELJME_LIB_ScritchUIFB_FPIC}>
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

# Executable launcher
add_executable(SquirrelJME
	lnStart.c)

# The base SquirrelJME core needs to be built
squirreljme_multilib_add_dependency(libJvm
	Core)
squirreljme_multilib_add_dependency(libJvm
	Base)
add_dependencies(SquirrelJME
	libJvmStatic)

# If using official JNI/JVM headers we need this definition to be set
squirreljme_multilib_target_compile_definitions(libJvm PRIVATE
	SJME_JNI_IMPLEMENTATION=1)
target_compile_definitions(SquirrelJME PRIVATE
	SJME_JNI_IMPLEMENTATION=1)

# We need to find all of the includes and such
squirreljme_multilib_target_include_directories(libJvm PRIVATE
	"${CMAKE_SOURCE_DIR}/include")
target_include_directories(SquirrelJME PRIVATE
	"${CMAKE_SOURCE_DIR}/include")
squirreljme_multilib_target_include_directories(libJvm SYSTEM PRIVATE
	"${JNI_INCLUDE_DIRS}"
	"${JAVA_INCLUDE_PATH}"
	"${JAVA_INCLUDE_PATH2}"
	"${JVM_INCLUDE_DIRS}"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/jni")
target_include_directories(SquirrelJME SYSTEM PRIVATE
	"${JNI_INCLUDE_DIRS}"
	"${JAVA_INCLUDE_PATH}"
	"${JAVA_INCLUDE_PATH2}"
	"${JVM_INCLUDE_DIRS}"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/jni")

# Libraries needed to be linked with for the DyLib to function
squirreljme_multilib_target_link_libraries(libJvm PUBLIC
	${SQUIRRELJME_LINK_LIBRARIES_Base}
	${SQUIRRELJME_LINK_LIBRARIES_Core})

# The JVM launcher needs the actual JVM library
target_link_libraries(SquirrelJME PRIVATE
	libJvmStatic)

# Link required libraries
squirreljme_target_link_libraries_required(libJvm)
squirreljme_target_link_libraries_required(SquirrelJME)

# Where should the executable be output?
squirreljme_target_binary_output(SquirrelJME
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Specify name for the JVM library
squirreljme_multilib_target_binary_name(libJvm
	"squirreljme")
squirreljme_target_binary_name(SquirrelJME
	"squirreljme")

# Ninja building on Windows has a problem when there is a DLL and a static
# library for the JVM
if("${SQUIRRELJME_SYSTEM}" STREQUAL "windows")
	squirreljme_target_binary_name(libJvmStatic
		"squirreljmes")
endif()

# Really only matters for dynamic libraries
if(SQUIRRELJME_ENABLE_DYLIB)
	# Output the dynamic library to where it should be
	squirreljme_target_binary_output(libJvmDyLib
		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

	# Do not set a SONAME for libjvm library as it could be either
	# libsquirreljme or libjvm, and this would break any kind of interop
	squirreljme_no_soname(libJvmDyLib)

	# Exports for Windows, etc.
	squirreljme_target_shared_library_exports(libJvmDyLib)

	# The target location can be overridden, generally through the pipeline
	# build system
	if(DEFINED ENV{SQUIRRELJME_BINARY_OUTPUT_DIR})
		set(jvmOutDir "$ENV{SQUIRRELJME_BINARY_OUTPUT_DIR}")
	elseif(DEFINED SQUIRRELJME_BINARY_OUTPUT_DIR)
		set(jvmOutDir "${SQUIRRELJME_BINARY_OUTPUT_DIR}")
	else()
		set(jvmOutDir "${SQUIRRELJME_DYLIB_OUTPUT_DIR}")
	endif()

	# Names of the original and new libraries...
	set(libSjmeOut
		"${jvmOutDir}/${CMAKE_SHARED_LIBRARY_PREFIX}squirreljme")
	set(libJvmOut
		"${jvmOutDir}/${CMAKE_SHARED_LIBRARY_PREFIX}jvm")

	# And the resultant library copy itself...
	add_custom_command(TARGET libJvmDyLib POST_BUILD
		COMMAND "${CMAKE_COMMAND}" "-E" "copy"
			"${libSjmeOut}${CMAKE_SHARED_LIBRARY_SUFFIX}"
			"${libJvmOut}${CMAKE_SHARED_LIBRARY_SUFFIX}"
		BYPRODUCTS
			"${libJvmOut}${CMAKE_SHARED_LIBRARY_SUFFIX}"
		COMMENT "Copying libsquirreljme to libjvm...")

	# If on Windows, we need to copy the implib as well
	if(SQUIRRELJME_HAS_IMPLIB)
		add_custom_command(TARGET libJvmDyLib POST_BUILD
			COMMAND "${CMAKE_COMMAND}" "-E" "copy"
				"${libSjmeOut}${CMAKE_STATIC_LIBRARY_SUFFIX}"
				"${libJvmOut}${CMAKE_STATIC_LIBRARY_SUFFIX}"
			BYPRODUCTS
				"${libJvmOut}${CMAKE_STATIC_LIBRARY_SUFFIX}"
			COMMENT "Copying libsquirreljme to libjvm (implib)...")
	endif()
endif()

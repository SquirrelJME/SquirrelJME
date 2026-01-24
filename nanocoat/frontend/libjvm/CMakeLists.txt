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
	"${SQUIRRELJME_JAVA_JNI_INCLUDE}"
	"${SQUIRRELJME_JAVA_JVM_INCLUDE}"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/jni")
target_include_directories(SquirrelJME SYSTEM PRIVATE
	"${SQUIRRELJME_JAVA_JNI_INCLUDE}"
	"${SQUIRRELJME_JAVA_JVM_INCLUDE}"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/jni")

# Public SquirrelJME Provided JNI headers
target_include_directories(libJvm PUBLIC
	"${CMAKE_SOURCE_DIR}/include/sjme/jvm/")

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
	"jvm")
squirreljme_target_binary_name(SquirrelJME
	"squirreljme")

# It is fine to have the static library be a completely different name
squirreljme_target_binary_name(libJvmStatic
	"squirreljme")

# Really only matters for dynamic libraries
if(SQUIRRELJME_ENABLE_DYLIB)
	# Output the dynamic library to where it should be
	squirreljme_target_binary_output(libJvmDyLib
		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

	# Do not set a SONAME for libjvm library as it could be either
	# libsquirreljme or libjvm, and this would break any kind of interop
	#squirreljme_no_soname(libJvmDyLib)

	# Exports for Windows, etc.
	squirreljme_target_shared_library_exports(libJvmDyLib)
endif()

# Export/Install JVM static/shared
squirreljme_multilib_export_install(libJvm)

# Exporting a JVM/JNI implementation?
if(NOT SQUIRRELJME_EXPORT_OWN_JNI_JVM OR SQUIRRELJME_ENABLE_DYLIB)
	# Declare library
	add_library(discoveredJvm INTERFACE IMPORTED GLOBAL)

	# Includes
	target_include_directories(discoveredJvm INTERFACE
		"${SQUIRRELJME_JAVA_JNI_INCLUDE}"
		"${SQUIRRELJME_JAVA_JVM_INCLUDE}"
		"${CMAKE_SOURCE_DIR}/include/sjme/jvm")

	# Create a virtual JNI export for the system
	if(NOT SQUIRRELJME_EXPORT_OWN_JNI_JVM)
		# Libraries
		target_link_libraries(discoveredJvm INTERFACE
			"${SQUIRRELJME_JAVA_JVM_LIBRARY}")
	else(SQUIRRELJME_ENABLE_DYLIB)
		# Libraries
		target_link_libraries(discoveredJvm INTERFACE
			libJvmDyLib)
	endif()

	# Allow the JVM export to be used externally
	set_target_properties(discoveredJvm PROPERTIES
		LINKER_LANGUAGE C)
	export(TARGETS discoveredJvm
		FILE "${CMAKE_BINARY_DIR}/discoveredJvm.cmake"
		EXPORT_LINK_INTERFACE_LIBRARIES)
endif()

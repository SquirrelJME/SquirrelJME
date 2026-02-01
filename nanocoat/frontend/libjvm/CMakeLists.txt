# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Define library
squirreljme_multilib_add_library(libJvm
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

# If using official JNI/JVM headers we need this definition to be set
squirreljme_multilib_compile_definitions(libJvm PRIVATE
	SJME_JNI_IMPLEMENTATION=1)

# We need to find all of the includes and such
squirreljme_multilib_include_directories(libJvm PRIVATE
	"${CMAKE_SOURCE_DIR}/include")
squirreljme_multilib_include_directories(libJvm PRIVATE
	"${SQUIRRELJME_JAVA_JNI_INCLUDE}"
	"${SQUIRRELJME_JAVA_JVM_INCLUDE}"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/jni")

# Public SquirrelJME Provided JNI headers
target_include_directories(libJvm PUBLIC
	"${CMAKE_SOURCE_DIR}/include/sjme/jvm/")

# Libraries needed to be linked with for the DyLib to function
squirreljme_multilib_link_libraries_required(libJvm PUBLIC
	CoreStatic
	ScritchAudioCoreStatic
	ScritchUiCoreStatic)

# Specify name for the JVM library
squirreljme_multilib_binary_name(libJvm
	"jvm")

# It is fine to have the static library be a completely different name
squirreljme_target_binary_name(libJvmStatic
	"squirreljme")

# Output everything here
squirreljme_multilib_binary_output(libJvm
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Exports for Windows, etc.
squirreljme_target_shared_library_exports(libJvmDyLib)

# Export/Install JVM static/shared
squirreljme_multilib_install(libJvm)

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
		squirreljme_link_libraries(discoveredJvm INTERFACE
			"${SQUIRRELJME_JAVA_JVM_LIBRARY}")
	else(SQUIRRELJME_ENABLE_DYLIB)
		# Libraries
		squirreljme_link_libraries(discoveredJvm INTERFACE
			libJvmDyLib)
	endif()

	# Allow the JVM export to be used externally
	set_target_properties(discoveredJvm PROPERTIES
		LINKER_LANGUAGE C)
	export(TARGETS discoveredJvm
		FILE "${CMAKE_BINARY_DIR}/discoveredJvm.cmake"
		EXPORT_LINK_INTERFACE_LIBRARIES)
endif()

# Executable launcher
add_executable(SquirrelJME
	lnStart.c)

# Statically link the JVM
squirreljme_link_libraries_required(SquirrelJME PRIVATE
	libJvmStatic)

# Definitions
target_compile_definitions(SquirrelJME PRIVATE
	SJME_JNI_IMPLEMENTATION=1)

# Includes
target_include_directories(SquirrelJME PRIVATE
	"${CMAKE_SOURCE_DIR}/include")
target_include_directories(SquirrelJME PRIVATE
	"${SQUIRRELJME_JAVA_JNI_INCLUDE}"
	"${SQUIRRELJME_JAVA_JVM_INCLUDE}"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/jni")

# Where should the executable be output?
squirreljme_target_binary_output(SquirrelJME
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Use lowercase name for the wrapper
squirreljme_target_binary_name(SquirrelJME
	"squirreljme")

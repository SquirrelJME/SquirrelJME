# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Attempts to find JNI and related headers

# Find headers
include(CheckIncludeFile)

# If JAVA_HOME is set it needs to be correctly a CMake path, otherwise FindJNI
# will probably just break
if(DEFINED JAVA_HOME)
	file(TO_CMAKE_PATH "${JAVA_HOME}" JAVA_HOME)
elseif(DEFINED ENV{JAVA_HOME})
	file(TO_CMAKE_PATH "$ENV{JAVA_HOME}" JAVA_HOME)
endif()

# Use self-hosted libjvm?
if(SQUIRRELJME_SELF_HOSTED_LIBJVM)
	# Export our own JNI interfaces
	set(SQUIRRELJME_EXPORT_OWN_JNI_JVM TRUE)

	# Set includes
	set(SQUIRRELJME_JAVA_JNI_INCLUDE
		"${CMAKE_CURRENT_LIST_DIR}/../include/sjme/jvm")
	set(SQUIRRELJME_JAVA_JVM_INCLUDE
		"${CMAKE_CURRENT_LIST_DIR}/../include/sjme/jvm")

	# We mostly have these
	set(SQUIRRELJME_HAS_JAVA_JNI MOSTLY)
	set(SQUIRRELJME_HAS_JAVA_JVM MOSTLY)

# If not cross compiled, try using the system's JVM implementation
elseif(NOT SQUIRRELJME_IS_CROSS_COMPILE)
	# Notice
	message(STATUS "Checking if CMake's FindJNI() is broken...")

	# Use standard JNI search from CMake, however it is very possible that it
	# is broken if your CMake is old enough and your environment is not
	# configured correctly
	unset(jniConfigureResult)
	file(MAKE_DIRECTORY "${CMAKE_BINARY_DIR}/check-find-jni")
	execute_process(COMMAND "${CMAKE_COMMAND}"
			"-DJAVA_HOME=${JAVA_HOME}"
			"${CMAKE_CURRENT_LIST_DIR}/find-jni"
		WORKING_DIRECTORY "${CMAKE_BINARY_DIR}/check-find-jni"
		OUTPUT_VARIABLE jniConfigureOutput
		RESULT_VARIABLE jniConfigureResult)

	# Only if it is valid should it be used
	if("${jniConfigureResult}" EQUAL "0")
		# Notice!
		message(STATUS "CMake FindJNI() operational, using that!")

		# Actually use it
		find_package(JNI QUIET)
	else()
		# Otherwise indicate brokenness
		message(STATUS "CMake's FindJNI() is actually broken...")
	endif()

	# Debug, because you never know...
	message(STATUS "CMake FindJNI(): ${jniConfigureOutput}")

	# Found something?
	if(JNI_FOUND)
		# Is there native JNI?
		set(CMAKE_REQUIRED_INCLUDES
			"${JNI_INCLUDE_DIRS};${JAVA_INCLUDE_PATH};${JAVA_INCLUDE_PATH2}")
		CHECK_INCLUDE_FILE("jni.h" SQUIRRELJME_HAS_NATIVE_JNI)
		unset(CMAKE_REQUIRED_INCLUDES)

		# Is there native JVM?
		set(CMAKE_REQUIRED_INCLUDES
			"${JNI_INCLUDE_DIRS};${JAVA_INCLUDE_PATH};${JAVA_INCLUDE_PATH2}")
		CHECK_INCLUDE_FILE("jvm.h" SQUIRRELJME_HAS_NATIVE_JVM)
		unset(CMAKE_REQUIRED_INCLUDES)

		# Direct path to the JVM library?
		if(JAVA_JVM_LIBRARY)
			set(SQUIRRELJME_JAVA_JVM_LIBRARY
				"${JAVA_JVM_LIBRARY}")
		endif()

		# Were there actual JNI headers?
		if(SQUIRRELJME_HAS_NATIVE_JNI)
			# Strip NOTFOUND
			squirreljme_notfound_strip(JNI_INCLUDE_DIRS)
			squirreljme_notfound_strip(JAVA_INCLUDE_PATH)
			squirreljme_notfound_strip(JAVA_INCLUDE_PATH2)

			# Set include
			list(APPEND SQUIRRELJME_JAVA_JNI_INCLUDE
				"${JNI_INCLUDE_DIRS}"
				"${JAVA_INCLUDE_PATH}"
				"${JAVA_INCLUDE_PATH2}")

			# Say that we do have it
			set(SQUIRRELJME_HAS_JAVA_JNI TRUE)
		endif()

		# Were there actual JVM headers?
		if(SQUIRRELJME_HAS_NATIVE_JVM)
			# Strip NOTFOUND
			squirreljme_notfound_strip(JNI_INCLUDE_DIRS)
			squirreljme_notfound_strip(JAVA_INCLUDE_PATH)
			squirreljme_notfound_strip(JAVA_INCLUDE_PATH2)

			# Set include
			list(APPEND SQUIRRELJME_JAVA_JVM_INCLUDE
				"${JNI_INCLUDE_DIRS}"
				"${JAVA_INCLUDE_PATH}"
				"${JAVA_INCLUDE_PATH2}")

			# Say that we do have it
			set(SQUIRRELJME_HAS_JAVA_JVM TRUE)
		endif()
	endif()

	# Do not export own JNI/JVM
	set(SQUIRRELJME_EXPORT_OWN_JNI_JVM FALSE)

# Otherwise emit a notice due to cross-compile
else()
	set(SQUIRRELJME_EXPORT_OWN_JNI_JVM TRUE)
	message(STATUS "Not using the system JNI due to cross-compilation!")
endif()

# Do not use Sun's/OpenJDK's headers if we are self-hosted libjvm
if(NOT SQUIRRELJME_SELF_HOSTED_LIBJVM)
	# If JNI was not found, use our own
	if(NOT SQUIRRELJME_HAS_JAVA_JNI)
		# Set include
		set(SQUIRRELJME_JAVA_JNI_INCLUDE
			"${CMAKE_CURRENT_LIST_DIR}/../include/3rdparty/jni")

		# Say that we do have it
		set(SQUIRRELJME_HAS_JAVA_JNI MOSTLY)
	endif()

	# If JVM was not found, use our own
	if(NOT SQUIRRELJME_HAS_JAVA_JVM)
		# Set include
		set(SQUIRRELJME_JAVA_JVM_INCLUDE
			"${CMAKE_CURRENT_LIST_DIR}/../include/3rdparty/jni")

		# Say that we do have it
		set(SQUIRRELJME_HAS_JAVA_JVM MOSTLY)
	endif()
endif()

# If using own JVM library and there is no library set, force one
if(SQUIRRELJME_EXPORT_OWN_JNI_JVM)
	# Which library is being chosen?
	if("${SQUIRRELJME_SYSTEM}" STREQUAL "windows")
		set(soName "${CMAKE_IMPORT_LIBRARY_PREFIX}jvm")
		set(soName "${soName}${CMAKE_STATIC_LIBRARY_SUFFIX}")
	else()
		set(soName "${CMAKE_SHARED_LIBRARY_PREFIX}jvm")
		set(soName "${soName}${CMAKE_SHARED_LIBRARY_SUFFIX}")
	endif()

	# Where is the library?
	if(NOT "${SQUIRRELJME_DYLIB_OUTPUT_DIR}" STREQUAL "")
		set(soDir "${SQUIRRELJME_DYLIB_OUTPUT_DIR}")
	elseif(NOT "${SQUIRRELJME_BINARY_OUTPUT_ROOT}" STREQUAL "")
		set(soDir "${SQUIRRELJME_BINARY_OUTPUT_ROOT}")
	elseif(NOT "${SQUIRRELJME_NANOCOAT_BINARY_DIR}" STREQUAL "")
		set(soDir "${SQUIRRELJME_NANOCOAT_BINARY_DIR}/bin")
	else()
		set(soDir "${CMAKE_BINARY_DIR}/bin")
	endif()

	# Set library to use
	set(SQUIRRELJME_JVM_LIBRARY "${soDir}/${soName}")
else()
	set(SQUIRRELJME_JVM_LIBRARY "")
endif()

# Use own library instead?
if(SQUIRRELJME_EXPORT_OWN_JNI_JVM AND
	"${JAVA_JVM_LIBRARY}" STREQUAL "" AND
	NOT "${SQUIRRELJME_JVM_LIBRARY}" STREQUAL "")
	set(JAVA_JVM_LIBRARY "${SQUIRRELJME_JVM_LIBRARY}")
endif()

# Emit whatever was found of this
message(STATUS "Has JNI: ${SQUIRRELJME_HAS_JAVA_JNI}")
message(STATUS "JNI Include: ${SQUIRRELJME_JAVA_JNI_INCLUDE}")
message(STATUS "Has JVM: ${SQUIRRELJME_HAS_JAVA_JVM}")
message(STATUS "JVM Include: ${SQUIRRELJME_JAVA_JVM_INCLUDE}")
message(STATUS "Use SquirrelJME libjvm? ${SQUIRRELJME_EXPORT_OWN_JNI_JVM}")
message(STATUS "Self-hosted libjvm? ${SQUIRRELJME_SELF_HOSTED_LIBJVM}")

# More info on libraries
message(STATUS "CMake JVM Library: ${JAVA_JVM_LIBRARY}")
message(STATUS "CMake JVM Include: ${JNI_INCLUDE_DIRS}")
message(STATUS "CMake JVM Include: ${JAVA_INCLUDE_PATH}")
message(STATUS "CMake JVM Include: ${JAVA_INCLUDE_PATH2}")

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

# If not cross compiled, try using the system's JVM implementation
if(NOT SQUIRRELJME_IS_CROSS_COMPILE)
	# Notice
	message(STATUS "Checking if CMake's FindJNI() is broken...")

	# Use standard JNI search from CMake, however it is very possible that it
	# is broken if your CMake is old enough and your environment is not
	# configured correctly
	file(MAKE_DIRECTORY "${CMAKE_BINARY_DIR}/check-find-jni")
	execute_process(COMMAND "${CMAKE_COMMAND}"
			"-DJAVA_HOME=${JAVA_HOME}"
			"${CMAKE_CURRENT_LIST_DIR}/find-jni"
		WORKING_DIRECTORY "${CMAKE_BINARY_DIR}/check-find-jni"
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

# Emit whatever was found of this
message(STATUS "Has JNI: ${SQUIRRELJME_HAS_JAVA_JNI}")
message(STATUS "JNI Include: ${SQUIRRELJME_JAVA_JNI_INCLUDE}")
message(STATUS "Has JVM: ${SQUIRRELJME_HAS_JAVA_JVM}")
message(STATUS "JVM Include: ${SQUIRRELJME_JAVA_JVM_INCLUDE}")
message(STATUS "Use SquirrelJME libjvm? ${SQUIRRELJME_EXPORT_OWN_JNI_JVM}")

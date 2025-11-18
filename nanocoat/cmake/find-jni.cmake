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
	# Use standard JNI search
	find_package(JNI QUIET)

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

		# Were there actual JNI headers?
		if(SQUIRRELJME_HAS_NATIVE_JNI)
			# Set include
			set(SQUIRRELJME_JAVA_JNI_INCLUDE
				"${JNI_INCLUDE_DIRS};${JAVA_INCLUDE_PATH};${JAVA_INCLUDE_PATH2}")

			# Say that we do have it
			set(SQUIRRELJME_HAS_JAVA_JNI YES)
		endif()

		# Were there actual JVM headers?
		if(SQUIRRELJME_HAS_NATIVE_JVM)
			# Set include
			set(SQUIRRELJME_JAVA_JVM_INCLUDE
				"${JNI_INCLUDE_DIRS};${JAVA_INCLUDE_PATH};${JAVA_INCLUDE_PATH2}")

			# Say that we do have it
			set(SQUIRRELJME_HAS_JAVA_JVM YES)
		endif()
	endif()
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

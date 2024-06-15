# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Attempts to find JNI and related headers

# Use standard JNI search
find_package(JNI QUIET)

# If JNI was not found, use a local copy as a fallback
if(NOT JNI_FOUND)
	# Where are the headers?
	set(JNI_INCLUDE_DIRS
		"${CMAKE_SOURCE_DIR}/include/3rdparty/jni")

	# Set as found
	set(JNI_FOUND ON)
endif()

# Debugging
message(STATUS "JNI Found? ${JNI_FOUND}")

# Enable JAWT?
if(JNI_FOUND)
	message(STATUS "JAWT Library: ${JAVA_AWT_LIBRARY}")
	message(STATUS "JAWT Include: ${JAVA_AWT_INCLUDE_PATH}")

	if(NOT "${JAVA_AWT_LIBRARY}" STREQUAL "" OR
		NOT "${JAVA_AWT_INCLUDE_PATH}" STREQUAL "")
		# Set JAWT was found, set this if it was not set since on older CMake
		# this will not be set despite being technically valid
		if(NOT DEFINED JNI_AWT_FOUND)
			set(JNI_AWT_FOUND TRUE)
		endif()
	endif()
endif()

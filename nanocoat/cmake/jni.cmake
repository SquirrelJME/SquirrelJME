# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Attempts to find JNI and related headers

# Where are we?
if(NOT DEFINED SQUIRRELJME_JNI_CMAKE_WHERE)
	set(SQUIRRELJME_JNI_CMAKE_WHERE "${CMAKE_CURRENT_LIST_DIR}")
endif()

if(NOT DEFINED SQUIRRELJME_JNI_CMAKE_WHERE)
	set(SQUIRRELJME_JNI_CMAKE_WHERE "${CMAKE_SOURCE_DIR}")
endif()

# Use standard JNI search
find_package(JNI QUIET)

# If JNI was not found, use a local copy as a fallback
if(NOT JNI_FOUND)
	# Set as found
	set(JNI_FOUND ON)

	# Where are the headers?
	set(JNI_INCLUDE_DIRS
		"${SQUIRRELJME_JNI_CMAKE_WHERE}/../include/3rdparty/jni")
	set(JAVA_AWT_INCLUDE_PATH
		"${SQUIRRELJME_JNI_CMAKE_WHERE}/../include/3rdparty/jni")
endif()

# Debugging
message(STATUS "JNI Found? ${JNI_FOUND}")

# Enable JAWT?
if(JNI_FOUND)
	if(NOT "${JAVA_AWT_LIBRARY}" STREQUAL "" OR
		NOT "${JAVA_AWT_INCLUDE_PATH}" STREQUAL "")
		# Set JAWT was found, set this if it was not set since on older CMake
		# this will not be set despite being technically valid
		if(NOT DEFINED JNI_AWT_FOUND)
			# Use fallbacks
			set(JAVA_AWT_INCLUDE_PATH
				"${SQUIRRELJME_JNI_CMAKE_WHERE}/../include/3rdparty/jni")
		endif()
	endif()
endif()

# Try compiling with JNI
if(JAVA_JVM_LIBRARY)
	try_compile(SQUIRRELJME_FOUND_JNI_VALID
		"${CMAKE_CURRENT_BINARY_DIR}"
		SOURCES "${CMAKE_CURRENT_LIST_DIR}/tryJni.c"
		CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
		LINK_LIBRARIES ${JAVA_JVM_LIBRARY}
		OUTPUT_VARIABLE SQUIRRELJME_FOUND_JNI_VALID_OUTPUT)
	message("JNI Valid?: ${SQUIRRELJME_FOUND_JNI_VALID_OUTPUT}")
endif()

# Not valid?
if(NOT SQUIRRELJME_FOUND_JNI_VALID)
	unset(JNI_FOUND)
	unset(JNI_INCLUDE_DIRS)
	unset(JAVA_JVM_LIBRARY)
endif()

# Do we need a library stub?
## For JNI
if(DEFINED JAVA_JVM_LIBRARY-NOTFOUND OR NOT DEFINED JAVA_JVM_LIBRARY)
	# Make sure these are cleared
	unset(JAVA_JVM_LIBRARY-NOTFOUND)
	unset(JAVA_JVM_LIBRARY-NOTFOUND CACHE)

	# Use stubbed version
	set(JAVA_JVM_LIBRARY
		"${SQUIRRELJME_UTIL_DIR}/${SQUIRRELJME_HOST_DYLIB_PREFIX}jvm${SQUIRRELJME_HOST_DYLIB_SUFFIX}")
endif()

## For JAWT
if(DEFINED JAVA_AWT_LIBRARY-NOTFOUND OR NOT DEFINED JAVA_AWT_LIBRARY)
	unset(JAVA_AWT_LIBRARY-NOTFOUND)
	unset(JAVA_AWT_LIBRARY-NOTFOUND CACHE)

	# Use stubbed version
	set(JAVA_JVM_LIBRARY
		"${SQUIRRELJME_UTIL_DIR}/${SQUIRRELJME_HOST_DYLIB_PREFIX}jawt${SQUIRRELJME_HOST_DYLIB_SUFFIX}")
endif()

if(NOT DEFINED JNI_INCLUDE_DIRS)
	set(JNI_INCLUDE_DIRS
		"${SQUIRRELJME_JNI_CMAKE_WHERE}/../include/3rdparty/jni")
endif()

if(NOT DEFINED JAVA_AWT_INCLUDE_PATH)
	set(JAVA_AWT_INCLUDE_PATH
		"${SQUIRRELJME_JNI_CMAKE_WHERE}/../include/3rdparty/jni")
endif()

# Debugging
message(STATUS "JNI Library: ${JAVA_JVM_LIBRARY}")
message(STATUS "JNI Include: ${JNI_INCLUDE_DIRS}")
message(STATUS "JAWT Library: ${JAVA_AWT_LIBRARY}")
message(STATUS "JAWT Include: ${JAVA_AWT_INCLUDE_PATH}")

# After everything, this will always be true
if(NOT JNI_FOUND)
	set(JNI_FOUND TRUE)
endif()

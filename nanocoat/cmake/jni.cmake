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

# If found, use specific host variables then remove them all
if(JNI_FOUND)
	set(HOST_JNI_FOUND "${JNI_FOUND}")
	unset(JNI_FOUND)

	if(DEFINED JNI_INCLUDE_DIRS)
		set(HOST_JNI_INCLUDE_DIRS "${JNI_INCLUDE_DIRS}")
		unset(JNI_INCLUDE_DIRS)
	endif()

	if(DEFINED JAVA_INCLUDE_PATH)
		set(HOST_JAVA_INCLUDE_PATH "${JAVA_INCLUDE_PATH}")
		unset(JAVA_INCLUDE_PATH)
	endif()

	if(DEFINED JAVA_INCLUDE_PATH2)
		set(HOST_JAVA_INCLUDE_PATH2 "${JAVA_INCLUDE_PATH2}")
		unset(JAVA_INCLUDE_PATH2)
	endif()

	if(DEFINED JAVA_AWT_INCLUDE_PATH)
		set(HOST_JAVA_AWT_INCLUDE_PATH "${JAVA_AWT_INCLUDE_PATH}")
		unset(JAVA_AWT_INCLUDE_PATH)
	endif()

	if(DEFINED JAVA_JVM_LIBRARY)
		set(HOST_JAVA_JVM_LIBRARY "${JAVA_JVM_LIBRARY}")
		unset(JAVA_JVM_LIBRARY)
	endif()

	if(DEFINED JAVA_AWT_LIBRARY)
		set(HOST_JAVA_AWT_LIBRARY "${JAVA_AWT_LIBRARY}")
		unset(JAVA_AWT_LIBRARY)
	endif()
endif()

# Clear always the NOTFOUND variables
unset(JNI_INCLUDE_DIRS-NOTFOUND)
unset(JAVA_JVM_LIBRARY-NOTFOUND)
unset(JAVA_INCLUDE_PATH-NOTFOUND)
unset(JAVA_INCLUDE_PATH2-NOTFOUND)
unset(JAVA_AWT_LIBRARY-NOTFOUND)
unset(JAVA_AWT_INCLUDE_PATH-NOTFOUND)
unset(JNI_INCLUDE_DIRS-NOTFOUND CACHE)
unset(JAVA_JVM_LIBRARY-NOTFOUND CACHE)
unset(JAVA_INCLUDE_PATH-NOTFOUND CACHE)
unset(JAVA_INCLUDE_PATH2-NOTFOUND CACHE)
unset(JAVA_AWT_LIBRARY-NOTFOUND CACHE)
unset(JAVA_AWT_INCLUDE_PATH-NOTFOUND CACHE)

# Check to see if the host can compile or not
if(HOST_JNI_FOUND)
	# JVM?
	try_compile(SQUIRRELJME_HOST_JVM_VALID
		"${CMAKE_CURRENT_BINARY_DIR}"
		SOURCES "${CMAKE_CURRENT_LIST_DIR}/tryJni.c"
		CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
		LINK_LIBRARIES "${JAVA_JVM_LIBRARY}"
		OUTPUT_VARIABLE SQUIRRELJME_HOST_JVM_VALID_DEBUG)
	message(STATUS "JNI Valid?: ${SQUIRRELJME_HOST_JVM_VALID}")
	message(NOTICE "${SQUIRRELJME_HOST_JVM_VALID_DEBUG}")

	# JAWT?
	try_compile(SQUIRRELJME_HOST_AWT_VALID
		"${CMAKE_CURRENT_BINARY_DIR}"
		SOURCES "${CMAKE_CURRENT_LIST_DIR}/tryJawt.c"
		CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
		LINK_LIBRARIES "${JAVA_JVM_LIBRARY}" "${JAVA_AWT_LIBRARY}"
		OUTPUT_VARIABLE SQUIRRELJME_HOST_AWT_VALID_DEBUG)
	message(STATUS "JAWT Valid?: ${SQUIRRELJME_HOST_AWT_VALID}")
	message(NOTICE "${SQUIRRELJME_HOST_AWT_VALID_DEBUG}")
else()
	set(SQUIRRELJME_HOST_JVM_VALID NO)
	set(SQUIRRELJME_HOST_AWT_VALID NO)
endif()

# JNI is always valid
set(JNI_FOUND YES)

# Use the host JNI or our own?
if(SQUIRRELJME_HOST_JVM_VALID)
	# Includes
	set(JNI_INCLUDE_DIRS "${HOST_JAVA_INCLUDE_PATH}")
	set(JAVA_INCLUDE_PATH "${HOST_JAVA_INCLUDE_PATH}")
	set(JAVA_INCLUDE_PATH2 "${HOST_JAVA_INCLUDE_PATH2}")

	# Libraries
	set(JAVA_JVM_LIBRARY "${HOST_JAVA_JVM_LIBRARY}")

# Use our own
else()
	# Includes
	set(JNI_INCLUDE_DIRS
		"${SQUIRRELJME_JNI_CMAKE_WHERE}/../include/3rdparty/jni")
	set(JAVA_INCLUDE_PATH
		"${SQUIRRELJME_JNI_CMAKE_WHERE}/../include/3rdparty/jni")
	set(JAVA_INCLUDE_PATH2
		"${SQUIRRELJME_JNI_CMAKE_WHERE}/../include/3rdparty/jni")

	# Use stubbed libraries
	set(JAVA_JVM_LIBRARY
		"${SQUIRRELJME_UTIL_DIR}/${SQUIRRELJME_HOST_DYLIB_PREFIX}jvm${SQUIRRELJME_HOST_DYLIB_SUFFIX}")
endif()

# Use host AWT?
if(SQUIRRELJME_HOST_AWT_VALID)
	# Includes
	set(JAVA_AWT_INCLUDE_PATH "${HOST_JAVA_AWT_INCLUDE_PATH}")

	# Libraries
	set(JAVA_AWT_LIBRARY "${HOST_JAVA_AWT_LIBRARY}")

# Use our own
else()
	# Includes
	set(JAVA_AWT_INCLUDE_PATH
		"${SQUIRRELJME_JNI_CMAKE_WHERE}/../include/3rdparty/jni")

	# Stubbed library
	set(JAVA_AWT_LIBRARY
		"${SQUIRRELJME_UTIL_DIR}/${SQUIRRELJME_HOST_DYLIB_PREFIX}jawt${SQUIRRELJME_HOST_DYLIB_SUFFIX}")
endif()

# Debugging
message(STATUS "JNI Include: ${JNI_INCLUDE_DIRS}")
message(STATUS "JVM Library: ${JAVA_JVM_LIBRARY}")
message(STATUS "JVM Include 1: ${JAVA_INCLUDE_PATH}")
message(STATUS "JVM Include 2: ${JAVA_INCLUDE_PATH2}")
message(STATUS "JAWT Library: ${JAVA_AWT_LIBRARY}")
message(STATUS "JAWT Include: ${JAVA_AWT_INCLUDE_PATH}")

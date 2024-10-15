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

# Used to remove any NOTFOUNDs from variables
macro(squirreljme_notfound_strip var)
	unset(${var}-NOTFOUND)
	unset(${var}-NOTFOUND CACHE)

	if (${var} MATCHES "-NOTFOUND$")
		unset(${var})
		unset(${var} CACHE)
	endif()

	if("${CMAKE_VERSION}" VERSION_GREATER_EQUAL "3.13")
		if("$CACHE{${var}}" MATCHES "-NOTFOUND$")
			unset(${var} CACHE)
		endif()
	endif()
endmacro()

# Clear always the NOTFOUND variables, since these will cause CMake to
# just fail if it is missing
squirreljme_notfound_strip(JNI_INCLUDE_DIRS)
squirreljme_notfound_strip(JAVA_JVM_LIBRARY)
squirreljme_notfound_strip(JAVA_INCLUDE_PATH)
squirreljme_notfound_strip(JAVA_INCLUDE_PATH2)
squirreljme_notfound_strip(JAVA_AWT_LIBRARY)
squirreljme_notfound_strip(JAVA_AWT_INCLUDE_PATH)

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
	message(DEBUG "${SQUIRRELJME_HOST_JVM_VALID_DEBUG}")

	# JAWT?
	try_compile(SQUIRRELJME_HOST_AWT_VALID
		"${CMAKE_CURRENT_BINARY_DIR}"
		SOURCES "${CMAKE_CURRENT_LIST_DIR}/tryJawt.c"
		CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
		LINK_LIBRARIES "${JAVA_JVM_LIBRARY}" "${JAVA_AWT_LIBRARY}"
		OUTPUT_VARIABLE SQUIRRELJME_HOST_AWT_VALID_DEBUG)
	message(STATUS "JAWT Valid?: ${SQUIRRELJME_HOST_AWT_VALID}")
	message(DEBUG "${SQUIRRELJME_HOST_AWT_VALID_DEBUG}")

	# It is possible that there are other libraries but there
	# is no jni_md.h, which is a broken installation of JNI
	find_file(SQUIRRELJME_HOST_JNI_MD "jni_md.h"
		"${HOST_JNI_INCLUDE_DIRS}"
		"${HOST_JAVA_INCLUDE_PATH}"
		"${HOST_JAVA_INCLUDE_PATH2}")
	if(SQUIRRELJME_HOST_JNI_MD)
		set(SQUIRRELJME_HOST_JNI_MD_VALID YES)
	else()
		set(SQUIRRELJME_HOST_JNI_MD_VALID NO)
	endif()
	message(STATUS "jni_md.h Valid?: ${SQUIRRELJME_HOST_JNI_MD_VALID}")
	message(DEBUG "jni_md.h path: ${SQUIRRELJME_HOST_JNI_MD}")
else()
	set(SQUIRRELJME_HOST_JVM_VALID NO)
	set(SQUIRRELJME_HOST_JNI_MD_VALID NO)
	set(SQUIRRELJME_HOST_AWT_VALID NO)
endif()

# JNI is always valid
set(JNI_FOUND YES)

# Use the host JNI or our own?
if(SQUIRRELJME_HOST_JVM_VALID AND SQUIRRELJME_HOST_JNI_MD_VALID)
	# Includes
	set(JNI_INCLUDE_DIRS "${HOST_JNI_INCLUDE_DIRS}")
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
	squirreljme_library_set(JAVA_JVM_LIBRARY jvm)
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
	squirreljme_library_set(JAVA_AWT_LIBRARY jawt)
endif()

# Debugging
message(STATUS "JNI Include: ${JNI_INCLUDE_DIRS}")
message(STATUS "JVM Library: ${JAVA_JVM_LIBRARY}")
message(STATUS "JVM Include 1: ${JAVA_INCLUDE_PATH}")
message(STATUS "JVM Include 2: ${JAVA_INCLUDE_PATH2}")
message(STATUS "JAWT Library: ${JAVA_AWT_LIBRARY}")
message(STATUS "JAWT Include: ${JAVA_AWT_INCLUDE_PATH}")

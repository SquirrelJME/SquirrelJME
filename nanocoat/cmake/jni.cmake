# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Attempts to find JNI

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

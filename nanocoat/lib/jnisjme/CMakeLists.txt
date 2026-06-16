# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare JNI/SquirrelJME Bridge
squirreljme_multilib_add_library(libJniSJME
	jsInputStream.c)

# Include main headers, and the appropriate JNI as well
squirreljme_multilib_include_directories(libJniSJME PUBLIC
	"${CMAKE_SOURCE_DIR}/include")
target_include_directories(libJniSJME PRIVATE
	"${SQUIRRELJME_JAVA_JNI_INCLUDE}"
	"${SQUIRRELJME_JAVA_JVM_INCLUDE}"
	"${CMAKE_SOURCE_DIR}/include/3rdparty/jni")

# This library goes here
squirreljme_multilib_binary_output(libJniSJME
	"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")

# Required system libraries
squirreljme_multilib_link_libraries_required(libJniSJME NONE)

# This bridge library is rather useful
squirreljme_multilib_export(libJniSJME)
squirreljme_multilib_install(libJniSJME)

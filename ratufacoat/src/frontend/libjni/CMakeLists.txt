# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SquirrelJME Native Java Native Interface wrapper
# Compatible with standard Java runtimes which use JNI for their
# implementations.

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Need all of the objects that make up the native JNI library
get_property(SQUIRRELJMEJNILIB_TARGET_OBJECTS
	GLOBAL PROPERTY SquirrelJMECoreTargetObjects)

# Declare static library
add_library(SquirrelJMELibJNI SHARED
	nativejni.c
	${SQUIRRELJMEJNILIB_TARGET_OBJECTS})

# Change the name of the library
set_target_properties(SquirrelJMELibJNI PROPERTIES
	OUTPUT_NAME "squirreljmejvmjni")

# This is written only in C!
set_target_properties(SquirrelJMELibJNI PROPERTIES LINKER_LANGUAGE C)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMELibJNI PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMELibJNI
	PROPERTY POSITION_INDEPENDENT_CODE ${SQUIRRELJME_FPIC_MODE})

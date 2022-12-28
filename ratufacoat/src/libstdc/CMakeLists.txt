# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Standard C library, if one is needed by a platform.

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Define the library
add_library(SquirrelJMELibStdC OBJECT
	stdio.c
	stdlib.c
	string.c)

# This is written only in C!
set_target_properties(SquirrelJMELibStdC PROPERTIES LINKER_LANGUAGE C)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMELibStdC PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include"
	"${PROJECT_SOURCE_DIR}/include/libstdc")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMELibStdC
	PROPERTY POSITION_INDEPENDENT_CODE ${SQUIRRELJME_FPIC_MODE})


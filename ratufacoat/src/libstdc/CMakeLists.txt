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

# Common C Directives
squirreljme_common_c(SquirrelJMELibStdC)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMELibStdC PUBLIC
	"${PROJECT_SOURCE_DIR}/include/libstdc")

# Make this position independent so it can be linked into shared libraries
squirreljme_enable_pic(SquirrelJMELibStdC)


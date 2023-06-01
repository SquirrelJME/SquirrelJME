# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Engine Base

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Define the library
add_library(SquirrelJMEEngine OBJECT
	buffer.c
	classloader.c
	file.c
	pipe.c
	profiler.c
	scaffold.c
	taskmanager.c)

# Common C Directives
squirreljme_common_c(SquirrelJMEEngine)

# Make this position independent so it can be linked into shared libraries
squirreljme_enable_pic(SquirrelJMEEngine)

# Engine implementations
add_subdirectory(springcoat)

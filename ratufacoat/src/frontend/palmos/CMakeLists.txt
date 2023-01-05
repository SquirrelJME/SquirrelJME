# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: PalmOS Base Support

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Setup executable
add_library(SquirrelJMEPalmOsLib OBJECT
	stdc.c)

# Common C Directives
squirreljme_common_c(SquirrelJMEPalmOsLib)

# Position independent code always
squirreljme_enable_pic(SquirrelJMEPalmOsLib)

# Needed by platform specific targets
squirreljme_object_and_sources(SquirrelJMEPalmOsLibTarget
	SquirrelJMEPalmOsLib
	SquirrelJMELibStdC)

# m68k?
if(CMAKE_SYSTEM_PROCESSOR STREQUAL "m68k")
	add_subdirectory("m68k")
endif()

# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: PalmOS m68k 1.0-5.0

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Include both core sets of libraries
get_property(SQUIRRELJME_PALMOS_M68K_CORE
	GLOBAL PROPERTY SquirrelJMECoreTargetObjects)
get_property(SQUIRRELJME_PALMOS_M68K_FRONTEND_LIB
	GLOBAL PROPERTY SquirrelJMEPalmOsLibTargetObjects)

# Add executable here
add_executable(SquirrelJMEPalmOsM68k
	boot.c
	${SQUIRRELJME_PALMOS_M68K_CORE}
	${SQUIRRELJME_PALMOS_M68K_FRONTEND_LIB})

# The ELF is not used at all, but it gets turned into a bunch of objects
set_target_properties(SquirrelJMEPalmOsM68k PROPERTIES
	OUTPUT_NAME "squirreljme.elf")
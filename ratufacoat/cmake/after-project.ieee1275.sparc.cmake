# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake adds -g and -fPIC to ASM_FLAGS when it should not

# Disable debugging for IEEE1275 SPARC because it is broken in CMake
if(CMAKE_SYSTEM_NAME STREQUAL "ieee1275" AND
	CMAKE_SYSTEM_PROCESSOR STREQUAL "sparc")
	set(CMAKE_ASM_FLAGS "" CACHE STRING "ASM Flags" FORCE)
	set(CMAKE_ASM_FLAGS_DEBUG "" CACHE STRING "ASM Flags Debug" FORCE)
	set(CMAKE_ASM_FLAGS_DEBUG_INIT "" CACHE STRING "ASM Debug Init" FORCE)

	set(CMAKE_ASM_FLAGS "")
	set(CMAKE_ASM_FLAGS_DEBUG "")
	set(CMAKE_ASM_FLAGS_DEBUG_INIT "")
endif()

# Disable -fPIC completely, since the executable is always in the same location
set(SQUIRRELJME_FPIC_MODE OFF)

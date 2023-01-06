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

# Compiles resources into a resource library
set(SQUIRRELJME_PALMOS_RESOURCE_LIB_OUTPUT
	"${CMAKE_CURRENT_BINARY_DIR}/squirreljme-rc.ro")
add_custom_target(SquirrelJMEPalmOsResources
	COMMAND "pilrc"
		-ro
		-o "${SQUIRRELJME_PALMOS_RESOURCE_LIB_OUTPUT}"
		-D "SQUIRRELJME_VERSION=${SQUIRRELJME_VERSION}"
		"${CMAKE_CURRENT_SOURCE_DIR}/palmos.rcp"
	DEPENDS "${CMAKE_CURRENT_SOURCE_DIR}/palmos.rcp"
	BYPRODUCTS "${SQUIRRELJME_PALMOS_RESOURCE_LIB_OUTPUT}"
	COMMENT "Building PalmOS Resource Library."
	COMMAND_EXPAND_LISTS VERBATIM)

# m68k?
if(CMAKE_SYSTEM_PROCESSOR STREQUAL "m68k")
	message("PalmOS: m68k")

	add_subdirectory("m68k")
endif()

# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: IEEE1275 Open Firmware Interface

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Include both core sets of libraries
get_property(SQUIRRELJME_IEEE1275_POWERPC_CORE
	GLOBAL PROPERTY SquirrelJMECoreTargetObjects)
get_property(SQUIRRELJME_IEEE1275_POWERPC_FRONTEND_IEEE1275
	GLOBAL PROPERTY SquirrelJMEIEEE1275LibTargetObjects)

# Add executable here
add_executable(SquirrelJMEIEEE1275PowerPC
	boot.c
	${SQUIRRELJME_IEEE1275_POWERPC_CORE}
	${SQUIRRELJME_IEEE1275_POWERPC_FRONTEND_IEEE1275})

# This is written only in C!
set_target_properties(SquirrelJMEIEEE1275PowerPC PROPERTIES LINKER_LANGUAGE C)

# Position independent code always
set_property(TARGET SquirrelJMEIEEE1275PowerPC
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Includes for target
target_include_directories(SquirrelJMEIEEE1275PowerPC PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Need to use the linker script to generate all the code
target_link_options(SquirrelJMEIEEE1275PowerPC PUBLIC
	-static										# Static library
	-nostdlib									# No standard libraries
	-T "${CMAKE_CURRENT_SOURCE_DIR}/linker.ld"	# Linker script for entry
	)


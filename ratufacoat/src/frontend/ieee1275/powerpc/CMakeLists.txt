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

# Find PowerPC Compiler
find_program(IEEE1275_POWERPC_COMPILER powerpc-elf)
if(NOT IEEE1275_POWERPC_COMPILER MATCHES ".*NOTFOUND.*")
	find_program(IEEE1275_POWERPC_COMPILER powerpc-linux-gnu-gcc)
endif()

# Setup custom project for compilation if found
if(NOT IEEE1275_POWERPC_COMPILER MATCHES ".*NOTFOUND.*")
	# For debugging
	message("Found PowerPC compiler, enabling IEEE1275 PowerPC!")

	# Include both core sets of libraries
	get_property(SQUIRRELJME_IEEE1275_POWERPC_CORE
		GLOBAL PROPERTY SquirrelJMECoreTargetSources)
	get_property(SQUIRRELJME_IEEE1275_POWERPC_LIBS
		GLOBAL PROPERTY SquirrelJMEIEEE1275LibTargetSources)

	# Determine all of the paths to use for compilation
	set(SQUIRRELJME_IEEE1275_POWERPC_SOURCES)
	foreach(sourceItem ${SQUIRRELJME_IEEE1275_POWERPC_CORE}
		${SQUIRRELJME_IEEE1275_POWERPC_LIBS})
		list(APPEND SQUIRRELJME_IEEE1275_POWERPC_SOURCES
			"${PROJECT_SOURCE_DIR}/${sourceItem}")
	endforeach()

	# Target for build
	set(SQUIRRELJME_IEEE1275_POWERPC_BIN
		"${PROJECT_BINARY_DIR}/squirreljme-ieee1275-powerpc.elf")
	add_custom_target(SquirrelJMEIEEE1275PowerPC
		COMMAND "${IEEE1275_POWERPC_COMPILER}"
			-static -nostdlib -nostdinc
			-I "${PROJECT_BINARY_DIR}"
			-I "${PROJECT_SOURCE_DIR}/include"
			-D SJME_BARE_METAL
			-o "${SQUIRRELJME_IEEE1275_POWERPC_BIN}"
			-T "${CMAKE_CURRENT_SOURCE_DIR}/linker.ld"
			-ffixed-r25
			${SQUIRRELJME_IEEE1275_POWERPC_SOURCES}
		DEPENDS SquirrelJMECoreLib # Just to check compilation
		WORKING_DIRECTORY "${PROJECT_BINARY_DIR}"
		BYPRODUCTS "${SQUIRRELJME_IEEE1275_POWERPC_BIN}"
		COMMAND_EXPAND_LISTS
		VERBATIM
		COMMENT "Calling cross-compiler for PowerPC GCC."
		SOURCES ${SQUIRRELJME_IEEE1275_POWERPC_SOURCES})

	# Target for testing, requires QEMU to exist
endif()

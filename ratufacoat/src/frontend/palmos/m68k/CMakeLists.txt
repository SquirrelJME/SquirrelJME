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

# Generate multi section linker and otherwise for sections and whatnot
add_custom_target(SquirrelJMEPalmOsM68kMultiGen
	COMMAND "m68k-palmos-multigen"
		-b "${CMAKE_CURRENT_BINARY_DIR}/multi"
		"${CMAKE_CURRENT_SOURCE_DIR}/multi.def"
	BYPRODUCTS "${CMAKE_CURRENT_BINARY_DIR}/multi.s"
		"${CMAKE_CURRENT_BINARY_DIR}/multi.ld"
	COMMENT "Generating multi-section link script"
	COMMAND_EXPAND_LISTS VERBATIM)

# Include both core sets of libraries
get_property(SQUIRRELJME_PALMOS_M68K_CORE
	GLOBAL PROPERTY SquirrelJMECoreTargetObjects)
get_property(SQUIRRELJME_PALMOS_M68K_FRONTEND_LIB
	GLOBAL PROPERTY SquirrelJMEPalmOsLibTargetObjects)

# Add executable here
add_executable(SquirrelJMEPalmOsM68k
	"${CMAKE_CURRENT_BINARY_DIR}/multi.s"
	boot.c
	${SQUIRRELJME_PALMOS_M68K_CORE}
	${SQUIRRELJME_PALMOS_M68K_FRONTEND_LIB})

# Generation of multi-section data needs to be built first so we can use it
add_dependencies(SquirrelJMEPalmOsM68k SquirrelJMEPalmOsM68kMultiGen)

# The ELF is not used at all, but it gets turned into a bunch of objects
set_target_properties(SquirrelJMEPalmOsM68k PROPERTIES
	OUTPUT_NAME "squirreljme.elf")

# Need to use the multi-section link file here
set_target_properties(SquirrelJMEPalmOsM68k PROPERTIES
	LINK_DEPENDS "${CMAKE_CURRENT_BINARY_DIR}/multi.ld")

# Special link options
target_link_options(SquirrelJMEPalmOsM68k PUBLIC
	-T text_64k							# Use 64k code sections
	)

# Common C Directives
squirreljme_common_c(SquirrelJMEPalmOsM68k)

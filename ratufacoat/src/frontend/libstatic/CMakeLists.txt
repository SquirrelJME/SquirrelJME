# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Static Library

# Load target objects
get_property(SQUIRRELJMECORELIB_TARGET_OBJECTS
	GLOBAL PROPERTY SquirrelJMECoreTargetObjects)

# Core library for SquirrelJME as a static library, which includes all of
# the objects together as one
add_library(SquirrelJMELibStatic STATIC
	blank.c
	${SQUIRRELJMECORELIB_TARGET_OBJECTS})

# Change the name of the library
set_target_properties(SquirrelJMELibStatic PROPERTIES
	OUTPUT_NAME "squirreljme")

# Needed includes
target_include_directories(SquirrelJMELibStatic PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# This is written only in C!
set_target_properties(SquirrelJMELibStatic PROPERTIES LINKER_LANGUAGE C)

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMELibStatic
	PROPERTY POSITION_INDEPENDENT_CODE ${SQUIRRELJME_FPIC_MODE})

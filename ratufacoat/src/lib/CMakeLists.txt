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
add_library(SquirrelJMECoreLib STATIC
	blank.cxx
	${SQUIRRELJMECORELIB_TARGET_OBJECTS})

# C++!
set_target_properties(SquirrelJMECoreLib PROPERTIES LINKER_LANGUAGE CXX)

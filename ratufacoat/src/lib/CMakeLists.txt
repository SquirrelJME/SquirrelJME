# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Static Library

# Core library for SquirrelJME as a static library, which includes all of
# the objects together as one
add_library(SquirrelJMECoreLib STATIC
	blank.c
	$<TARGET_OBJECTS:SquirrelJMEEngine>
	$<TARGET_OBJECTS:SquirrelJMEFormat>
	$<TARGET_OBJECTS:SquirrelJMEEngineSpringCoat>
	$<TARGET_OBJECTS:SquirrelJMECore>)

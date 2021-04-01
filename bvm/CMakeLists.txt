# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

cmake_minimum_required (VERSION 3.13)
project(BuildVM
	VERSION 0.3.0
	DESCRIPTION "Build Virtual Machine specifically for only SquirrelJME."
	HOMEPAGE_URL https://squirreljme.cc/
	LANGUAGES C)

# Build properties for SquirrelJME
set(CMAKE_C_STANDARD 99)
set(CMAKE_C_STANDARD_REQUIRED True)

# Include directories for the project
add_subdirectory(src)

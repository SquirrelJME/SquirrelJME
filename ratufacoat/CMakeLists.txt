# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

cmake_minimum_required (VERSION 3.13)
project(SquirrelJME
	VERSION 0.3.0
	DESCRIPTION "SquirrelJME is a Java ME 8 Virtual Machine for embedded and Internet of Things devices."
	HOMEPAGE_URL https://squirreljme.cc/
	LANGUAGES C)

# Build properties for SquirrelJME
set(CMAKE_C_STANDARD 99)
set(CMAKE_C_STANDARD_REQUIRED True)

# Include directories for the project
add_subdirectory(src)
target_include_directories(SquirrelJMECore PUBLIC
	"${PROJECT_BINARY_DIR}" "include")
target_include_directories(SquirrelJME PUBLIC
	"${PROJECT_BINARY_DIR}" "include")

# Testing
add_subdirectory(tests)

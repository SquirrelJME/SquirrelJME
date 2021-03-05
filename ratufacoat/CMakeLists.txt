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

# Does the static ROM file exist? If it does we can pack it into the binary
if(EXISTS "${PROJECT_SOURCE_DIR}/build/builtin.c")
	message("SquirrelJME: Including Built-In ROM.")
	set(SQUIRRELJME_HAS_BUILTIN)

	option(SQUIRRELJME_HAS_BUILTIN "Use Built-In ROM (Detected)" ON)
else()
	message("SquirrelJME: No Built-In ROM Available.")

	option(SQUIRRELJME_HAS_BUILTIN "Use Built-In ROM (Detected)" OFF)
	set(SQUIRRELJME_BUILTIN_FILE)
endif()

# Enable support for testing, this is needed here otherwise testing will not
# work at all! Major headache this has caused...
# From: https://cmake.org/cmake/help/v3.13/command/enable_testing.html
# > Note that ctest expects to find a test file in the build directory root.
# > Therefore, this command should be in the source directory root.
enable_testing()

# Include directories for the project
add_subdirectory(src)
target_include_directories(SquirrelJME PUBLIC
	"${PROJECT_BINARY_DIR}" "include")

# Testing
add_subdirectory(tests)


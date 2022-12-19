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

# Setup executable
add_executable(SquirrelJMEIEEE1275
	devtree.c)

# This is written only in C!
set_target_properties(SquirrelJMEIEEE1275 PROPERTIES LINKER_LANGUAGE C)

# Includes for target
target_include_directories(SquirrelJMEIEEE1275 PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Library core
target_link_libraries(SquirrelJMEIEEE1275 PUBLIC
	SquirrelJMECoreLib)


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
add_library(SquirrelJMEIEEE1275Lib OBJECT
	devtree.c)

# This is written only in C!
set_target_properties(SquirrelJMEIEEE1275Lib PROPERTIES LINKER_LANGUAGE C)

# Position independent code always
set_property(TARGET SquirrelJMEIEEE1275Lib
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Includes for target
target_include_directories(SquirrelJMEIEEE1275Lib PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Needed by sub-targets
squirreljme_object_and_sources(SquirrelJMEIEEE1275LibTarget
	SquirrelJMEIEEE1275Lib)

# Include supported architectures
add_subdirectory(powerpc)

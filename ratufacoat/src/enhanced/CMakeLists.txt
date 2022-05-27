# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: C++ Enhanced base functions

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Define the library
add_library(SquirrelJMEEnhanced OBJECT
	javatypes.cxx)

# Fancy C++ features!
set_target_properties(SquirrelJMEEnhanced PROPERTIES LINKER_LANGUAGE CXX)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMEEnhanced PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMEEnhanced
	PROPERTY POSITION_INDEPENDENT_CODE ON)

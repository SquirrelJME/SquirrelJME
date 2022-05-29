# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Collections

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Declare base library
add_library(SquirrelJMECollections OBJECT
	arraylist.c
	collection.c
	hashmap.c)

# C++!
set_target_properties(SquirrelJMECollections PROPERTIES LINKER_LANGUAGE CXX)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMECollections PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMECollections
	PROPERTY POSITION_INDEPENDENT_CODE ON)

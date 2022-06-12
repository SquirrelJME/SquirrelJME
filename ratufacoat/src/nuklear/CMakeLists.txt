# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Nuklear UI

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Declare base library
add_library(SquirrelJMENuklearUI OBJECT
	nuklear.c)

# This is written only in C!
set_target_properties(SquirrelJMENuklearUI PROPERTIES LINKER_LANGUAGE C)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMENuklearUI PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMENuklearUI
	PROPERTY POSITION_INDEPENDENT_CODE ON)

# Disable warnings since this is a third party library we do not touch
if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
	target_compile_options(SquirrelJMENuklearUI PRIVATE -w)
endif()

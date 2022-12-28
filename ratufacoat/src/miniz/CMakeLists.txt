# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: MiniZ

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Declare base library
add_library(SquirrelJMEMiniZ OBJECT
	miniz.c)

# This is written only in C!
set_target_properties(SquirrelJMEMiniZ PROPERTIES LINKER_LANGUAGE C)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMEMiniZ PUBLIC
	"${PROJECT_BINARY_DIR}"
	"${PROJECT_SOURCE_DIR}/include"
	"${PROJECT_SOURCE_DIR}/include/miniz")

# Make this position independent so it can be linked into shared libraries
set_property(TARGET SquirrelJMEMiniZ
	PROPERTY POSITION_INDEPENDENT_CODE ${SQUIRRELJME_FPIC_MODE})

# Disable warnings since this is a third party library we do not touch
if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
	target_compile_options(SquirrelJMEMiniZ PRIVATE -w)
endif()

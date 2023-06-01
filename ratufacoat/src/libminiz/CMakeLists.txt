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
add_library(SquirrelJMELibMiniZ OBJECT
	miniz.c)

# Common C Directives
squirreljme_common_c(SquirrelJMELibMiniZ)

# Dynamically generated and standard includes
target_include_directories(SquirrelJMELibMiniZ PUBLIC
	"${PROJECT_SOURCE_DIR}/include/miniz")

# Make this position independent so it can be linked into shared libraries
squirreljme_enable_pic(SquirrelJMELibMiniZ)

# Disable warnings since this is a third party library we do not touch
if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
	target_compile_options(SquirrelJMELibMiniZ PRIVATE -w)
endif()

# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: SpringCoat Engine

# Verbosity?
if(DEFINED ENV{CMAKE_VERBOSE_MAKEFILE})
	set(CMAKE_VERBOSE_MAKEFILE $ENV{CMAKE_VERBOSE_MAKEFILE})
endif()

# Declare base library
add_library(SquirrelJMEEngineSpringCoat OBJECT
	springcoat.c)

# Common C Directives
squirreljme_common_c(SquirrelJMEEngineSpringCoat)

# Make this position independent so it can be linked into shared libraries
squirreljme_enable_pic(SquirrelJMEEngineSpringCoat)

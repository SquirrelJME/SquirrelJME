# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Core ScritchInput Library
squirreljme_multilib_add_library(ScritchInputCore
	inputCore.c)

# Include main headers
squirreljme_multilib_include_directories(ScritchInputCore PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

# Set C language if not set
squirreljme_multilib_linker_language(ScritchInputCore C)

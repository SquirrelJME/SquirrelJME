# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# Declare MiniZ library
squirreljme_multilib_add_library(3rdPartyMiniZ
	miniz.c)

# Include main headers
squirreljme_multilib_target_include_directories(3rdPartyMiniZ
	"${CMAKE_SOURCE_DIR}/include")

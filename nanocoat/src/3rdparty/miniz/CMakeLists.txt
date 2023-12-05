# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

add_library(3rdPartyMiniZ OBJECT
	miniz.c)

# Include main headers
target_include_directories(3rdPartyMiniZ PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

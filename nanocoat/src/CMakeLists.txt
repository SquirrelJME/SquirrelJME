# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------

# THe core library contains the main logic for the virtual machine
add_library(Core STATIC
	boot.c
	debug.c
	loop.c
	util.c)

# Include main headers
target_include_directories(Core PUBLIC
	"${CMAKE_SOURCE_DIR}/include")

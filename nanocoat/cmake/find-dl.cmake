# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Check if the dynamic linker library is needed

# Include module
include(CheckLibraryExists)

# Check to see if dl has both of these
check_library_exists("${SQUIRRELJME_MAYBE_LIBDL}"
	"dlopen" "" SJME_CONFIG_HAS_DL_DLOPEN)
check_library_exists("${SQUIRRELJME_MAYBE_LIBDL}"
	"dlclose" "" SJME_CONFIG_HAS_DL_DLCLOSE)
if(SJME_CONFIG_HAS_DL_DLOPEN AND SJME_CONFIG_HAS_DL_DLCLOSE)
	set(SQUIRRELJME_MAYBE_LIBDL "dl")
else()
	find_library(SQUIRRELJME_MAYBE_LIBDL "dl")
endif()


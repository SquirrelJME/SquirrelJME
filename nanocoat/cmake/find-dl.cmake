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

# Notice...
message(STATUS "If the following library check is blank and shows as found, "
	"dlopen/dlclose may be built-in to your C runtime and not as a "
	"separate library.")

# Check to see if dl has both of these
check_library_exists("${SQUIRRELJME_MAYBE_LIBDL}"
	"dlopen" "" SJME_CONFIG_HAS_DL_DLOPEN)
check_library_exists("${SQUIRRELJME_MAYBE_LIBDL}"
	"dlclose" "" SJME_CONFIG_HAS_DL_DLCLOSE)
if(SJME_CONFIG_HAS_DL_DLOPEN AND SJME_CONFIG_HAS_DL_DLCLOSE)
	set(SQUIRRELJME_MAYBE_LIBDL "dl")
else()
	find_library(checkLibDlPath "dl")
	if(NOT "${checkLibDlPath}" STREQUAL "checkLibDlPath-NOTFOUND")
		# Ensure this is cleared
		set(SQUIRRELJME_MAYBE_LIBDL)
	else()
		set(SQUIRRELJME_MAYBE_LIBDL "dl")
	endif()
endif()


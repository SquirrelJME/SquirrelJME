# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Soft patching for CMake.

# CMake 3.13 added many things!
if(${CMAKE_VERSION} VERSION_LESS_EQUAL "3.12")
	# Sort list of files
	macro(squirreljme_list_file_sort lfsList)
		list(SORT ${lfsList})
	endmacro()
else()
	# Sorting file list
	macro(squirreljme_list_file_sort lfsList)
		list(SORT ${lfsList} COMPARE FILE_BASENAME)
	endmacro()
endif()

# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Merge the Standalone Jar

# Get arguments as a list
# Read in all arguments to a list
set(args)
set(gotDashDash)
foreach(i RANGE 0 ${CMAKE_ARGC} 1)
	# Ignore until dash dash hit
	if(gotDashDash)
		list(APPEND args "${CMAKE_ARGV${i}}")
	endif()

	# Hit --?
	if("${CMAKE_ARGV${i}}" STREQUAL "--")
		set(gotDashDash YES)
	endif()
endforeach()

message(FATAL_ERROR "ARGS: ${args}")

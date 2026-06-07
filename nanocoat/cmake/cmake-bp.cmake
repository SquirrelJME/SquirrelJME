# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Backporting for CMake, recreates functionality that newer
# CMake has but older CMake does not. This is mostly for legacy systems, but
# also for specific embedded toolchains which are out of date or include an
# old version of CMake and cannot upgrade.

# Is there at least _THIS_ version?
macro(squirreljme_bp_version_test ver set)
	if(CMAKE_VERSION VERSION_GREATER_EQUAL ${ver})
		set(${set} YES)
	else()
		set(${set} NO)
	endif()
endmacro()

# Version tests
squirreljme_bp_version_test(3.13 squirreljme_bp_version_3_13)
squirreljme_bp_version_test(3.14 squirreljme_bp_version_3_14)
squirreljme_bp_version_test(3.25 squirreljme_bp_version_3_25)

# squirreljme_bp_return_propagate inOutVariable
# Propagates a variable into the parent scope then returns
macro(squirreljme_bp_return_propagate inOutVariable)
	if(squirreljme_bp_version_3_25)
		return(PROPAGATE ${inOutVariable})
	else()
		set(${inOutVariable} "${inOutVariable}" PARENT_SCOPE)
		return()
	endif()
endmacro()

# squirreljme_bp_file_size(<filename> <variable>)
# Determines the size of the given file
function(squirreljme_bp_file_size inFileName outVariable)
	# Use normal file size
	if(squirreljme_bp_version_3_14)
		file(SIZE "${inFileName}" ${outVariable})
		squirreljme_bp_return_propagate(${outVariable})

	# Imitate the functionality
	else()
		# Read in all the data as hex, since that is really the only way to
		# determine the size
		unset(garbageData)
		file(READ "${inFileName}" ${garbageData} HEX)

		# Calculate length, then discard all the string data
		string(LENGTH "${garbageData}" ${outVariable})
		unset(garbageData)

		# Cut in half
		if(squirreljme_bp_version_3_13)
			math(EXPR ${outVariable} "${outVariable} / 2"
				OUTPUT_FORMAT DECIMAL)
		else()
			math(EXPR ${outVariable} "${outVariable} / 2")
		endif()

		# Return the resultant size
		squirreljme_bp_return_propagate(${outVariable})
	endif()
endfunction()

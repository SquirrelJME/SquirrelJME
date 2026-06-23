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

# Which CMake version is this?
message(STATUS "CMake Version ${CMAKE_VERSION}")

# Version tests
squirreljme_bp_version_test(3.1 squirreljme_bp_version_3_1)
squirreljme_bp_version_test(3.3 squirreljme_bp_version_3_3)
squirreljme_bp_version_test(3.13 squirreljme_bp_version_3_13)
squirreljme_bp_version_test(3.14 squirreljme_bp_version_3_14)
squirreljme_bp_version_test(3.17 squirreljme_bp_version_3_17)
squirreljme_bp_version_test(3.18 squirreljme_bp_version_3_18)
squirreljme_bp_version_test(3.23 squirreljme_bp_version_3_23)
squirreljme_bp_version_test(3.25 squirreljme_bp_version_3_25)

# There is no current function list dir, so this needs to be set for some
# functions in this backport implementation to work.
# This replaces CMAKE_CURRENT_FUNCTION_LIST_FILE
set(SQUIRRELJME_BP_LIST_FILE "${CMAKE_CURRENT_LIST_FILE}")

# Only set policies when using the most minimal version of CMake
if(NOT squirreljme_bp_version_3_13)
	# CMake 3.1+ Policies
	if(squirreljme_bp_version_3_1)
		# Only interpret if() arguments as variables or keywords when unquoted.
		cmake_policy(SET CMP0054 YES)
	endif()

	# CMake 3.3+ Policies
	if(squirreljme_bp_version_3_3)
		# Support new if() IN_LIST operator.
		cmake_policy(SET CMP0057 YES)
	endif()
endif()

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

# squirreljme_bp_check_linker_flag
# This according to the CMake documentation is a convenience method that
# > This command temporarily sets the CMAKE_REQUIRED_LINK_OPTIONS variable
# > and calls the check_source_compiles() command from the CheckSourceCompiles
# > module. See that module's documentation for a listing of variables that
# > can otherwise modify the build.
function(squirreljme_bp_check_linker_flag lang flag outVariable)
	if(squirreljme_bp_version_3_18)
		# Use modern CMake version
		check_linker_flag(${lang} "${flag}" ${outVariable})

		# Return the compilation result
		squirreljme_bp_return_propagate(${outVariable})
	else()
		# Add verbosity since normally CMake emits a status message that this
		# is being checked
		message(STATUS "Backport checking ${lang} linker flag ${flag}...")

		# Check to see if some source compiles, it should be noted that the
		# documentation says check_source_compiles() however that is only in
		# CMake 3.19+. check_source_compilers() is a wrapper around
		# try_compile(). Note that we have to use the old signature and not
		# the newer signature.
		try_compile(${outVariable}
			SOURCES "${SQUIRRELJME_BP_LIST_FILE}/tryMain.c"
			LINK_OPTIONS "${CMAKE_${lang}_LINK_FLAGS} ${flag}")

		# What was the result of it?
		message(STATUS "${lang} linker flag ${flag}: ${outVariable}")

		# Return the compilation result
		squirreljme_bp_return_propagate(${outVariable})
	endif()
endfunction()

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

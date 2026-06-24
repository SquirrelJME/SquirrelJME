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
	# Should this be set?
	if(CMAKE_VERSION VERSION_GREATER_EQUAL ${ver})
		set(${set} YES)
	else()
		set(${set} NO)
	endif()

	# Print the version stage
	message(STATUS "${set}: ${${set}}")
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
set(SQUIRRELJME_BP_LIST_DIR "${CMAKE_CURRENT_LIST_DIR}")

# CMake 3.1+ Policies
if(squirreljme_bp_version_3_1)
	# Only interpret if() arguments as variables or keywords when unquoted.
	cmake_policy(SET CMP0054 NEW)
endif()

# CMake 3.3+ Policies
if(squirreljme_bp_version_3_3)
	# Support new if() IN_LIST operator.
	cmake_policy(SET CMP0057 NEW)
endif()

# CMake 3.25+ Policies
if(squirreljme_bp_version_3_25)
	# The return() command checks its parameters.
	cmake_policy(SET CMP0140 NEW)
endif()

if(squirreljme_bp_version_3_18)
	# Needed for compiler/linker flag check, if CMake is new enough
	include(CheckLinkerFlag)
	include(CheckCompilerFlag)
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
	# Add verbosity for the check
	message(STATUS "Checking ${lang} linker flag ${flag}...")

	if(squirreljme_bp_version_3_18)
		# Use modern CMake version
		check_linker_flag(${lang} "${flag}" ${outVariable})

		# What was the result of it?
		message(STATUS "${lang} linker flag ${flag}: ${${outVariable}}")

		# Return the compilation result
		squirreljme_bp_return_propagate(${outVariable})
	else()
		# A _unique_ binary directory is required, otherwise this will not
		# work properly
		string(MAKE_C_IDENTIFIER "${lang}_${flag}_${outVariable}"
			uniq)

		# Check to see if some source compiles, it should be noted that the
		# documentation says check_source_compiles() however that is only in
		# CMake 3.19+. check_source_compiles() is a wrapper around
		# try_compile(). Note that we have to use the old signature and not
		# the newer signature.
		try_compile(${outVariable} "${CMAKE_CURRENT_BINARY_DIR}/try/${uniq}"
			SOURCES "${SQUIRRELJME_BP_LIST_DIR}/tryMain.c"
			CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE=EXECUTABLE"
				"-DCMAKE_BUILD_TYPE=Release"
			LINK_OPTIONS "${CMAKE_${lang}_LINK_FLAGS} ${flag}"
			OUTPUT_VARIABLE tryCompileOutput)

		# What was the result of it?
		message(STATUS "${lang} linker flag ${flag}: ${${outVariable}}")
		message("${lang} linker flag ${flag}: ${tryCompileOutput}")

		# Return the compilation result
		squirreljme_bp_return_propagate(${outVariable})
	endif()
endfunction()

# squirreljme_bp_check_compiler_flag
# Like squirreljme_bp_check_linker_flag() this is also a convenience method
# around try_compile()
function(squirreljme_bp_check_compiler_flag lang flag outVariable)
	# Add verbosity for the check
	message(STATUS "Checking ${lang} compiler flag ${flag}...")

	if(squirreljme_bp_version_3_18)
		# Use modern CMake version
		check_compiler_flag(${lang} "${flag}" ${outVariable})

		# What was the result of it?
		message(STATUS "${lang} compiler flag ${flag}: ${${outVariable}}")

		# Return the compilation result
		squirreljme_bp_return_propagate(${outVariable})
	else()
		# A _unique_ binary directory is required, otherwise this will not
		# work properly
		string(MAKE_C_IDENTIFIER "${lang}_${flag}_${outVariable}"
			uniq)

		# As above, check_source_compiles() is CMake 3.19+, so use
		# try_compile() with the older signature.
		# Note that the generated try_compile() project has the following:
		# > set(CMAKE_C_FLAGS "")
		# > set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} ${COMPILE_DEFINITIONS}")
		# while the documentation states that `add_definitions` are used, so
		# which one is it?
		try_compile(${outVariable} "${CMAKE_CURRENT_BINARY_DIR}/try/${uniq}"
			SOURCES "${SQUIRRELJME_BP_LIST_DIR}/tryMain.c"
			CMAKE_FLAGS "-DCMAKE_TRY_COMPILE_TARGET_TYPE:STRING=EXECUTABLE"
				"-DCMAKE_BUILD_TYPE=Release"
				# This may be needed for older CMake where there are no build
				# types
				"-DCMAKE_${lang}_FLAGS:STRING=${flag}"
				# Specifying these, seems to take effect
				"-DCMAKE_${lang}_FLAGS_INIT:STRING=${flag}"
				"-DCMAKE_${lang}_FLAGS_DEBUG:STRING=${flag}"
				"-DCMAKE_${lang}_FLAGS_RELEASE:STRING=${flag}"
			#COMPILE_DEFINITIONS "${flag}"
			OUTPUT_VARIABLE tryCompileOutput)

		# What was the result of it?
		message(STATUS "${lang} compiler flag ${flag}: ${${outVariable}}")
		message("${lang} compiler flag ${flag}: ${tryCompileOutput}")

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

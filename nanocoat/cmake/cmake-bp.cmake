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

# CMake 3.1+ Policies
# Note that before CMP0054 is set, this is not capable of using the variable
# Need to use VERSION_GREATER as VERSION_GREATER_EQUAL does not exist
# Only interpret if() arguments as variables or keywords when unquoted.
# This must be set first
message(STATUS "Setting policy CMP0054...")
cmake_policy(SET CMP0054 NEW)

# Spliced version
set(squirreljme_bp_version_splice "${CMAKE_VERSION}")
string(REPLACE "." ";"
	squirreljme_bp_version_splice "${squirreljme_bp_version_splice}")
list(GET squirreljme_bp_version_splice 0 squirreljme_bp_version_major)
list(GET squirreljme_bp_version_splice 1 squirreljme_bp_version_minor)

# Basic backport version
message(STATUS "Reported as CMake ${CMAKE_VERSION}")
message(STATUS
	"CMake ${squirreljme_bp_version_major}.${squirreljme_bp_version_minor}")

# Is there at least _THIS_ version?
# Note that VERSION_GREATER_EQUAL was added in CMake 3.7!
# So this must use VERSION_GREATER or something else
# Hence, the complication unfortunately
macro(squirreljme_bp_version_test majorVer minorVer set)
	if("${CMAKE_VERSION}" VERSION_GREATER "${majorVer}.${minorVer}")
		set(${set} TRUE)
	elseif(${squirreljme_bp_version_major} EQUAL ${majorVer})
		if(${squirreljme_bp_version_minor} GREATER ${minorVer})
			set(${set} TRUE)
		elseif(${squirreljme_bp_version_minor} EQUAL ${minorVer})
			set(${set} TRUE)
		else()
			set(${set} FALSE)
		endif()
	else()
		if(${squirreljme_bp_version_major} GREATER ${majorVer})
			set(${set} TRUE)
		elseif(${squirreljme_bp_version_major} EQUAL ${majorVer})
			if(${squirreljme_bp_version_minor} GREATER ${minorVer})
				set(${set} TRUE)
			elseif(${squirreljme_bp_version_minor} EQUAL ${minorVer})
				set(${set} TRUE)
			else()
				set(${set} FALSE)
			endif()
		else()
			set(${set} FALSE)
		endif()
	endif()

	# Print the version stage
	message(STATUS "CMake ${majorVer}.${minorVer}: ${${set}}")
endmacro()

# Version tests
squirreljme_bp_version_test(3 1 squirreljme_bp_version_3_1)
squirreljme_bp_version_test(3 3 squirreljme_bp_version_3_3)
squirreljme_bp_version_test(3 12 squirreljme_bp_version_3_12)
squirreljme_bp_version_test(3 13 squirreljme_bp_version_3_13)
squirreljme_bp_version_test(3 14 squirreljme_bp_version_3_14)
squirreljme_bp_version_test(3 17 squirreljme_bp_version_3_17)
squirreljme_bp_version_test(3 18 squirreljme_bp_version_3_18)
squirreljme_bp_version_test(3 19 squirreljme_bp_version_3_19)
squirreljme_bp_version_test(3 20 squirreljme_bp_version_3_20)
squirreljme_bp_version_test(3 23 squirreljme_bp_version_3_23)
squirreljme_bp_version_test(3 24 squirreljme_bp_version_3_24)
squirreljme_bp_version_test(3 25 squirreljme_bp_version_3_25)
squirreljme_bp_version_test(3 29 squirreljme_bp_version_3_29)

# Future versions
squirreljme_bp_version_test(4 0 squirreljme_bp_version_4_0)
squirreljme_bp_version_test(4 1 squirreljme_bp_version_4_1)
squirreljme_bp_version_test(4 2 squirreljme_bp_version_4_2)

# Note on these future versions
if(squirreljme_bp_version_4_0 OR
	squirreljme_bp_version_4_1 OR
	squirreljme_bp_version_4_2)
	message(STATUS "CMake 4.0+ may break backwards compatibility with 3.x!")
endif()

# There is no current function list dir, so this needs to be set for some
# functions in this backport implementation to work.
# This replaces CMAKE_CURRENT_FUNCTION_LIST_FILE
set(SQUIRRELJME_BP_LIST_FILE "${CMAKE_CURRENT_LIST_FILE}")
set(SQUIRRELJME_BP_LIST_DIR "${CMAKE_CURRENT_LIST_DIR}")

# CMake 3.3+ Policies
if(squirreljme_bp_version_3_3 OR
	"${CMAKE_VERSION}" VERSION_GREATER "3.2")
	# Support new if() IN_LIST operator.
	message(STATUS "Setting policy CMP0057...")
	cmake_policy(SET CMP0057 NEW)
endif()

# CMake 3.25+ Policies
if(squirreljme_bp_version_3_25)
	# The return() command checks its parameters.
	message(STATUS "Setting policy CMP0140...")
	cmake_policy(SET CMP0140 NEW)
endif()

if(squirreljme_bp_version_3_19)
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
		set(${inOutVariable} "${${inOutVariable}}" PARENT_SCOPE)
		return()
	endif()
endmacro()

# Adding compile definitions was done in a slightly different way
if(NOT squirreljme_bp_version_3_12)
	macro(add_compile_definitions varVal)
		add_definitions("-D${varVal}")
	endmacro()
endif()

# squirreljme_bp_check_linker_flag
# This according to the CMake documentation is a convenience method that
# > This command temporarily sets the CMAKE_REQUIRED_LINK_OPTIONS variable
# > and calls the check_source_compiles() command from the CheckSourceCompiles
# > module. See that module's documentation for a listing of variables that
# > can otherwise modify the build.
function(squirreljme_bp_check_linker_flag lang flag outVariable)
	# Add verbosity for the check
	message(STATUS "Checking ${lang} linker flag ${flag}...")

	if(squirreljme_bp_version_3_19)
		# Use modern CMake version
		check_linker_flag(${lang} "${flag}" ${outVariable})

		# CMake 3.22 seems to have a bug where the resultant variable is
		# blank when it fails? Set to some value accordingly if so.
		if("${${outVariable}}" STREQUAL "")
			set(${outVariable} FALSE)
		endif()

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

	if(squirreljme_bp_version_3_19)
		# Use modern CMake version
		check_compiler_flag(${lang} "${flag}" ${outVariable})

		# CMake 3.22 seems to have a bug where the resultant variable is
		# blank when it fails? Set to some value accordingly if so.
		if("${${outVariable}}" STREQUAL "")
			set(${outVariable} FALSE)
		endif()

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
			math(EXPR ${outVariable} "${${outVariable}} / 2"
				OUTPUT_FORMAT DECIMAL)
		else()
			math(EXPR ${outVariable} "${${outVariable}} / 2")
		endif()

		# Return the resultant size
		squirreljme_bp_return_propagate(${outVariable})
	endif()
endfunction()

# String joining
function(squirreljme_string_join sjGlue sjOut sjList)
	if(squirreljme_bp_version_3_13 OR
		"${CMAKE_VERSION}" VERSION_GREATER "3.12")
		string(JOIN "${sjGlue}" ${sjOut}
			"${sjList}")
		squirreljme_bp_return_propagate(${sjOut})
	else()
		# Setup initial blank output
		set(sjResult "")

		# Go through list
		list(LENGTH "${sjList}" sjListLen)
		set(sjAt "0")
		while("${sjAt}" LESS "${sjListLen}")
			# Get list item
			set(sjTemp "")
			list(GET "${sjList}" "${sjAt}" sjTemp)

			# Append joiner
			string(APPEND sjResult "${sjGlue}")

			# Append string
			string(APPEND sjResult "${sjTemp}")

			# Move up
			math(EXPR sjAt "${sjAt} + 1")
		endwhile()

		# Set output
		set(${sjOut} "${sjResult}")
		squirreljme_bp_return_propagate(${sjOut})
	endif()
endfunction()

function(squirreljme_target_link_options target scope)
	# This is available since CMake 3.13
	if(squirreljme_bp_version_3_13)
		target_link_options(${ARGV})

	# Otherwise it must be manually added in
	else()
		# The target we are interested in...
		set(ltoArgs "${ARGV}")
		list(GET ltoArgs 0 tloTarget)

		# Is there a before?
		list(GET ltoArgs 1 tloMaybeBefore)
		if(tloMaybeBefore STREQUAL "BEFORE")
			# Mark as before
			set(tloBefore TRUE)

			# Start pivot point
			set(tloPivot 2)
		else()
			# Mark as not before
			set(tloBefore No)

			# Start pivot point
			set(tloPivot 1)
		endif()

		# Handle the remaining number of items
		set(tloAt "${tloPivot}")
		set(tloFlags)
		while(tloAt LESS ARGC)
			# Determine indexes
			math(EXPR tloAtI "${tloAt} + 0")
			math(EXPR tloAtL "${tloAt} + 1")

			# Extract sub-parameters
			list(GET ltoArgs "${tloAtI}" tloInstance)
			list(GET ltoArgs "${tloAtL}" tloFlag)

			# Add library, ignore the instance for it
			list(APPEND tloFlags "${tloFlag}")

			# Move indexes up for the next items
			math(EXPR tloAt "${tloAt} + 2")
		endwhile()

		# Join options together
		squirreljme_string_join(" " tloStrOpt "${tloFlags}")

		# What should be used for link flags?
		if(NOT "$<CONFIG>" STREQUAL "")
			set(tloLinkFlagsName "LINK_FLAGS_$<CONFIG>")
		else()
			set(tloLinkFlagsName "LINK_FLAGS")
		endif()

		# Get old link options to add in the list...
		get_target_property(tloOldLinkOpt ${tloTarget}
			LINK_FLAGS)
		if(tloOldLinkOpt)
			if(tloBefore)
				set_target_properties(${tloTarget} PROPERTIES
					${tloLinkFlagsName} "${tloStrOpt} ${tloOldLinkOpt}")
			else()
				set_target_properties(${tloTarget} PROPERTIES
					${tloLinkFlagsName} "${tloOldLinkOpt} ${tloStrOpt}")
			endif()
		else()
			set_target_properties(${tloTarget} PROPERTIES
				${tloLinkFlagsName} "${tloStrOpt}")
		endif()
	endif()
endfunction()

# Add link directories to target
function(squirreljme_target_link_directories target scope)
	# Determine directories
	set(directories "${ARGV}")
	list(REMOVE_AT directories 0)
	list(REMOVE_AT directories 0)

	# This is available since CMake 3.13
	if(squirreljme_bp_version_3_13)
		target_link_directories(${target} ${scope} ${directories})
	else()
		# MSVC
		if(MSVC OR
			CMAKE_C_COMPILER_ID STREQUAL "MSVC" OR
			CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
			foreach(directory IN ITEMS ${directories})
				squirreljme_target_link_options(${target} ${scope}
					"/LIBPATH:${directory}")
			endforeach()
		# Assume POSIX
		else()
			foreach(directory IN ITEMS ${directories})
				squirreljme_target_link_options(${target} ${scope}
					"-L${directory}")
			endforeach()
		endif()
	endif()
endfunction()

# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake related fixes and build system related fixes
# The major purpose of this file is to make it so the main files are kept
# clean and pristine and patches are placed here because they affect the
# entire project.

# Cross-compiling the build?
if(NOT "${CMAKE_HOST_SYSTEM_NAME}" STREQUAL "${CMAKE_SYSTEM_NAME}" OR
	NOT "${CMAKE_HOST_SYSTEM_PROCESSOR}" STREQUAL "${CMAKE_SYSTEM_PROCESSOR}")
	set(SQUIRRELJME_CROSS_BUILD ON)
	message(STATUS "Performing cross-build as "
		"${CMAKE_HOST_SYSTEM_NAME}/"
		"${CMAKE_HOST_SYSTEM_PROCESSOR} is not "
		"${CMAKE_SYSTEM_NAME}/${CMAKE_SYSTEM_PROCESSOR}.")
endif()

# It's a UNIX system! I know this!
if(APPLE OR BSD OR LINUX OR
	CMAKE_SYSTEM_NAME STREQUAL "Linux" OR
	CMAKE_SYSTEM_NAME STREQUAL "Solaris" OR
	CMAKE_SYSTEM_NAME STREQUAL "Darwin" OR
	CMAKE_SYSTEM_NAME STREQUAL "FreeBSD" OR
	CMAKE_SYSTEM_NAME STREQUAL "OpenBSD" OR
	CMAKE_SYSTEM_NAME STREQUAL "NetBSD")
	set(SQUIRRELJME_IS_UNIX ON)
else()
	set(SQUIRRELJME_IS_UNIX OFF)
endif()

# It's a Windows system? Must be another flavor of UNIX!
if(WIN32 OR CMAKE_SYSTEM_NAME STREQUAL "Windows")
	set(SQUIRRELJME_IS_WINDOWS ON)
else()
	set(SQUIRRELJME_IS_WINDOWS OFF)
endif()

# Is this RetroArch? Any kind of RetroArch build?
if(RETROARCH OR ENV{RETROARCH} OR
	LIBRETRO_STATIC OR ENV{LIBRETRO_STATIC} OR
	LIBRETRO_SUFFIX OR ENV{LIBRETRO_SUFFIX} OR
	ENV{LIBRETRO})
	set(LIBRETRO ON)
endif()

# LibRetro build for emscripten can never be static
if(LIBRETRO)
	if(EMSCRIPTEN)
		unset(LIBRETRO_REALLY_STATIC)
		unset(LIBRETRO_REALLY_STATIC CACHE)

		# Linking needs to be fixed here
		set(CMAKE_STATIC_LIBRARY_SUFFIX
			".bc")
		set(CMAKE_C_CREATE_STATIC_LIBRARY
			"emcc -o <TARGET> -shared <LINK_FLAGS> <OBJECTS>")
		set(CMAKE_CXX_CREATE_STATIC_LIBRARY
			"emcc -o <TARGET> -shared <LINK_FLAGS> <OBJECTS>")
		set(EMSCRIPTEN_GENERATE_BITCODE_STATIC_LIBRARIES
			ON)
	elseif(LIBRETRO_STATIC)
		set(LIBRETRO_REALLY_STATIC ON)
	endif()
endif()

# If we cannot run the code we are building then we cannot actually test code
#if(NOT SQUIRRELJME_CROSS_BUILD)
#	include(CheckCSourceRuns)
#	set(CMAKE_REQUIRED_QUIET ON)
#	check_c_source_runs("${CMAKE_SOURCE_DIR}/cmake/utils/simple.c"
#		SQUIRRELJME_SIMPLE_SOURCE_RUNS)
#	if(NOT SQUIRRELJME_SIMPLE_SOURCE_RUNS)
#		# Note
#		message(WARNING
#			"Could not run simple utility ("
#			"${SQUIRRELJME_SIMPLE_SOURCE_RUNS}), disabling tests.")
#
#		# Disable testing
#		set(SQUIRRELJME_ENABLE_TESTING OFF)
#	endif()
#else()
#	# Different host, assume we cannot run the target code
#	set(SQUIRRELJME_ENABLE_TESTING OFF)
#endif()

# String joining
if(${CMAKE_VERSION} VERSION_LESS_EQUAL "3.11")
	macro(squirreljme_string_join sjGlue sjOut
		sjList)
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
	endmacro()
else()
	macro(squirreljme_string_join sjGlue sjOut
		sjList)
		string(JOIN "${sjGlue}" ${sjOut}
			"${sjList}")
	endmacro()
endif()

# CMake 3.13 added many things!
if(${CMAKE_VERSION} VERSION_LESS_EQUAL "3.12")
	# Sort list of files
	macro(squirreljme_list_file_sort lfsList)
		list(SORT ${lfsList})
	endmacro()

	# Additional compiler settings
	macro(add_compile_definitions varVal)
		add_definitions("-D${varVal}")
	endmacro()

	# Additional linker options
	macro(target_link_options)
		# The target we are interested in...
		list(GET "${ARGV}" 0 tloTarget)

		# Is there a before?
		set(GET "${ARGV}" 1 tloMaybeBefore)
		if(tloMaybeBefore STREQUAL "BEFORE")
			# Mark as before
			set(tloBefore YES)

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
			list(GET "${ARGV}" "${tloAtI}" tloInstance)
			list(GET "${ARGV}" "${tloAtL}" tloFlag)

			# Add library, ignore the instance for it
			list(APPEND tloFlags "${tloFlag}")

			# Move indexes up for the next items
			math(EXPR tloAt "${tloAt} + 2")
		endwhile()

		# Get old link options to add in the list...
		get_target_property(tloOldLinkOpt ${tloTarget}
			LINK_FLAGS)
		squirreljme_string_join(" " tloStrOpt "${tloFlags}")
		if(tloBefore)
			set_target_properties(${tloTarget} PROPERTIES
				LINK_FLAGS "${tloStrOpt} ${tloOldLinkOpt}")
		else()
			set_target_properties(${tloTarget} PROPERTIES
				LINK_FLAGS "${tloOldLinkOpt} ${tloStrOpt}")
		endif()
	endmacro()

	# Disable Testing
	if(NOT DEFINED SQUIRRELJME_ENABLE_TESTING)
		message(WARNING "Disabling testing due to old CMake.")

		set(SQUIRRELJME_ENABLE_TESTING OFF)
	endif()

	# Disable CPacking
	if(NOT DEFINED SQUIRRELJME_ENABLE_PACKING)
		message(WARNING "Disabling packing due to old CMake.")

		set(SQUIRRELJME_ENABLE_PACKING OFF)
	endif()
else()
	# Enable Testing
	if(NOT DEFINED SQUIRRELJME_ENABLE_TESTING)
		message(STATUS "Enabling testing...")

		set(SQUIRRELJME_ENABLE_TESTING ON)
	endif()

	# Enable CPacking and
	if(NOT DEFINED SQUIRRELJME_ENABLE_PACKING)
		message(STATUS "Enabling packing...")

		set(SQUIRRELJME_ENABLE_PACKING ON)
	endif()

	# Sorting file list
	macro(squirreljme_list_file_sort lfsList)
		list(SORT ${lfsList} COMPARE FILE_BASENAME)
	endmacro()
endif()

# Make static executable
macro(squirreljme_static_executable target)
	if(CMAKE_COMPILER_IS_GNUCC OR
		CMAKE_COMPILER_IS_GNUCXX OR
		CMAKE_C_COMPILER_ID STREQUAL "GNU" OR
		CMAKE_CXX_COMPILER_ID STREQUAL "Clang")
		target_link_options(${target} BEFORE PRIVATE
			"-static")
	elseif(MSVC OR
		CMAKE_C_COMPILER_ID STREQUAL "MSVC" OR
		CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
		# For MSVC, static linking is specified at compile time rather than
		# at link time, so everything has to be compiled this way to be static
		#target_compile_options(${target} BEFORE PRIVATE
		#	"/MT")

		# And as such we cannot specify a library to use here
		#target_link_options(${target} BEFORE PRIVATE
		#	"/NODEFAULTLIB:library")
	endif()
endmacro()

# Force a specific name for the output resultant binary
macro(squirreljme_target_binary_name target what)
	# Base properties
	set_target_properties(${target} PROPERTIES
		RUNTIME_OUTPUT_NAME "${what}"
		LIBRARY_OUTPUT_NAME "${what}"
		ARCHIVE_OUTPUT_NAME "${what}")

	# Then for each configuration
	foreach(outputConfig ${CMAKE_CONFIGURATION_TYPES})
		string(TOUPPER "${outputConfig}" outputConfig)

		set_target_properties(${target} PROPERTIES
			RUNTIME_OUTPUT_NAME_${outputConfig} "${what}"
			LIBRARY_OUTPUT_NAME_${outputConfig} "${what}"
			ARCHIVE_OUTPUT_NAME_${outputConfig} "${what}")
	endforeach()
endmacro()

# Need to set specific locations for output libraries?
# Note that RUNTIME_OUTPUT_DIRECTORY is needed for the Windows build to output
# directories since .DLL files are output there and not where shared libraries
# go??? No idea really.
macro(squirreljme_target_binary_output target where)
	set_target_properties(${target} PROPERTIES
		RUNTIME_OUTPUT_DIRECTORY "${where}"
		LIBRARY_OUTPUT_DIRECTORY "${where}"
		ARCHIVE_OUTPUT_DIRECTORY "${where}")

	foreach(outputConfig ${CMAKE_CONFIGURATION_TYPES})
		string(TOUPPER "${outputConfig}" outputConfig)

		set_target_properties(${target} PROPERTIES
			RUNTIME_OUTPUT_DIRECTORY_${outputConfig} "${where}"
			LIBRARY_OUTPUT_DIRECTORY_${outputConfig} "${where}"
			ARCHIVE_OUTPUT_DIRECTORY_${outputConfig} "${where}")
	endforeach()
endmacro()

# Turn some warnings into errors
if(CMAKE_COMPILER_IS_GNUCC OR CMAKE_COMPILER_IS_GNUCXX)
	add_compile_options("-Werror=implicit-function-declaration")
endif()

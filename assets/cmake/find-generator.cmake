# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: In the past though I looked specifically for MSVC's cl.exe,
# which really does not work well. CMake also ends up just using the 64-bit
# target anyway. So I think the best thing to do here is to just use the
# Visual Studio generators... This also applies to Xcode and such as well.

# Attempts a given generator
macro(squirreljme_find_generator generator toolset platform)
	# We need a temporary directory for the build project
	squirreljme_temp_path(tempBuild)
	file(MAKE_DIRECTORY "${tempBuild}")

	# Try configuring the blank project
	# If the toolset is none, ignore it
	unset(generatorResult)
	if(squirreljme_bp_version_3_13)
		if("${toolset}" STREQUAL "none")
			# Ignore also if the platform is none
			if("${platform}" STREQUAL "none")
				execute_process(
					COMMAND "${CMAKE_COMMAND}"
						"-G" "${generator}"
						"-B" "${tempBuild}"
						"-S" "${CMAKE_SOURCE_DIR}/assets/cmake/simple"
					WORKING_DIRECTORY "${tempBuild}"
					RESULT_VARIABLE generatorResult
					OUTPUT_QUIET
					ERROR_QUIET)
			else()
				execute_process(
					COMMAND "${CMAKE_COMMAND}"
						"-G" "${generator}"
						"-A" "${platform}"
						"-B" "${tempBuild}"
						"-S" "${CMAKE_SOURCE_DIR}/assets/cmake/simple"
					WORKING_DIRECTORY "${tempBuild}"
					RESULT_VARIABLE generatorResult
					OUTPUT_QUIET
					ERROR_QUIET)
			endif()
		else()
			execute_process(
				COMMAND "${CMAKE_COMMAND}"
					"-G" "${generator}"
					"-T" "${platform}"
					"-A" "${toolset}"
					"-B" "${tempBuild}"
					"-S" "${CMAKE_SOURCE_DIR}/assets/cmake/simple"
				WORKING_DIRECTORY "${tempBuild}"
				RESULT_VARIABLE generatorResult
				OUTPUT_QUIET
				ERROR_QUIET)
		endif()
	else()
		if("${toolset}" STREQUAL "none")
			# Ignore also if the platform is none
			if("${platform}" STREQUAL "none")
				execute_process(
					COMMAND "${CMAKE_COMMAND}"
						"-G" "${generator}"
						"${CMAKE_SOURCE_DIR}/assets/cmake/simple"
					WORKING_DIRECTORY "${tempBuild}"
					RESULT_VARIABLE generatorResult
					OUTPUT_QUIET
					ERROR_QUIET)
			else()
				execute_process(
					COMMAND "${CMAKE_COMMAND}"
						"-G" "${generator}"
						"-A" "${platform}"
						"${CMAKE_SOURCE_DIR}/assets/cmake/simple"
					WORKING_DIRECTORY "${tempBuild}"
					RESULT_VARIABLE generatorResult
					OUTPUT_QUIET
					ERROR_QUIET)
			endif()
		else()
			execute_process(
				COMMAND "${CMAKE_COMMAND}"
					"-G" "${generator}"
					"-T" "${platform}"
					"-A" "${toolset}"
					"${CMAKE_SOURCE_DIR}/assets/cmake/simple"
				WORKING_DIRECTORY "${tempBuild}"
				RESULT_VARIABLE generatorResult
				OUTPUT_QUIET
				ERROR_QUIET)
		endif()
	endif()

	# Try to actually run the output, since older CMake seems to want to
	# actually sometimes only configure with certain generators when built
	execute_process(
		COMMAND "${CMAKE_COMMAND}"
			"--build" "${tempBuild}"
		WORKING_DIRECTORY "${tempBuild}"
		RESULT_VARIABLE buildResult
		OUTPUT_QUIET
		ERROR_QUIET)

	# If successful and the system/arch information exists, register it
	if("${generatorResult}" EQUAL "0" AND
		"${buildResult}" EQUAL "0" AND
		EXISTS "${tempBuild}/system.tgt" AND
		EXISTS "${tempBuild}/arch__.tgt")
		# Load the info
		file(READ "${tempBuild}/system.tgt" systemNormal)
		file(READ "${tempBuild}/arch__.tgt" archNormal)

		# Track it
		squirreljme_track_generator(${systemNormal} ${archNormal}
			${generator} ${toolset} ${platform})

		# Status that it worked!
		message(STATUS "Found ${generator}.${toolset}.${platform}!")
	else()
		# Debug
		if(NOT "${generatorResult}" EQUAL "0")
			message(STATUS
				"Ignoring ${generator}.${toolset}.${platform}... ")
		elseif(NOT "${buildResult}" EQUAL "0")
			message(STATUS
				"Failed ${generator}.${toolset}.${platform}... ")
		elseif(NOT EXISTS "${tempBuild}/system.tgt")
			message(STATUS
				"Unknown system for ${generator}.${toolset}.${platform}... ")
		elseif(NOT EXISTS "${tempBuild}/arch__.tgt")
			message(STATUS
				"Unknown arch for ${generator}.${toolset}.${platform}... ")
		else()
			message(STATUS
				"Ignoring ${generator}.${toolset}.${platform}... ")
		endif()
	endif()
endmacro()

# Try a set of generators
macro(squirreljme_find_generators generators toolsets platforms)
	# Make actual variables for lists
	set(generators "${generators}")
	set(toolsets "${toolsets}")
	set(platforms "${platforms}")

	# Process each combination
	foreach(generator IN ITEMS ${generators})
		foreach(toolset IN ITEMS ${toolsets})
			foreach(platform IN ITEMS ${platforms})
				squirreljme_find_generator(${generator} ${toolset} ${platform})
			endforeach()
		endforeach()
	endforeach()
endmacro()

# Basic platforms, generally used as a sanity check to ensure that generators
# work properly but this could unlock more compilers!
unset(SQUIRRELJME_GENERATOR_BASIC)
unset(SQUIRRELJME_GENERATOR_BASIC_TOOLSETS)
unset(SQUIRRELJME_GENERATOR_BASIC_PLATFORMS)
list(APPEND SQUIRRELJME_GENERATOR_BASIC
	"Borland Makefiles"
	"NMake Makefiles"
	"Watcom WMake"
	"MinGW Makefiles"
	"Unix Makefiles")
list(APPEND SQUIRRELJME_GENERATOR_BASIC_TOOLSETS
	"none")
list(APPEND SQUIRRELJME_GENERATOR_BASIC_PLATFORMS
	"none")

squirreljme_find_generators(
	"${SQUIRRELJME_GENERATOR_BASIC}"
	"${SQUIRRELJME_GENERATOR_BASIC_TOOLSETS}"
	"${SQUIRRELJME_GENERATOR_BASIC_PLATFORMS}")

# Green Hills
unset(SQUIRRELJME_GENERATOR_GHM)
unset(SQUIRRELJME_GENERATOR_GHM_TOOLSETS)
unset(SQUIRRELJME_GENERATOR_GHM_PLATFORMS)
list(APPEND SQUIRRELJME_GENERATOR_GHM
	"Green Hills MULTI")
list(APPEND SQUIRRELJME_GENERATOR_GHM_TOOLSETS
	"none")
list(APPEND SQUIRRELJME_GENERATOR_GHM_PLATFORMS
	"arm"
	"ppc"
	"86")

squirreljme_find_generators(
	"${SQUIRRELJME_GENERATOR_GHM}"
	"${SQUIRRELJME_GENERATOR_GHM_TOOLSETS}"
	"${SQUIRRELJME_GENERATOR_GHM_PLATFORMS}")

# Microsoft Visual Studio
unset(SQUIRRELJME_GENERATOR_MSVC)
unset(SQUIRRELJME_GENERATOR_MSVC_TOOLSETS)
unset(SQUIRRELJME_GENERATOR_MSVC_PLATFORMS)
list(APPEND SQUIRRELJME_GENERATOR_MSVC
	"Visual Studio 6"
	"Visual Studio 7"
	"Visual Studio 7 .NET 2003"
	"Visual Studio 8 2005"
	"Visual Studio 9 2008"
	"Visual Studio 10 2010"
	"Visual Studio 11 2012"
	"Visual Studio 12 2013"
	"Visual Studio 14 2015"
	"Visual Studio 15 2017"
	"Visual Studio 16 2019"
	"Visual Studio 17 2022"
	"Visual Studio 18 2026")
list(APPEND SQUIRRELJME_GENERATOR_MSVC_TOOLSETS
	"none")
list(APPEND SQUIRRELJME_GENERATOR_MSVC_PLATFORMS
	"Win32"
	"X64"
	"ARM"
	"ARM64")

squirreljme_find_generators(
	"${SQUIRRELJME_GENERATOR_MSVC}"
	"${SQUIRRELJME_GENERATOR_MSVC_TOOLSETS}"
	"${SQUIRRELJME_GENERATOR_MSVC_PLATFORMS}")

# Xcode (macOS)
unset(SQUIRRELJME_GENERATOR_XCODE)
unset(SQUIRRELJME_GENERATOR_XCODE_TOOLSETS)
unset(SQUIRRELJME_GENERATOR_XCODE_PLATFORMS)
list(APPEND SQUIRRELJME_GENERATOR_XCODE
	"Xcode")
list(APPEND SQUIRRELJME_GENERATOR_XCODE_TOOLSETS
	"none")
list(APPEND SQUIRRELJME_GENERATOR_XCODE_PLATFORMS
	"arm64"
	"x86_64")

squirreljme_find_generators(
	"${SQUIRRELJME_GENERATOR_XCODE}"
	"${SQUIRRELJME_GENERATOR_XCODE_TOOLSETS}"
	"${SQUIRRELJME_GENERATOR_XCODE_PLATFORMS}")

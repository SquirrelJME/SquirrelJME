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

# Possible CMake Generators
list(APPEND SQUIRRELJME_CMAKE_GENERATORS
	"Xcode"
	"Green Hills MULTI"
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

# Platforms to be passed to the generators
list(APPEND SQUIRRELJME_CMAKE_PLATFORMS
	"none")
list(APPEND SQUIRRELJME_CMAKE_PLATFORMS_GREEN
	"arm"
	"ppc"
	"86")
list(APPEND SQUIRRELJME_CMAKE_PLATFORMS_MSVC
	"Win32"
	"x64"
	"ARM"
	"ARM64")

# Go through each generator, and each platform
message(STATUS "Checking CMake Generators...")
foreach(generator IN LISTS SQUIRRELJME_CMAKE_GENERATORS)
	# Which set of platforms to use?
	string(FIND "${generator}" "Visual Studio" isMsvc)
	string(FIND "${generator}" "Green Hills" isGreen)
	if("${isMsvc}" GREATER_EQUAL "0")
		set(usePlatforms "${SQUIRRELJME_CMAKE_PLATFORMS_MSVC}")
	elseif("${isGreen}" GREATER_EQUAL "0")
		set(usePlatforms "${SQUIRRELJME_CMAKE_PLATFORMS_GREEN}")
	else()
		set(usePlatforms "${SQUIRRELJME_CMAKE_PLATFORMS}")
	endif()

	# Then for each platform
	foreach(platform IN LISTS usePlatforms)
		# Where to place this?
		string(MAKE_C_IDENTIFIER
			"check-${generator}-${platform}" dirName)
		file(TO_CMAKE_PATH
			"${CMAKE_BINARY_DIR}/gen-check/${dirName}" checkDir)
		file(MAKE_DIRECTORY "${checkDir}")

		# Is the architecture used?
		unset(archArgs)
		if(NOT "${platform}" STREQUAL "none")
			list(APPEND archArgs "-A" "${platform}")
		endif()

		# Check to see if we can configure for this platform
		message(STATUS "Checking CMake Generator ${generator}/${platform}...")
		execute_process(COMMAND "${CMAKE_COMMAND}"
			"-G" "${generator}"
			"${archArgs}"
			"-B" "${checkDir}"
			"-S" "${CMAKE_SOURCE_DIR}/nanocoat"
			COMMAND_EXPAND_LISTS
			RESULT_VARIABLE checkResult
			OUTPUT_FILE "${checkDir}.out"
			ERROR_FILE "${checkDir}.err")

		# Successfully configured? With a valid system?
		if("${checkResult}" EQUAL "0" AND
			EXISTS "${checkDir}/system.tgt" AND
			EXISTS "${checkDir}/arch__.tgt")
			# Read in architecture and target
			file(READ "${checkDir}/system.tgt" systemChecked)
			file(READ "${checkDir}/arch__.tgt" archChecked)

			# Cleanup
			string(STRIP "${systemChecked}" systemChecked)
			string(STRIP "${archChecked}" archChecked)

			# Is this valid?
			if(NOT "${systemChecked}" STREQUAL "unknown" AND
				NOT "${archChecked}" STREQUAL "unknown" AND
				NOT "${systemChecked}" STREQUAL "" AND
				NOT "${archChecked}" STREQUAL "")
				# Register the compiler by generator/arch
				squirreljme_compiler_by_generator(
					"${systemChecked}" "${archChecked}"
					"${generator}" "${platform}")
			endif()
		endif()
	endforeach()
endforeach()

# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Find various MSVC installations, as they can be anywhere and
# all over the place

# Possible Major MSVC versions
list(APPEND SQUIRRELJME_MSVC_VERSION
	"6" "7" "8" "9" "10" "11" "12" "13" "14"
	"2019" "2020" "2021" "2022" "2023" "2024")

# Possible MSVC architectures
list(APPEND SQUIRRELJME_MSVC_ARCH
	"x86" "x64" "amd64" "arm")

# Possible Program Files locations (include 32-bit CMake on 64-bit system)
list(APPEND SQUIRRELJME_PROGRAM_FILES
	"$ENV{ProgramFiles}"
	"$ENV{ProgramFiles\(x86\)}"
	"$ENV{ProgramFilesW6432}")
file(TO_CMAKE_PATH "${SQUIRRELJME_PROGRAM_FILES}" SQUIRRELJME_PROGRAM_FILES)
list(REMOVE_DUPLICATES SQUIRRELJME_PROGRAM_FILES)

# Inner check for MSVC
macro(squirreljme_msvc_check_inner binPath hostArch targetArch)
	# Request the command line options to see if we can actually run it, if
	# we cannot then it might be for another architecture
	file(TO_CMAKE_PATH "${binPath}/cl.exe" clExe)
	execute_process(COMMAND "${clExe}"
		OUTPUT_VARIABLE helpOutput
		ERROR_VARIABLE helpError
		RESULT_VARIABLE helpResult)

	# It does run, we need to test it, however it is hard to tell what some
	# targets actually are until we try to compile for it and run things
	if("${helpResult}" EQUAL "0")
		squirreljme_compiler_probe("${clExe}")
	endif()
endmacro()

# Outer check for MSVC
macro(squirreljme_msvc_check_outer pf ver hostArch targetArch)
	# If the target architecture is the same, it makes no difference
	if("${hostArch}" STREQUAL "${targetArch}")
		set(arch "${hostArch}")
	else()
		set(arch "${hostArch}_${targetArch}")
	endif()

	# C:\Program Files\Microsoft Visual Studio 8\VC\bin\x86_amd64
	file(TO_CMAKE_PATH
		"${pf}/Microsoft Visual Studio ${ver}/VC/bin/${arch}"
		possible)
	if(IS_DIRECTORY "${possible}")
		squirreljme_msvc_check_inner("${possible}"
			"${hostArch}" "${targetArch}")
	endif()

	# C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin\x86_amd64
	file(TO_CMAKE_PATH
		"${pf}/Microsoft Visual Studio ${ver}.0/VC/bin/${arch}"
		possible)
	if(IS_DIRECTORY "${possible}")
		squirreljme_msvc_check_inner("${possible}"
			"${hostArch}" "${targetArch}")
	endif()

	# And most annoyingly...
	# C:\Program Files (x86)\Microsoft Visual Studio\2022\
	#  -> BuildTools\VC\Tools\MSVC\*\bin\Host(x86|x64)\(x86|x64)
	file(TO_CMAKE_PATH
		"${pf}/Microsoft Visual Studio/${ver}/BuildTools/VC/Tools/MSVC"
		modernPath)
	if(IS_DIRECTORY "${modernPath}")
		# Grab all directories here
		file(GLOB idList
			LIST_DIRECTORIES true
			RELATIVE "${modernPath}"
			"${modernPath}/*.*")

		# Are these even valid?
		foreach(id IN LISTS idList)
			# Build path
			file(TO_CMAKE_PATH
				"${modernPath}/${id}/bin/Host${hostArch}/${targetArch}"
				possible)

			# Valid?
			if(IS_DIRECTORY "${possible}")
				squirreljme_msvc_check_inner("${possible}"
					"${hostArch}" "${targetArch}")
			endif()
		endforeach()
	endif()
endmacro()

# Each program files directory needs to be checked
foreach(programFiles IN LISTS SQUIRRELJME_PROGRAM_FILES)
	# Blank or does not exist?
	if("${programFiles}" STREQUAL "" OR
		NOT IS_DIRECTORY "${programFiles}")
		continue()
	endif()

	# Check each major version
	foreach(msvcVersion IN LISTS SQUIRRELJME_MSVC_VERSION)
		# Check each architecture (host, then target)
		foreach(msvcHostArch IN LISTS SQUIRRELJME_MSVC_ARCH)
			foreach(msvcTargetArch IN LISTS SQUIRRELJME_MSVC_ARCH)

				# Because nesting is so deep...
				squirreljme_msvc_check_outer("${programFiles}"
					"${msvcVersion}"
					"${msvcHostArch}"
					"${msvcTargetArch}")
			endforeach()
		endforeach()
	endforeach()
endforeach()

# Locations I have discovered on my own systems:
# C:\Program Files (x86)\Microsoft Visual Studio\2022\
#  -> BuildTools\VC\Tools\MSVC\*\bin\Host(x86|x64)\(x86|x64)
# C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin
# C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin\amd64
# C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin\amd64_arm
# C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin\amd64_x86
# C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin\arm
# C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin\x86_amd64
# C:\Program Files (x86)\Microsoft Visual Studio 14.0\VC\bin\x86_arm
# C:\Program Files\Microsoft Platform SDK\Bin\win64
# C:\Program Files\Microsoft Platform SDK\Bin\win64\x86\AMD64
# C:\Program Files\Microsoft Visual Studio 8\VC\bin
# C:\Program Files\Microsoft Visual Studio 8\VC\bin\amd64
# C:\Program Files\Microsoft Visual Studio 8\VC\bin\x86_amd64

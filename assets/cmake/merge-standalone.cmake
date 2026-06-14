# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Merge the Standalone Jar
#
# Example input arguments: <outJar> <workDir> <...>
# /tmp/build/bin/squirreljme-standalone-0.3.0.jar
# /tmp/build/mergeTemp
# /tmp/build/natives.build/linux/amd64//bin/natives-linux-amd64.zip
# /tmp/build/natives.build/linux/arm64l//bin/natives-linux-arm64l.zip
# /tmp/build/natives.build/linux/ia32//bin/natives-linux-ia32.zip
# /tmp/build/natives.build/linux/mips32b//bin/natives-linux-mips32b.zip
# /tmp/build/natives.build/linux/mips32b6//bin/natives-linux-mips32b6.zip
# /tmp/build/natives.build/linux/mips32l//bin/natives-linux-mips32l.zip
# /tmp/build/natives.build/linux/mips32l6//bin/natives-linux-mips32l6.zip
# /tmp/build/natives.build/linux/powerpc32b//bin/natives-linux-powerpc32b.zip
# /tmp/build/natives.build/linux/powerpc64l//bin/natives-linux-powerpc64l.zip
# /tmp/build/natives.build/linux/riscv64//bin/natives-linux-riscv64.zip
# /tmp/build/natives.build/linux/mips64b//bin/natives-linux-mips64b.zip
# /tmp/build/natives.build/linux/mips64b6//bin/natives-linux-mips64b6.zip
# /tmp/build/natives.build/linux/mips64l//bin/natives-linux-mips64l.zip
# /tmp/build/natives.build/linux/mips64l6//bin/natives-linux-mips64l6.zip
# /tmp/build/natives.build/windows/amd64//bin/natives-windows-amd64.zip
# /tmp/build/natives.build/windows/ia32//bin/natives-windows-ia32.zip
# /tmp/build/download/natives-macosx-arm64l.zip
# /tmp/build/base//squirreljme-standalone-0.3.0.jar

# Set CMake policy here for the script (is that even possible)?
# Use the best version possible. Otherwise, fallback to a viable version
if(CMAKE_VERSION VERSION_GREATER_EQUAL 3.13)
	cmake_policy(VERSION 3.13..3.31)
else()
	cmake_policy(VERSION 3.0)
endif()

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

# Determine arguments
list(GET args 0 outJar)
list(GET args 1 workDir)

# Shift down known arguments
list(REMOVE_AT args 0)
list(REMOVE_AT args 0)

# Debugging
message(STATUS "Resultant Jar: ${outJar}")
message(STATUS "Working directory: ${workDir}")

# Make sure the temporary directory exists first
file(MAKE_DIRECTORY "${workDir}")

# Extract all into the working directory
foreach(extracting IN LISTS args)
	if(NOT EXISTS "${extracting}")
		message(STATUS "File '${extracting}' does not exist, this is "
			"likely indicative of a broken shell or a CMake bug!")
		continue()
	endif()

	message(STATUS "Extracting '${extracting}'...")
	execute_process(COMMAND "${CMAKE_COMMAND}" "-E"
		"tar" "xvf" "${extracting}" "--format=zip" "--"
		WORKING_DIRECTORY "${workDir}"
		COMMAND_ERROR_IS_FATAL ANY)
endforeach()

# Merge everything back together into a single zip
message(STATUS "Merging into '${outJar}'...")
execute_process(COMMAND "${CMAKE_COMMAND}" "-E"
	"tar" "cvf" "${outJar}" "--format=zip" "--" "."
	WORKING_DIRECTORY "${workDir}"
	COMMAND_ERROR_IS_FATAL ANY)

# Finished!
message(STATUS "Done!")

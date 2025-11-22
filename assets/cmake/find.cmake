# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Finding of various things

# Macro to add a specific compiler
macro(squirreljme_compiler_register type system arch compilerExe)
	# Do not replace an existing compiler map
	set(compilerMap "${system}!${arch}")
	if("${compilerMap}" IN_LIST SQUIRRELJME_COMPILER_MAP)
		message(STATUS "Not adding another compiler for ${system}/${arch}!")
	else()
		# Note it
		message(STATUS "Compiler ${type} ${system}/${arch}: "
			"${compilerExe}")

		# Set compiler executable
		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_EXECUTABLE
			"${compilerExe}")

		# Add to the list of available compilers
		list(APPEND SQUIRRELJME_COMPILERS "${compilerExe}")
		list(APPEND SQUIRRELJME_COMPILER_MAP
			"${system}!${arch}")
	endif()
endmacro()

# Probe a compiler manually
macro(squirreljme_compiler_probe compilerExe)
	# Indicate that we are going to probe it...
	message(STATUS "Probing compiler ${compilerExe}...")

	# We need some kind of pseudo-compilerId, no security is needed here
	# and we do not need too long of a hash
	string(SHA1 compilerId "${compilerExe}")
	string(SUBSTRING "${compilerId}" 0 16 compilerId)

	# Setup directory where temporary configuration is placed
	file(TO_CMAKE_PATH "${CMAKE_BINARY_DIR}/probe-${compilerId}" probeDir)
	file(MAKE_DIRECTORY "${probeDir}")

	# Try configuring NanoCoat and seeing what happens
	execute_process(COMMAND "${CMAKE_COMMAND}"
		"-DCMAKE_C_COMPILER=${compilerExe}"
		"-B" "${probeDir}"
		"-S" "${CMAKE_SOURCE_DIR}/nanocoat"
		RESULT_VARIABLE probeResult
		OUTPUT_FILE "${CMAKE_BINARY_DIR}/probe-${compilerId}.out"
		ERROR_FILE "${CMAKE_BINARY_DIR}/probe-${compilerId}.err")

	# Did it pass? And do the system/arch files exist?
	if("${probeResult}" EQUAL "0" AND
		EXISTS "${probeDir}/system.tgt" AND
		EXISTS "${probeDir}/arch__.tgt")
		# Read in architecture and target
		file(READ "${probeDir}/system.tgt" systemProbed)
		file(READ "${probeDir}/arch__.tgt" archProbed)

		# Make sure there is no whitespace
		string(STRIP "${systemProbed}" systemProbed)
		string(STRIP "${archProbed}" archProbed)

		# Are these actually valid?
		if(NOT "${systemProbed}" STREQUAL "unknown" AND
			NOT "${archProbed}" STREQUAL "unknown" AND
			NOT "${systemProbed}" STREQUAL "" AND
			NOT "${archProbed}" STREQUAL "")
			squirreljme_compiler_register(probed
				"${systemProbed}" "${archProbed}" "${compilerExe}")
		endif()
	endif()
endmacro()

# Find Java
squirreljme_include("find-java.cmake")

# Find GCC
squirreljme_include("find-gcc.cmake")

# Find MSVC
squirreljme_include("find-msvc.cmake")

# Make sure the lists are sorted to keep them consistent
list(SORT SQUIRRELJME_COMPILERS)
list(SORT SQUIRRELJME_COMPILER_MAP)

# Store the set of compilers into the cache, so that it is never lost if
# this needs to be re-run
set(SQUIRRELJME_COMPILERS "${SQUIRRELJME_COMPILERS}"
	CACHE STRING "Available compilers")

# Store the compiler mappings into the cache so reloads do not break
set(SQUIRRELJME_COMPILER_MAP "${SQUIRRELJME_COMPILER_MAP}"
	CACHE STRING "Available compiler mappings")

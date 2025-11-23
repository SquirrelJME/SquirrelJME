# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Finding of various things

# Macro to add a specific compiler by its generator/platform
macro(squirreljme_compiler_by_generator system arch generator platform)
	# Do not replace an existing compiler map
	set(compilerMap "${system}!${arch}")
	if("${compilerMap}" IN_LIST SQUIRRELJME_COMPILER_MAP)
		message(STATUS "Not adding another compiler for ${system}/${arch}!")

	# Otherwise add and register it!
	else()
		# Note it
		message(STATUS "Compiler for ${system}/${arch}: "
			"-G ${generator} -A ${platform}")

		# Set compiler executable
		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_TYPE
			"generator")
		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_GENERATOR
			"${generator}")
		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_PLATFORM
			"${platform}")

		# Add to the list of available compilers
		list(APPEND SQUIRRELJME_GENERATORS "${generator}!${platform}")
		list(APPEND SQUIRRELJME_COMPILER_MAP
			"${system}!${arch}")
	endif()
endmacro()

# Macro to add a specific compiler by its executable
macro(squirreljme_compiler_by_exe system arch compilerExe)
	# Do not replace an existing compiler map
	set(compilerMap "${system}!${arch}")
	if("${compilerMap}" IN_LIST SQUIRRELJME_COMPILER_MAP)
		message(STATUS "Not adding another compiler for ${system}/${arch}!")

	# Otherwise add and register it!
	else()
		# Note it
		message(STATUS "Compiler for ${system}/${arch}: "
			"${compilerExe}")

		# Set compiler executable
		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_TYPE
			"exe")
		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_EXECUTABLE
			"${compilerExe}")

		# Add to the list of available compilers
		list(APPEND SQUIRRELJME_COMPILERS "${compilerExe}")
		list(APPEND SQUIRRELJME_COMPILER_MAP
			"${system}!${arch}")
	endif()
endmacro()

# Returns the arguments used for CMake
# The output format is not nice due to execute_process() not taking
# the parameter COMMAND_EXPAND_LISTS sadly.
macro(squirreljme_compiler_cmake_args outVa outVb outVc outVb system arch)
	# Generator Specified
	if("${SQUIRRELJME_COMPILER_${system}_${arch}_TYPE}"
		STREQUAL "generator")
		set(${va} "-G")
		set(${vb} "${SQUIRRELJME_COMPILER_${system}_${arch}_GENERATOR}")

		if("${SQUIRRELJME_COMPILER_${system}_${arch}_PLATFORM}"
			STREQUAL "none")
			set(${vc} "-DXXSJMEVCXX=1")
			set(${vd} "-DXXSJMEVDXX=1")
		else()
			set(${vc} "-A")
			set(${vd} "${SQUIRRELJME_COMPILER_${system}_${arch}_PLATFORM}")
		endif()
	else()
		set(${va}
	"-DCMAKE_C_COMPILER=${SQUIRRELJME_COMPILER_${system}_${arch}_EXECUTABLE}")
		set(${vb} "-DXXSJMEVBXX=1")
		set(${vc} "-DXXSJMEVCXX=1")
		set(${vd} "-DXXSJMEVDXX=1")
	endif()
endmacro()

# Find Java
squirreljme_include("find-java.cmake")

# Find GCC
squirreljme_include("find-gcc.cmake")

# Find by CMake Generators
squirreljme_include("find-generator.cmake")

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

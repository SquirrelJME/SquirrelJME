# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Finding of various things

# Keep track of a compiler
macro(squirreljme_track_compiler systemNormal archNormal compilerPath)
	# Get the list
	get_property(compilerList GLOBAL PROPERTY
		SQUIRRELJME_KNOWN_COMPILERS)

	# Append to it
	list(APPEND compilerList
		"${systemNormal}!${archNormal}!${compilerPath}")

	# Notice
	message(STATUS "Compiler ${systemNormal}/${archNormal}: ${compilerPath}")

	# Store again
	set_property(GLOBAL PROPERTY
		SQUIRRELJME_KNOWN_COMPILERS "${compilerList}")
endmacro()

# Keep track of a generator
macro(squirreljme_track_generator systemNormal archNormal generator toolset
	platform)
	# Get the list
	get_property(generatorList GLOBAL PROPERTY
		SQUIRRELJME_KNOWN_GENERATORS)

	# Append to it
	list(APPEND generatorList
		"${systemNormal}!${archNormal}!${generator}!${toolset}!${platform}")

	# Notice
	message(STATUS "Generator ${systemNormal}/${archNormal}: "
		"-G ${generator} -T ${toolset} -A ${platform}")

	# Store again
	set_property(GLOBAL PROPERTY
		SQUIRRELJME_KNOWN_GENERATORS "${generatorList}")
endmacro()

# Find Java
squirreljme_include("find-java.cmake")

# Find GCC
squirreljme_include("find-gcc.cmake")

# Find by CMake Generators
squirreljme_include("find-generator.cmake")

## Macro to add a specific compiler by its generator/platform
#macro(squirreljme_compiler_by_generator system arch generator platform)
#	# Do not replace an existing compiler map
#	set(compilerMap "${system}!${arch}")
#	if("${compilerMap}" IN_LIST SQUIRRELJME_COMPILER_MAP)
#		message(STATUS "Not adding another compiler for ${system}/${arch}!")
#
#	# Otherwise add and register it!
#	else()
#		# Note it
#		message(STATUS "Compiler for ${system}/${arch}: "
#			"-G ${generator} -A ${platform}")
#
#		# Set compiler executable
#		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_TYPE
#			"generator")
#		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_GENERATOR
#			"${generator}")
#		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_PLATFORM
#			"${platform}")
#
#		# Add to the list of available compilers
#		list(APPEND SQUIRRELJME_GENERATORS "${generator}!${platform}")
#		list(APPEND SQUIRRELJME_COMPILER_MAP
#			"${system}!${arch}")
#	endif()
#endmacro()
#
## Macro to add a specific compiler by its executable
#macro(squirreljme_compiler_by_exe system arch compilerExe)
#	# Do not replace an existing compiler map
#	set(compilerMap "${system}!${arch}")
#	if("${compilerMap}" IN_LIST SQUIRRELJME_COMPILER_MAP)
#		message(STATUS "Not adding another compiler for ${system}/${arch}!")
#
#	# Otherwise add and register it!
#	else()
#		# Note it
#		message(STATUS "Compiler for ${system}/${arch}: "
#			"${compilerExe}")
#
#		# Set compiler executable
#		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_TYPE
#			"exe")
#		set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_EXECUTABLE
#			"${compilerExe}")
#
#		# Add to the list of available compilers
#		list(APPEND SQUIRRELJME_COMPILERS "${compilerExe}")
#		list(APPEND SQUIRRELJME_COMPILER_MAP
#			"${system}!${arch}")
#	endif()
#endmacro()
#
## Returns the arguments used for CMake
## The output format is not nice due to execute_process() not taking
## the parameter COMMAND_EXPAND_LISTS sadly.
#macro(squirreljme_compiler_cmake_args outVa outVb outVc outVd system arch)
#	# Generator Specified
#	if("${SQUIRRELJME_COMPILER_${system}_${arch}_TYPE}"
#		STREQUAL "generator")
#		set(${outVa} "-G"
#			PARENT_SCOPE)
#		set(${outVb} "${SQUIRRELJME_COMPILER_${system}_${arch}_GENERATOR}"
#			PARENT_SCOPE)
#
#		if("${SQUIRRELJME_COMPILER_${system}_${arch}_PLATFORM}"
#			STREQUAL "none")
#			set(${outVc} "-DXXSJMEVCACXX=1")
#			set(${outVd} "-DXXSJMEVDXX=1")
#		else()
#			set(${outVc} "-A"
#				PARENT_SCOPE)
#			set(${outVd} "${SQUIRRELJME_COMPILER_${system}_${arch}_PLATFORM}"
#				PARENT_SCOPE)
#		endif()
#	else()
#		set(${outVa}
#	"-DCMAKE_C_COMPILER=${SQUIRRELJME_COMPILER_${system}_${arch}_EXECUTABLE}"
#			PARENT_SCOPE)
#		set(${outVb} "-DXXSJMEVBXX=1")
#		set(${outVc} "-DXXSJMEVCXX=1")
#		set(${outVd} "-DXXSJMEVDXX=1")
#	endif()
#endmacro()
#
#
## Make sure the lists are sorted to keep them consistent
#list(SORT SQUIRRELJME_COMPILERS)
#list(SORT SQUIRRELJME_COMPILER_MAP)

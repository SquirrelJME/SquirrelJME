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

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
	# Set compiler executable
	set(SQUIRRELJME_COMPILER_${systemNormal}_${archNormal}_EXECUTABLE
		"${compilerExe}")

	# Add to the list of available compilers
	list(APPEND SQUIRRELJME_COMPILERS "${compilerExe}")
	list(APPEND SQUIRRELJME_COMPILER_MAP
		"${system}!${arch}")

	# Note it
	message(STATUS "Compiler ${type} ${system}/${arch}: "
		"${compilerExe}")
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

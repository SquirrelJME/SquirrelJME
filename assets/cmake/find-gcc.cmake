# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Find various GCC compilers and cross compilers

# Check every single alias for every system and architecture as there are lots
# for GCC
foreach(systemMap IN LISTS SQUIRRELJME_SYSTEM_MAP)
	foreach(archMap IN LISTS SQUIRRELJME_ARCH_MAP)
		# Extra components
		squirreljme_unmap(system 1 "${systemMap}")
		squirreljme_unmap(arch 1 "${archMap}")

		# Normalized components
		squirreljme_unmap(systemNormal 0 "${systemMap}")
		squirreljme_unmap(archNormal 0 "${archMap}")

		# Ignore if already found as there can be multiple aliases for the
		# same system an architecture
		if(GCC_${systemNormal}_${archNormal}_EXECUTABLE)
			continue()
		endif()

		# Build GCC compiler name (such as i686-linux-gnu-gcc)
		set(gccName "${arch}-${system}-gcc")

		# Is this GCC compiler available? Note that for Windows there could
		# be the win32 variant (prefer Windows threads over Posix threads)
		if("${systemNormal}" STREQUAL "windows")
			find_program(GCC_${systemNormal}_${archNormal}_EXECUTABLE
				NAMES "${gccName}"
					"${gccName}-win32"
					"${gccName}-posix")
		else()
			find_program(GCC_${systemNormal}_${archNormal}_EXECUTABLE
				NAMES "${gccName}")
		endif()

		# If found, we want to store it somewhere
		if(GCC_${systemNormal}_${archNormal}_EXECUTABLE)
			# Add to the system of available compilers
			list(APPEND SQUIRRELJME_GCC_COMPILERS
				"${GCC_${systemNormal}_${archNormal}_EXECUTABLE}")
			list(APPEND SQUIRRELJME_GCC_MAP
				"${systemNormal}!${archNormal}")

			# Note it, if found
			message(STATUS "GCC ${systemNormal}/${archNormal}: "
				"${GCC_${systemNormal}_${archNormal}_EXECUTABLE}")
		endif()
	endforeach()
endforeach()

# Store the set of GCC compilers into the cache, so that it is never lost if
# this needs to be re-run
set(SQUIRRELJME_GCC_COMPILERS "${SQUIRRELJME_GCC_COMPILERS}"
	CACHE STRING "Available GCC compilers")
set(SQUIRRELJME_GCC_MAP "${SQUIRRELJME_GCC_MAP}"
	CACHE STRING "Available GCC mappings")

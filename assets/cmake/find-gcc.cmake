# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Find various GCC compilers and cross compilers

# Go through each possible architecture and system and try to find GCC
# compilers for these
foreach(systemMap IN ITEMS ${SQUIRRELJME_SYSTEM_MAP})
	foreach(archMap IN ITEMS ${SQUIRRELJME_ARCH_MAP})
		# GCC
		squirreljme_unmap(systemGcc 1 "${systemMap}")
		squirreljme_unmap(archGcc 1 "${archMap}")

		# SquirrelJME
		squirreljme_unmap(systemNormal 0 "${systemMap}")
		squirreljme_unmap(archNormal 0 "${archMap}")

		# Ignore if already found as there can be multiple aliases for the
		# same system for an architecture or operating system
		if(GCC_${systemNormal}_${archNormal}_EXECUTABLE)
			continue()
		endif()

		# Build GCC compiler name (such as i686-linux-gnu-gcc)
		if("${archGcc}" STREQUAL "none")
			if("${systemGcc}" STREQUAL "none")
				set(gccName "gcc")
			else()
				set(gccName "${systemGcc}-gcc")
			endif()
		else()
			if("${systemGcc}" STREQUAL "none")
				set(gccName "${archGcc}-gcc")
			else()
				set(gccName "${archGcc}-${systemGcc}-gcc")
			endif()
		endif()

		# Is this GCC compiler available? Note that for Windows there could
		# be the win32 variant (prefer Windows threads over Posix threads)
		# Never store in the cache
		if("${systemNormal}" STREQUAL "windows")
			find_program(GCC_${systemNormal}_${archNormal}_EXECUTABLE
				NAMES "${gccName}"
					"${gccName}-win32"
					"${gccName}-posix"
				NO_CACHE)
		else()
			find_program(GCC_${systemNormal}_${archNormal}_EXECUTABLE
				NAMES "${gccName}"
				NO_CACHE)
		endif()

		# If the executable was found keep track of it for later usage
		if(GCC_${systemNormal}_${archNormal}_EXECUTABLE)
			squirreljme_track_compiler(${systemNormal} ${archNormal}
				"${GCC_${systemNormal}_${archNormal}_EXECUTABLE}")
		endif()
	endforeach()
endforeach()

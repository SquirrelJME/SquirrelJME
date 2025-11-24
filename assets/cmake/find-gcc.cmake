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

		# Was an executable never found/cached for this specific GCC?
		if(NOT DEFINED GCC_${systemNormal}_${archNormal}_EXECUTABLE)
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
		endif()

		# If found, we need to store it somewhere
		if(GCC_${systemNormal}_${archNormal}_EXECUTABLE)
			squirreljme_compiler_by_exe(
				"${systemNormal}" "${archNormal}"
				"${GCC_${systemNormal}_${archNormal}_EXECUTABLE}")
		endif()
	endforeach()
endforeach()

# Is there a default GCC?
find_program(DEFAULT_GCC_EXECUTABLE
	NAMES "gcc")
if(DEFAULT_GCC_EXECUTABLE)
	# Determine the system/arch of that GCC
	squirreljme_identify_by_gcc(defaultGccSystem defaultGccArch
		"${DEFAULT_GCC_EXECUTABLE}")

	# Is the compiler a detected system?
	if(NOT "${defaultGccSystem}" STREQUAL "unknown" AND
		NOT "${defaultGccArch}" STREQUAL "unknown" AND
		NOT "${defaultGccSystem}" STREQUAL "" AND
		NOT "${defaultGccArch}" STREQUAL "")
		# Has the system never been added yet?
		list(FIND SQUIRRELJME_COMPILER_MAP
			"${defaultGccSystem}!${defaultGccArch}"
			hasDefaultGcc)

		# If not, then we should add it
		if(NOT "${hasDefaultGcc}" GREATER_EQUAL "0")
			squirreljme_compiler_by_exe(
				"${defaultGccSystem}" "${defaultGccArch}"
				"${DEFAULT_GCC_EXECUTABLE}")
		endif()
	endif()
endif()

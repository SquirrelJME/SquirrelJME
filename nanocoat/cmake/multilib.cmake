# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Multiple library declarations and otherwise

# Add multi-lib library
macro(squirreljme_multilib_add_library libBase)
	# Load in source files
	set(libBaseSources)
	foreach(arg ${ARGV})
		# Ignore first
		if("${arg}" STREQUAL "${libBase}")
			continue()
		endif()

		list(APPEND libBaseSources "${arg}")
	endforeach()

	# Non-PIC Object
	add_library(${libBase} OBJECT
		${libBaseSources})

	# PIC Object
	add_library(${libBase}PIC OBJECT
		${libBaseSources})

	set_property(TARGET ${libBase}PIC
		PROPERTY POSITION_INDEPENDENT_CODE ON)

	# Shared
	add_library(${libBase}DyLib SHARED
		${libBaseSources})

	set_property(TARGET ${libBase}DyLib
		PROPERTY POSITION_INDEPENDENT_CODE ON)
endmacro()

# Add include directories to multilib library
macro(squirreljme_multilib_target_include_directories libBase)
	# Load in include paths
	set(libBaseIncludes)
	foreach(arg ${ARGV})
		# Ignore first
		if("${arg}" STREQUAL "${libBase}")
			continue()
		endif()

		list(APPEND libBaseIncludes "${arg}")
	endforeach()

	target_include_directories(${libBase} PUBLIC
		${libBaseIncludes})
	target_include_directories(${libBase}PIC PUBLIC
		${libBaseIncludes})
	target_include_directories(${libBase}DyLib PUBLIC
		${libBaseIncludes})
endmacro()

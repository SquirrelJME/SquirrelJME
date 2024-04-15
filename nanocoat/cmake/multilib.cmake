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

	# Static Library
	add_library(${libBase}Static STATIC
		${libBaseSources})

	set_property(TARGET ${libBase}Static
		PROPERTY POSITION_INDEPENDENT_CODE ON)

	# Shared Library
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
	target_include_directories(${libBase}Static PUBLIC
		${libBaseIncludes})
	target_include_directories(${libBase}DyLib PUBLIC
		${libBaseIncludes})
endmacro()

# Multi-lib library directories
macro(squirreljme_multilib_target_link_directories libBase)
	# Load in source files
	set(libBaseLibDirs)
	foreach(arg ${ARGV})
		# Ignore first
		if("${arg}" STREQUAL "${libBase}")
			continue()
		endif()

		list(APPEND libBaseLibDirs "${arg}")
	endforeach()

	# Only link for the dynamic library
	target_link_directories(${libBase}DyLib PUBLIC
		${libBaseLibDirs})

	# Otherwise set transient library directories to be included, for use with
	# $<TARGET_PROPERTY:Target,SQUIRRELJME_LINK_DIRECTORIES>
	set_target_properties(${libBase} PROPERTIES
		SQUIRRELJME_LINK_DIRECTORIES "${libBaseLibDirs}")
	set(SQUIRRELJME_LINK_DIRECTORIES_${libBase}
		${libBaseLibDirs})
endmacro()

# Multi-lib linking of libraries
macro(squirreljme_multilib_target_link_libraries libBase)
	# Load in source files
	set(libBaseLibs)
	foreach(arg ${ARGV})
		# Ignore first
		if("${arg}" STREQUAL "${libBase}")
			continue()
		endif()

		list(APPEND libBaseLibs "${arg}")
	endforeach()

	# Only link for the dynamic library
	target_link_libraries(${libBase}DyLib PUBLIC
		${libBaseLibs})

	# Otherwise set transient libraries to be included, for use with
	# $<TARGET_PROPERTY:Target,SQUIRRELJME_LINK_LIBRARIES>
	set_target_properties(${libBase} PROPERTIES
		SQUIRRELJME_LINK_LIBRARIES "${libBaseLibs}")
	set(SQUIRRELJME_LINK_LIBRARIES_${libBase}
		${libBaseLibs})
endmacro()

# Output locations for binaries
macro(squirreljme_multilib_target_binary_output libBase where)
	# Static library output
	squirreljme_target_binary_output(${libBase}Static
		${where})

	# Dynamic library output
	squirreljme_target_binary_output(${libBase}DyLib
		${where})
endmacro()

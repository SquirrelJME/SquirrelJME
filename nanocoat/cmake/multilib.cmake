# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Multiple library declarations and otherwise

# Correct Paths
## Emulator base import directory
if(DEFINED SQUIRRELJME_EMULATOR_BASE_IMPORT_DIR)
	file(TO_CMAKE_PATH "${SQUIRRELJME_EMULATOR_BASE_IMPORT_DIR}"
		SQUIRRELJME_EMULATOR_BASE_IMPORT_DIR)
endif()

## Dynamic library output
if(DEFINED SQUIRRELJME_DYLIB_OUTPUT_DIR)
	file(TO_CMAKE_PATH "${SQUIRRELJME_DYLIB_OUTPUT_DIR}"
		SQUIRRELJME_DYLIB_OUTPUT_DIR)
endif()

## Binary output
if(DEFINED SQUIRRELJME_BINARY_OUTPUT_DIR)
	file(TO_CMAKE_PATH "${SQUIRRELJME_BINARY_OUTPUT_DIR}"
		SQUIRRELJME_BINARY_OUTPUT_DIR)
elseif(DEFINED SQUIRRELJME_DYLIB_OUTPUT_DIR)
	file(TO_CMAKE_PATH "${SQUIRRELJME_DYLIB_OUTPUT_DIR}"
		SQUIRRELJME_BINARY_OUTPUT_DIR)
endif()

# Add static library
macro(squirreljme_multilib_add_static_library libBase)
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
	if(SQUIRRELJME_ENABLE_FPIC)
		add_library(${libBase}PIC OBJECT
			${libBaseSources})

		set_property(TARGET ${libBase}PIC
			PROPERTY POSITION_INDEPENDENT_CODE ON)
	endif()

	# Static Library
	add_library(${libBase}Static STATIC
		${libBaseSources})

	if(SQUIRRELJME_ENABLE_FPIC)
		set_property(TARGET ${libBase}Static
			PROPERTY POSITION_INDEPENDENT_CODE ON)
	endif()

	# Use FPIC objects for a given target or not?
	# Unfortunately, for these to be truly global these must be in the cache
	if(SQUIRRELJME_ENABLE_FPIC)
		set(SQUIRRELJME_LIB_${libBase}_STATIC ${libBase}Static
			CACHE STRING "Static ${libBase} Target" FORCE)
		set(SQUIRRELJME_LIB_${libBase}_FIXED ${libBase}
			CACHE STRING "Non-FPIC ${libBase} Target" FORCE)
		set(SQUIRRELJME_LIB_${libBase}_FPIC ${libBase}PIC
			CACHE STRING "FPIC ${libBase} Target" FORCE)
	else()
		set(SQUIRRELJME_LIB_${libBase}_STATIC ${libBase}Static
			CACHE STRING "Static ${libBase} Target" FORCE)
		set(SQUIRRELJME_LIB_${libBase}_FIXED ${libBase}
			CACHE STRING "Non-FPIC ${libBase} Target" FORCE)
		set(SQUIRRELJME_LIB_${libBase}_FPIC ${libBase}
			CACHE STRING "FPIC ${libBase} Target" FORCE)
	endif()
endmacro()


# Make a target always FPIC
function(squirreljme_always_fpic target)
	if(SQUIRRELJME_ENABLE_FPIC)
		set_target_properties(${target} PROPERTIES
			POSITION_INDEPENDENT_CODE ON)
	endif()
endfunction()

# Add definitions for shared library builds
function(squirreljme_dylib_standard_properties target)
	target_compile_definitions(${target} PRIVATE
		"SJME_CONFIG_MULTILIB_IS_DYLIB=1")

	# Always try to enable FPIC for dynamic libraries
	squirreljme_always_fpic(${target})
endfunction()

# Add multi-lib library
macro(squirreljme_multilib_add_library libBase)
	# Bring in statics
	squirreljme_multilib_add_static_library(${ARGV})

	# Shared Library
	if(SQUIRRELJME_ENABLE_DYLIB)
		# Load in source files
		set(libBaseSources)
		foreach(arg ${ARGV})
			# Ignore first
			if("${arg}" STREQUAL "${libBase}")
				continue()
			endif()

			list(APPEND libBaseSources "${arg}")
		endforeach()

		add_library(${libBase}DyLib SHARED
			${libBaseSources})
		squirreljme_dylib_standard_properties(${libBase}DyLib)
	endif()
endmacro()

# Add include directories to static multilib library
macro(squirreljme_multilib_static_target_include_directories libBase)
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

	if(SQUIRRELJME_ENABLE_FPIC)
		target_include_directories(${libBase}PIC PUBLIC
			${libBaseIncludes})
	endif()

	target_include_directories(${libBase}Static PUBLIC
		${libBaseIncludes})
endmacro()

# Add compile definitions to multilib library
macro(squirreljme_multilib_target_compile_definitions target scope what)
	# Set on object target
	target_compile_definitions(${target} ${scope}
		${what})

	# Set on static target
	target_compile_definitions(${target}Static ${scope}
		${what})

	# And on static FPIC target
	if(SQUIRRELJME_ENABLE_FPIC)
		target_compile_definitions(${target}PIC ${scope}
			${what})
	endif()

	# And on the library target
	if(SQUIRRELJME_ENABLE_DYLIB)
		target_compile_definitions(${target}DyLib ${scope}
			${what})
	endif()
endmacro()

# Change the name of the library
macro(squirreljme_multilib_target_binary_name target name)
	# Set on static target
	squirreljme_target_binary_name(${target}Static ${name})

	# And on static FPIC target
	if(SQUIRRELJME_ENABLE_FPIC)
		squirreljme_target_binary_name(${target}PIC ${name})
	endif()

	# And on the library target
	if(SQUIRRELJME_ENABLE_DYLIB)
		squirreljme_target_binary_name(${target}DyLib ${name})
	endif()
endmacro()

# Add include directories to multilib library
macro(squirreljme_multilib_target_include_directories libBase)
	# Use static variant
	squirreljme_multilib_static_target_include_directories(${ARGV})

	if(SQUIRRELJME_ENABLE_DYLIB)
		# Load in include paths
		set(libBaseIncludes)
		foreach(arg ${ARGV})
			# Ignore first
			if("${arg}" STREQUAL "${libBase}")
				continue()
			endif()

			list(APPEND libBaseIncludes "${arg}")
		endforeach()

		target_include_directories(${libBase}DyLib PUBLIC
			${libBaseIncludes})
	endif()
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
	if(SQUIRRELJME_ENABLE_DYLIB)
		target_link_directories(${libBase}DyLib PUBLIC
			${libBaseLibDirs})
	endif()

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
	if(SQUIRRELJME_ENABLE_DYLIB)
		target_link_libraries(${libBase}DyLib PUBLIC
			${libBaseLibs})
	endif()

	# Otherwise set transient libraries to be included, for use with
	# $<TARGET_PROPERTY:Target,SQUIRRELJME_LINK_LIBRARIES>
	set_target_properties(${libBase} PROPERTIES
		SQUIRRELJME_LINK_LIBRARIES "${libBaseLibs}")
	set(SQUIRRELJME_LINK_LIBRARIES_${libBase}
		${libBaseLibs})
endmacro()

# Link multilib against required core libraries
macro(squirreljme_multilib_target_link_libraries_required libBase)
	# Dynamic library output
	if(SQUIRRELJME_ENABLE_DYLIB)
		squirreljme_target_link_libraries_required(${libBase}DyLib)
	endif()
endmacro()

# Output locations for binaries
macro(squirreljme_multilib_target_binary_output libBase where)
	# Static library output
	squirreljme_target_binary_output(${libBase}Static
		${where})

	# Dynamic library output
	if(SQUIRRELJME_ENABLE_DYLIB)
		squirreljme_target_binary_output(${libBase}DyLib
			${where})
	endif()
endmacro()

# Add dependency on library, static only
macro(squirreljme_multilib_static_add_dependency libBase dependOn)
	# Static library
	add_dependencies(${libBase}
		${dependOn})

	# PIC?
	if(SQUIRRELJME_ENABLE_FPIC)
		add_dependencies(${libBase}PIC
			${dependOn})
	endif()
endmacro()

# Add dependency on library
macro(squirreljme_multilib_add_dependency libBase dependOn)
	# Static
	squirreljme_multilib_static_add_dependency(${libBase} ${dependOn})

	# Only link for the dynamic library
	if(SQUIRRELJME_ENABLE_DYLIB)
		add_dependencies(${libBase}DyLib
			${SQUIRRELJME_LIB_${dependOn}_FPIC})
	endif()
endmacro()

# Add dependency on multi-lib binaries, static only
macro(squirreljme_multilib_static_add_multilib_dependency libBase dependOn)
	# Base non-PIC object
	add_dependencies(${libBase}
		${dependOn})

	# PIC?
	if(SQUIRRELJME_ENABLE_FPIC)
		add_dependencies(${libBase}PIC
			${SQUIRRELJME_LIB_${dependOn}_FPIC})
	endif()

	# Static Library
	add_dependencies(${libBase}Static
		${SQUIRRELJME_LIB_${dependOn}_STATIC})
endmacro()

# Add dependency on multi-lib binaries
macro(squirreljme_multilib_add_multilib_dependency libBase dependOn)
	# Static
	squirreljme_multilib_static_add_multilib_dependency(${libBase} ${dependOn})

	if(SQUIRRELJME_ENABLE_DYLIB)
		add_dependencies(${libBase}DyLib
			${dependOn}DyLib)
	endif()
endmacro()

# Export multilib targets
macro(squirreljme_multilib_export target)
	# There are multiple branching paths based on the configuration
	if(SQUIRRELJME_ENABLE_FPIC)
		if(SQUIRRELJME_ENABLE_DYLIB)
			export(TARGETS ${target}
				${target}Static
				${target}PIC
				${target}DyLib
				FILE SquirrelJME${target}.cmake
				NAMESPACE SquirrelJME::)
		else()
			export(TARGETS ${target}
				${target}Static
				${target}PIC
				FILE SquirrelJME${target}.cmake
				NAMESPACE SquirrelJME::)
		endif()
	else()
		if(SQUIRRELJME_ENABLE_DYLIB)
			export(TARGETS ${target}
				${target}Static
				${target}DyLib
				FILE SquirrelJME${target}.cmake
				NAMESPACE SquirrelJME::)
		else()
			export(TARGETS ${target}
				${target}Static
				${target}PIC
				FILE SquirrelJME${target}.cmake
				NAMESPACE SquirrelJME::)
		endif()
	endif()
endmacro()


# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Multiple library declarations and otherwise

# Needed for directory setups
include(GNUInstallDirs)

# Properties for multilib targets
define_property(TARGET PROPERTY SQUIRRELJME_MULTILIB_TYPE
	BRIEF_DOCS "The type of target this is."
	FULL_DOCS "The type of target this is.")

# Correct Paths
## Emulator base import directory
if(DEFINED SQUIRRELJME_EMULATOR_BASE_IMPORT_DIR)
	file(TO_CMAKE_PATH "${SQUIRRELJME_EMULATOR_BASE_IMPORT_DIR}"
		SQUIRRELJME_EMULATOR_BASE_IMPORT_DIR)
endif()

# These cannot directly be tested via if()
if(DEFINED ENV{SQUIRRELJME_BINARY_OUTPUT_ROOT})
	set(SQUIRRELJME_BINARY_OUTPUT_ROOT
		"$ENV{SQUIRRELJME_BINARY_OUTPUT_ROOT}")
endif()

## Dynamic library output
if(DEFINED SQUIRRELJME_BINARY_OUTPUT_ROOT AND
	SQUIRRELJME_BINARY_OUTPUT_ROOT)
	message(STATUS "Outputting dylibs to the build root...")
	file(TO_CMAKE_PATH "${CMAKE_BINARY_DIR}"
		SQUIRRELJME_DYLIB_OUTPUT_DIR)
elseif(DEFINED SQUIRRELJME_DYLIB_OUTPUT_DIR)
	file(TO_CMAKE_PATH "${SQUIRRELJME_DYLIB_OUTPUT_DIR}"
		SQUIRRELJME_DYLIB_OUTPUT_DIR)
elseif(DEFINED ENV{SQUIRRELJME_DYLIB_OUTPUT_DIR})
	file(TO_CMAKE_PATH "$ENV{SQUIRRELJME_DYLIB_OUTPUT_DIR}"
		SQUIRRELJME_DYLIB_OUTPUT_DIR)
endif()

## Binary output
if(DEFINED SQUIRRELJME_BINARY_OUTPUT_ROOT AND
	SQUIRRELJME_BINARY_OUTPUT_ROOT)
	message(STATUS "Outputting binaries to the build root...")
	file(TO_CMAKE_PATH "${CMAKE_BINARY_DIR}"
		SQUIRRELJME_BINARY_OUTPUT_DIR)
elseif(DEFINED SQUIRRELJME_BINARY_OUTPUT_DIR)
	file(TO_CMAKE_PATH "${SQUIRRELJME_BINARY_OUTPUT_DIR}"
		SQUIRRELJME_BINARY_OUTPUT_DIR)
elseif(DEFINED ENV{SQUIRRELJME_BINARY_OUTPUT_DIR})
	file(TO_CMAKE_PATH "$ENV{SQUIRRELJME_BINARY_OUTPUT_DIR}"
		SQUIRRELJME_BINARY_OUTPUT_DIR)
elseif(DEFINED SQUIRRELJME_DYLIB_OUTPUT_DIR)
	file(TO_CMAKE_PATH "${SQUIRRELJME_DYLIB_OUTPUT_DIR}"
		SQUIRRELJME_BINARY_OUTPUT_DIR)
else()
	set(SQUIRRELJME_BINARY_OUTPUT_DIR
		"${CMAKE_BINARY_DIR}/bin")
endif()

# Check that the scope is valid
function(squirreljme_check_valid_scope scope)
	# Scope is wrong?
	if(NOT "${scope}" STREQUAL "PRIVATE" AND
		NOT "${scope}" STREQUAL "PUBLIC" AND
		NOT "${scope}" STREQUAL "INTERFACE" AND
		NOT "${scope}" STREQUAL "NONE")
		message(FATAL_ERROR "${scope} is not a valid scope!")
	endif()
endfunction()

# Add static library
function(squirreljme_multilib_add_static_library libBase)
	# Load in source files
	set(sourcesList "${ARGV}")
	list(REMOVE_AT sourcesList 0)

	# Object Library
	add_library(${libBase} OBJECT
		${sourcesList})

	# Always FPIC
	squirreljme_always_fpic(${libBase})

	# Properties as needed
	set_target_properties(${libBase} PROPERTIES
		SQUIRRELJME_MULTILIB_TYPE OBJECT_LIBRARY
		SQUIRRELJME_MULTILIB_FPIC ${SQUIRRELJME_ENABLE_FPIC}
		SQUIRRELJME_TARGET_OBJECTS
			"$<TARGET_GENEX_EVAL:${libBase},$<TARGET_OBJECTS:${libBase}>>")

	# Static library
	add_library(${libBase}Static STATIC
		"${SQUIRRELJME_BP_LIST_DIR}/blank.c"
		"$<TARGET_GENEX_EVAL:${libBase},$<TARGET_OBJECTS:${libBase}>>")

	# Always FPIC
	squirreljme_always_fpic(${libBase}Static)

	# Properties as needed
	set_target_properties(${libBase}Static PROPERTIES
		SQUIRRELJME_MULTILIB_TYPE STATIC_LIBRARY
		SQUIRRELJME_MULTILIB_FPIC ${SQUIRRELJME_ENABLE_FPIC}
		SQUIRRELJME_TARGET_OBJECTS
			"$<TARGET_GENEX_EVAL:${libBase},$<TARGET_OBJECTS:${libBase}>>")

	# Variable reference names
	# Unfortunately, for these to be truly global these must be in the cache
	set(SQUIRRELJME_LIB_${libBase}_STATIC ${libBase}Static
		CACHE STRING "Static ${libBase} Target" FORCE)
	set(SQUIRRELJME_LIB_${libBase}_OBJECT ${libBase}
		CACHE STRING "Object ${libBase} Target" FORCE)
endfunction()

# Add definitions for shared library builds
function(squirreljme_dylib_standard_properties target)
	target_compile_definitions(${target} PRIVATE
		"SJME_CONFIG_MULTILIB_IS_DYLIB=1")

	# Always try to enable FPIC for dynamic libraries
	squirreljme_always_fpic(${target})
endfunction()

# Add multi-lib library
function(squirreljme_multilib_add_library libBase)
	# Bring in statics
	squirreljme_multilib_add_static_library(${ARGV})

	# Shared Library
	if(SQUIRRELJME_ENABLE_DYLIB)
		# Setup library
		add_library(${libBase}DyLib SHARED
			"${SQUIRRELJME_BP_LIST_DIR}/blank.c"
			"$<TARGET_GENEX_EVAL:${libBase},$<TARGET_OBJECTS:${libBase}>>")

		# Always FPIC
		squirreljme_always_fpic(${libBase}DyLib)

		# Properties as needed
		set_target_properties(${libBase}DyLib PROPERTIES
			SQUIRRELJME_MULTILIB_TYPE SHARED_LIBRARY
			SQUIRRELJME_MULTILIB_FPIC ${SQUIRRELJME_ENABLE_FPIC}
			SQUIRRELJME_TARGET_OBJECTS
				"$<TARGET_GENEX_EVAL:${libBase},$<TARGET_OBJECTS:${libBase}>>")

		# Set standard properties
		squirreljme_dylib_standard_properties(${libBase}DyLib)

		# Dynamic library target reference
		set(SQUIRRELJME_LIB_${libBase}_DYLIB ${libBase}DyLib
			CACHE STRING "Dynamic ${libBase} Target" FORCE)
	endif()
endfunction()

# Set properties on all static targets
function(squirreljme_multilib_static_properties target properties)
	# Must be PROPERTIES
	if(NOT "${properties}" STREQUAL "PROPERTIES")
		message(FATAL_ERROR "PROPERTIES is missing!")
	endif()

	# Load in properties
	set(properties "${ARGV}")
	list(REMOVE_AT properties 0)
	list(REMOVE_AT properties 0)

	# Plain Objects
	set_target_properties(${target} PROPERTIES
		${properties})

	# Static library
	set_target_properties(${target}Static PROPERTIES
		${properties})
endfunction()

# Set properties on all targets
function(squirreljme_multilib_properties target properties)
	# Must be PROPERTIES
	if(NOT "${properties}" STREQUAL "PROPERTIES")
		message(FATAL_ERROR "PROPERTIES is missing!")
	endif()

	# Set static targets
	squirreljme_multilib_static_properties(${ARGV})

	# Load in properties
	set(properties "${ARGV}")
	list(REMOVE_AT properties 0)
	list(REMOVE_AT properties 0)

	# Dynamic library
	if(SQUIRRELJME_ENABLE_DYLIB)
		set_target_properties(${target}DyLib PROPERTIES
			${properties})
	endif()
endfunction()

# Add include directories to static multilib library
function(squirreljme_multilib_static_include_directories libBase scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Load in include paths
	set(includesList "${ARGV}")
	list(REMOVE_AT includesList 0)
	list(REMOVE_AT includesList 0)

	# Set includes
	target_include_directories(${libBase} ${scope}
		${includesList})

	# Only needed for interfaces?
	if(NOT "${scope}" STREQUAL "PRIVATE")
		target_include_directories(${libBase}Static INTERFACE
			${includesList})
	endif()
endfunction()

# Add compile definitions to multilib static library
function(squirreljme_multilib_static_compile_definitions target scope what)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Load in options
	set(addOptions "${ARGV}")
	list(REMOVE_AT addOptions 0)
	list(REMOVE_AT addOptions 0)

	# Set on object target
	target_compile_definitions(${target} ${scope}
		${addOptions})

	# Set on static target
	target_compile_definitions(${target}Static ${scope}
		${addOptions})
endfunction()

# Add compile definitions to multilib library
function(squirreljme_multilib_compile_definitions target scope what)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Set on statics
	squirreljme_multilib_static_compile_definitions(${ARGV})

	# Load in options
	set(addOptions "${ARGV}")
	list(REMOVE_AT addOptions 0)
	list(REMOVE_AT addOptions 0)

	# And on the shared library
	if(SQUIRRELJME_ENABLE_DYLIB)
		target_compile_definitions(${target}DyLib ${scope}
			${addOptions})
	endif()
endfunction()

# Add compile options to multilib static library
function(squirreljme_multilib_static_compile_options target scope what)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Load in options
	set(addOptions "${ARGV}")
	list(REMOVE_AT addOptions 0)
	list(REMOVE_AT addOptions 0)

	# Set on object target
	target_compile_options(${target} ${scope}
		${addOptions})

	# Set on static target
	target_compile_options(${target}Static ${scope}
		${addOptions})
endfunction()

# Add compile options to multilib library
function(squirreljme_multilib_compile_options target scope what)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Set on statics
	squirreljme_multilib_static_compile_options(${ARGV})

	# Load in options
	set(addOptions "${ARGV}")
	list(REMOVE_AT addOptions 0)
	list(REMOVE_AT addOptions 0)

	# And on the shared library
	if(SQUIRRELJME_ENABLE_DYLIB)
		target_compile_options(${target}DyLib ${scope}
			${addOptions})
	endif()
endfunction()

# Add link options to multilib static library
function(squirreljme_multilib_static_link_options target scope what)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Load in options
	set(addOptions "${ARGV}")
	list(REMOVE_AT addOptions 0)
	list(REMOVE_AT addOptions 0)

	# Set on object target
	target_link_options(${target} ${scope}
		${addOptions})

	# Set on static target
	target_link_options(${target}Static ${scope}
		${addOptions})
endfunction()

# Add link options to multilib library
function(squirreljme_multilib_link_options target scope what)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Set on statics
	squirreljme_multilib_static_link_options(${ARGV})

	# Load in options
	set(addOptions "${ARGV}")
	list(REMOVE_AT addOptions 0)
	list(REMOVE_AT addOptions 0)

	# And on the shared library
	if(SQUIRRELJME_ENABLE_DYLIB)
		target_link_options(${target}DyLib ${scope}
			${addOptions})
	endif()
endfunction()

# Change the name of the library (dynamic)
function(squirreljme_multilib_binary_name target name)
	# Set on static target
	squirreljme_target_binary_name(${target}Static ${name})

	# And on the library target
	if(SQUIRRELJME_ENABLE_DYLIB)
		squirreljme_target_binary_name(${target}DyLib ${name})
	endif()
endfunction()

# Add include directories to multilib library
function(squirreljme_multilib_include_directories libBase scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Use static variant
	squirreljme_multilib_static_include_directories(${ARGV})

	if(SQUIRRELJME_ENABLE_DYLIB)
		# Load in include paths
		set(includesList "${ARGV}")
		list(REMOVE_AT includesList 0)
		list(REMOVE_AT includesList 0)

		# Only needed for interfaces?
		if(NOT "${scope}" STREQUAL "PRIVATE")
			target_include_directories(${libBase}DyLib INTERFACE
				${includesList})
		endif()
	endif()
endfunction()

# Multi-lib library directories
function(squirreljme_multilib_link_directories libBase scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Load in library paths
	set(libraryDirs "${ARGV}")
	list(REMOVE_AT libraryDirs 0)
	list(REMOVE_AT libraryDirs 0)

	# Add for the static library, but only at the interface level
	if(NOT "${scope}" STREQUAL "PRIVATE")
		target_link_directories(${libBase}Static INTERFACE
			${libraryDirs})
	endif()

	# Add for the dynamic library as well
	if(SQUIRRELJME_ENABLE_DYLIB)
		target_link_directories(${libBase}DyLib ${scope}
			${libraryDirs})
	endif()

	# Otherwise set transient library directories to be included, for use with
	# $<TARGET_PROPERTY:Target,SQUIRRELJME_LINK_DIRECTORIES>
	set_target_properties(${libBase} PROPERTIES
		SQUIRRELJME_LINK_DIRECTORIES "${libraryDirs}")
	set(SQUIRRELJME_LINK_DIRECTORIES_${libBase}
		${libraryDirs})
endfunction()

# Static linking of libraries
function(squirreljme_multilib_static_link_libraries_required libBase scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Load in library paths
	set(libraryPaths "${ARGV}")
	list(REMOVE_AT libraryPaths 0)
	list(REMOVE_AT libraryPaths 0)

	# Add for the static library, but only at the public/interface level
	if(NOT "${scope}" STREQUAL "PRIVATE")
		squirreljme_link_libraries_required(${libBase}Static ${scope}
			${libraryPaths})
	endif()

	# Otherwise set transient libraries to be included, for use with
	# $<TARGET_PROPERTY:Target,SQUIRRELJME_LINK_LIBRARIES>
	set_target_properties(${libBase} PROPERTIES
		SQUIRRELJME_LINK_LIBRARIES "${libraryPaths}")
	set(SQUIRRELJME_LINK_LIBRARIES_${libBase}
		${libraryPaths})
endfunction()

# Multi-lib linking of libraries
function(squirreljme_multilib_link_libraries_required libBase scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Static?
	squirreljme_multilib_static_link_libraries_required(${ARGV})

	# Dynamic library linking?
	if(SQUIRRELJME_ENABLE_DYLIB)
		# Load in library list
		set(libraries "${ARGV}")
		list(REMOVE_AT libraries 0)

		# Set libraries
		squirreljme_link_libraries_required(${libBase}DyLib ${scope}
			${libraries})
	endif()
endfunction()

# Output locations for binaries
function(squirreljme_multilib_binary_output libBase where)
	# Static library output
	squirreljme_target_binary_output(${libBase}Static
		${where})

	# Dynamic library output
	if(SQUIRRELJME_ENABLE_DYLIB)
		squirreljme_target_binary_output(${libBase}DyLib
			${where})
	endif()
endfunction()

# Add dependency on a plain target
function(squirreljme_multilib_static_add_dependency libBase)
	# Load in dependency list
	set(dependOn "${ARGV}")
	list(REMOVE_AT dependOn 0)

	# Static library
	add_dependencies(${libBase}
		${dependOn})

	# Static library
	add_dependencies(${libBase}Static
		${dependOn})
endfunction()

# Add dependency on a plain target
function(squirreljme_multilib_add_dependency libBase dependOn)
	# Load in dependency list
	set(dependOn "${ARGV}")
	list(REMOVE_AT dependOn 0)

	# Static
	squirreljme_multilib_static_add_dependency(${ARGV})

	# Only link for the dynamic library
	if(SQUIRRELJME_ENABLE_DYLIB)
		foreach(subDepend IN LISTS dependOn)
			add_dependencies(${libBase}DyLib
				${subDepend})
		endforeach()
	endif()
endfunction()

# Add dependency on a multi-lib library
# - AUTOMATIC: Depend on the best matching library
# - STATIC: Force depend on the static library
# - SHARED: Force depend on the shared library
function(squirreljme_add_dependency_multilib target type)
	# Must be either AUTOMATIC, STATIC, or SHARED
	if(NOT "${type}" STREQUAL "AUTOMATIC" AND
		NOT "${type}" STREQUAL "STATIC" AND
		NOT "${type}" STREQUAL "SHARED")
		message(FATAL_ERROR "Multilib dependency must be either AUTOMATIC, "
			"STATIC, or SHARED.")
	endif()

	# Load in dependency list
	set(dependOn "${ARGV}")
	list(REMOVE_AT dependOn 0)

	# Then for each dependency...
	foreach(subDepend IN LISTS dependOn)
		# Force static dependency?
		if("${type}" STREQUAL "STATIC")
			# Use the object library
			add_dependencies(${target}
				${SQUIRRELJME_LIB_${subDepend}_OBJECT})

		# Force shared dependency?
		elseif("${type}" STREQUAL "SHARED")
			# We need dynamic libraries
			if(NOT SQUIRRELJME_ENABLE_DYLIB)
				message(FATAL_ERROR "Depending on shared when dynamic "
					"libraries are not supported!")
			endif()

			# Use the dynamic library
			add_dependencies(${target}
				${SQUIRRELJME_LIB_${subDepend}_DYLIB})

		# Otherwise, automatic determination
		else()
			# Get the target details
			get_target_property(targetType ${target}
				TYPE)
			get_target_property(isTargetFPIC ${target}
				POSITION_INDEPENDENT_CODE)

			# Object libraries
			if("${targetType}" STREQUAL "OBJECT_LIBRARY" OR
				"${targetType}" STREQUAL "STATIC_LIBRARY")
				if(isTargetFPIC)
					# Use FPIC
					add_dependencies(${target}
						${SQUIRRELJME_LIB_${subDepend}_OBJECT})
				else()
					# Do not use FPIC
					add_dependencies(${target}
						${SQUIRRELJME_LIB_${subDepend}_OBJECT})
				endif()

			# Shared libraries and executables
			elseif("${targetType}" STREQUAL "SHARED_LIBRARY" OR
				"${targetType}" STREQUAL "EXECUTABLE")
				# Depend on the dynamic library
				add_dependencies(${target}
					${SQUIRRELJME_LIB_${subDepend}_DYLIB})
			endif()
		endif()
	endforeach()
endfunction()

# Add dependency on multi-lib binaries, static only
function(squirreljme_multilib_static_add_multilib_dependency libBase type)
	# Load in dependency list
	set(dependOn "${ARGV}")
	list(REMOVE_AT dependOn 0)
	list(REMOVE_AT dependOn 0)

	# Object Library
	squirreljme_add_dependency_multilib(${libBase} ${type}
		${dependOn})

	# Static Library
	squirreljme_add_dependency_multilib(${libBase}Static ${type}
		${dependOn})
endfunction()

# Add dependency on multi-lib binaries
function(squirreljme_multilib_add_multilib_dependency libBase type)
	# Static
	squirreljme_multilib_static_add_multilib_dependency(${ARGV})

	# Dynamic library
	if(SQUIRRELJME_ENABLE_DYLIB)
		# Load in dependency list
		set(dependOn "${ARGV}")
		list(REMOVE_AT dependOn 0)
		list(REMOVE_AT dependOn 0)

		# Forward
		squirreljme_add_dependency_multilib(${libBase}DyLib
			${type} ${dependOn})
	endif()
endfunction()

# Export single target
function(squirreljme_export target)
	export(TARGETS ${target}
		FILE "${CMAKE_BINARY_DIR}/export/${target}.cmake")
endfunction()

# Export multilib targets
function(squirreljme_multilib_export target)
	# There are multiple branching paths based on the configuration
	if(SQUIRRELJME_ENABLE_DYLIB AND TARGET ${target}DyLib)
		export(TARGETS
			${target}DyLib
			${target}Static
			${target}
			FILE "${CMAKE_BINARY_DIR}/export/${target}.cmake"
			NAMESPACE SquirrelJME::)
	else()
		export(TARGETS
			${target}Static
			${target}
			FILE "${CMAKE_BINARY_DIR}/export/${target}.cmake"
			NAMESPACE SquirrelJME::)
	endif()
endfunction()

# Export and install for multilib
function(squirreljme_multilib_install target)
	# Set base directories
	if(squirreljme_bp_version_3_23)
		target_sources(${target}
			PUBLIC FILE_SET HEADERS
			BASE_DIRS "${CMAKE_SOURCE_DIR}/include")
	else()
		message(WARNING "This CMake version does not support setting "
			"a specific base directory for headers.")
	endif()

	# Export libraries
	if(SQUIRRELJME_ENABLE_DYLIB)
		install(TARGETS ${target}DyLib
			${target}Static)
	else()
		install(TARGETS ${target}Static)
	endif()
endfunction()


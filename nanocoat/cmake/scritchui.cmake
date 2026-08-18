# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: ScritchAny CMake Macros

# Properties for ScritchAny libraries and pseudo targets
define_property(TARGET PROPERTY SCRITCHANY_OUTPUT_PATH
	BRIEF_DOCS "The output paths for the ScritchAny binaries."
	FULL_DOCS "The output paths for the ScritchAny binaries.")
define_property(TARGET PROPERTY SCRITCHANY_OUTPUT_PATH_LIST
	BRIEF_DOCS "The output path for the ScritchAny list, in a list file."
	FULL_DOCS "The output path for the ScritchAny list, in a list file.")
define_property(TARGET PROPERTY SCRITCHANY_OUTPUT_LIST
	BRIEF_DOCS "The output path for the ScritchAny list."
	FULL_DOCS "The output path for the ScritchAny list.")
define_property(TARGET PROPERTY SCRITCHANY_AREA
	BRIEF_DOCS "The ScritchAny area."
	FULL_DOCS "The ScritchAny area.")
define_property(TARGET PROPERTY SCRITCHANY_INTERFACE
	BRIEF_DOCS "Interfaces which are available for the area."
	FULL_DOCS "Interfaces which are available for the area.")

# Convert to noun
function(squirreljme_noun result input)
	# Split and uppercase the first
	string(SUBSTRING "${input}" 0 1 pfx)
	string(TOUPPER "${pfx}" pfx)
	string(SUBSTRING "${input}" 1 -1 sfx)

	# Return the result
	set(${result} "${pfx}${sfx}"
		PARENT_SCOPE)
endfunction()

# Standard ScritchAny target name
function(squirreljme_scritchany_target_name result area target)
	squirreljme_noun(areaNoun "${area}")
	set(${result} "Scritch${areaNoun}${target}"
		PARENT_SCOPE)
endfunction()

# Declare a valid ScritchAny area
macro(squirreljme_scritchany_declare area)
	# Determine the area noun
	squirreljme_noun(areaNoun "${area}")

	# Determine the pseudo targets
	set(targetAll "Scritch${areaNoun}")
	set(targetZip "Scritch${areaNoun}Zip")

	# Determine base output
	set(basePath
		"${SQUIRRELJME_BINARY_OUTPUT_DIR}/Scritch${areaNoun}")

	# Setup All Target
	if(squirreljme_bp_version_3_12)
		add_custom_target(${targetAll}
			COMMAND "${CMAKE_COMMAND}" "-E"
				"echo"
		"$<GENEX_EVAL:$<TARGET_PROPERTY:${targetAll},SCRITCHANY_INTERFACE>>"
					">" "${basePath}.list"
			COMMAND "${CMAKE_COMMAND}" "-E"
				"echo"
		"$<GENEX_EVAL:$<TARGET_PROPERTY:${targetAll},SCRITCHANY_OUTPUT_PATH>>"
					">" "${basePath}.paths"
			BYPRODUCTS "${basePath}.list" "${basePath}.paths"
			VERBATIM
			WORKING_DIRECTORY "${SQUIRRELJME_BINARY_OUTPUT_DIR}"
			COMMENT "Producing Scritch${areaNoun} list..."
			COMMAND_EXPAND_LISTS)
	else()
		add_custom_target(${targetAll}
			COMMAND "${CMAKE_COMMAND}" "-E"
				"echo"
				"$<TARGET_PROPERTY:${targetAll},SCRITCHANY_INTERFACE>"
					">" "${basePath}.list"
			COMMAND "${CMAKE_COMMAND}" "-E"
				"echo"
				"$<TARGET_PROPERTY:${targetAll},SCRITCHANY_OUTPUT_PATH>"
					">" "${basePath}.paths"
			BYPRODUCTS "${basePath}.list" "${basePath}.paths"
			VERBATIM
			WORKING_DIRECTORY "${SQUIRRELJME_BINARY_OUTPUT_DIR}"
			COMMENT "Producing Scritch${areaNoun} list..."
			COMMAND_EXPAND_LISTS)
	endif()

	# Properties for all
	set_target_properties(${targetAll} PROPERTIES
		SCRITCHANY_AREA "${area}"
		SCRITCHANY_INTERFACE ""
		SCRITCHANY_OUTPUT_PATH ""
		SCRITCHANY_OUTPUT_LIST "${basePath}.list"
		SCRITCHANY_OUTPUT_PATH_LIST "${basePath}.paths")

	# Setup Zip Target
	add_custom_target(${targetZip}
		DEPENDS ${targetAll}
		COMMAND "${CMAKE_COMMAND}" "-E"
			"tar" "cv" "${basePath}.zip"
				"--format=zip" "--files-from=${basePath}.paths" "--"
		BYPRODUCTS "${basePath}.zip"
		WORKING_DIRECTORY "${SQUIRRELJME_BINARY_OUTPUT_DIR}"
		COMMENT "Bundling Scritch${areaNoun} libraries..."
		COMMAND_EXPAND_LISTS)

	# Properties for the zip bundle
	set_target_properties(${targetZip} PROPERTIES
		SCRITCHANY_AREA "${area}"
		SCRITCHANY_OUTPUT_PATH "${basePath}.zip")
endmacro()

# Pseudo ScritchAudio and ScritchUi targets, for "all" and export
squirreljme_scritchany_declare(audio)
squirreljme_scritchany_declare(ui)

# Declare ScritchAny implementation
function(squirreljme_scritchany_add_library area name)
	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Determine sources
	set(sourcesList "${ARGV}")
	list(REMOVE_AT sourcesList 0)
	list(REMOVE_AT sourcesList 0)

	# Which base and core libraries should be used?
	if("${area}" STREQUAL "ui")
		set(coreTarget ScritchUiCoreStatic)
	elseif("${area}" STREQUAL "audio")
		set(coreTarget ScritchAudioCoreStatic)
	endif()

	# Add the library accordingly
	squirreljme_multilib_add_library(${target}
		${sourcesList})

	# Link in base targets
	squirreljme_multilib_link_libraries_required(${target} PUBLIC
		${coreTarget})

	# Include main SquirrelJME headers
	squirreljme_multilib_include_directories(${target} PUBLIC
		"${CMAKE_SOURCE_DIR}/include")

	# This is rather important
	squirreljme_multilib_properties(${target} PROPERTIES
		SCRITCHANY_AREA "${area}"
		SCRITCHANY_INTERFACE "${name}")
endfunction()

# Add compile definitions
function(squirreljme_scritchany_compile_definitions area name scope)
	# Check scope
	squirreljme_check_valid_scope(${scope})

	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Determine arguments
	set(defines "${ARGV}")
	list(REMOVE_AT defines 0)
	list(REMOVE_AT defines 0)
	list(REMOVE_AT defines 0)

	# Add definitions
	squirreljme_multilib_compile_definitions(${target} ${scope}
		${defines})
endfunction()

# Added include directories
function(squirreljme_scritchany_include_directories area name scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Determine arguments
	set(directories "${ARGV}")
	list(REMOVE_AT directories 0)
	list(REMOVE_AT directories 0)
	list(REMOVE_AT directories 0)

	# Add directories
	squirreljme_multilib_include_directories(${target} ${scope}
		${directories})
endfunction()

# Added library directories
function(squirreljme_scritchany_link_directories area name scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Determine arguments
	set(directories "${ARGV}")
	list(REMOVE_AT directories 0)
	list(REMOVE_AT directories 0)
	list(REMOVE_AT directories 0)

	# Add directories
	squirreljme_multilib_link_directories(${target} ${scope}
		${directories})
endfunction()

# Added link libraries
function(squirreljme_scritchany_link_libraries area name scope)
	# Check the scope
	squirreljme_check_valid_scope(${scope})

	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Determine arguments
	set(libraries "${ARGV}")
	list(REMOVE_AT libraries 0)
	list(REMOVE_AT libraries 0)
	list(REMOVE_AT libraries 0)

	# Add libraries
	squirreljme_multilib_link_libraries_required(${target} ${scope}
		${libraries})
endfunction()

# Add compile options
function(squirreljme_scritchany_compile_options area name scope)
	# Check scope
	squirreljme_check_valid_scope(${scope})

	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Determine arguments
	set(defines "${ARGV}")
	list(REMOVE_AT defines 0)
	list(REMOVE_AT defines 0)
	list(REMOVE_AT defines 0)

	# Add options
	squirreljme_multilib_compile_options(${target} ${scope}
		${defines})
endfunction()

# Add link options
function(squirreljme_scritchany_link_options area name scope)
	# Check scope
	squirreljme_check_valid_scope(${scope})

	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Determine arguments
	set(defines "${ARGV}")
	list(REMOVE_AT defines 0)
	list(REMOVE_AT defines 0)
	list(REMOVE_AT defines 0)

	# Add options
	squirreljme_multilib_link_options(${target} ${scope}
		${defines})
endfunction()

# Binary name and output
function(squirreljme_scritchany_binary_name_and_output area name)
	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Lowercase the name
	string(TOLOWER "${name}" name)

	# The static library needs a different name, this is mostly for Windows
	# due to the DLL implib
	squirreljme_target_binary_name(${target}Static
		"squirreljme-scritch${area}-${name}-static")

	# ScritchAny requires a normalized library name
	if(SQUIRRELJME_ENABLE_DYLIB)
		squirreljme_target_binary_name(${target}DyLib
			"squirreljme-scritch${area}-${name}")
	endif()

	# The libraries are output to the same location
	squirreljme_multilib_binary_output(${target}
		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}")
endfunction()

# Library exports, so that others may use
function(squirreljme_scritchany_library_exports area name)
	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Generate exports
	squirreljme_multilib_export(${target})
	squirreljme_multilib_install(${target})
endfunction()

# Standard properties for everything
function(squirreljme_scritchany_standard_properties area name)
	# Binary name and output
	squirreljme_scritchany_binary_name_and_output(${area} ${name})

	# Exports of the ScritchAny libraries
	squirreljme_scritchany_library_exports(${area} ${name})
endfunction()

## Enables a Scritch library
#macro(squirreljme_scritchany_enable area capArea)
#	# Delete the old list
#	file(REMOVE "${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list")
#
#	# Make sure a blank file exists at least
#	if(${CMAKE_VERSION} VERSION_GREATER_EQUAL "3.12")
#		file(TOUCH "${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list")
#	else()
#		file(WRITE "${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list" "")
#	endif()
#
#	# Add collection target
#	add_custom_target(Scritch${capArea}CollectZip
#		COMMAND "${CMAKE_COMMAND}" -E "tar" "cv"
#		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}/libsquirreljme-scritch${area}.zip"
#			"--format=zip" "--"
#		"$<TARGET_PROPERTY:Scritch${capArea}CollectZip,sjmeLibraries>"
#		"${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list"
#		WORKING_DIRECTORY "${SQUIRRELJME_DYLIB_OUTPUT_DIR}"
#		BYPRODUCTS "${SQUIRRELJME_DYLIB_OUTPUT_DIR}/libsquirreljme-scritch${area}.zip"
#		COMMENT "Collects all Scritch${capArea} outputs into one single Zip."
#		COMMAND_EXPAND_LISTS)
#endmacro()

# Used to build SOs into lists
macro(squirreljme_scritchany_build area subDir name)
	# Determine the area noun
	squirreljme_noun(areaNoun "${area}")

	# Determine the target name
	squirreljme_scritchany_target_name(target "${area}" "${name}")

	# Determine the pseudo targets
	set(targetAll "Scritch${areaNoun}")
	set(targetZip "Scritch${areaNoun}Zip")

	# Notice!
	message(STATUS "Scritch${areaNoun}: Enabling ${name}!")

	# Include sub-directory for the build
	add_subdirectory(${subDir})

	# Prefer dynamic libraries where possible
	if(SQUIRRELJME_ENABLE_DYLIB)
		set(realTarget "${target}DyLib")
	else()
		set(realTarget "${target}Static")
	endif()

	# Depend on the target
	add_dependencies(${targetAll}
		${realTarget})

	# Add to the interfaces
	get_target_property(outInterface ${targetAll} SCRITCHANY_INTERFACE)
	list(APPEND outInterface "${name}")
	set_target_properties(${targetAll} PROPERTIES
		SCRITCHANY_INTERFACE "${outInterface}")

	# Add to the paths
	get_target_property(outPaths ${targetAll} SCRITCHANY_OUTPUT_PATH)
	if(squirreljme_bp_version_3_12)
		list(APPEND outPaths "$<GENEX_EVAL:$<TARGET_FILE:${realTarget}>>")
	else()
		list(APPEND outPaths "$<TARGET_FILE:${realTarget}>")
	endif()
	set_target_properties(${targetAll} PROPERTIES
		SCRITCHANY_OUTPUT_PATH "${outPaths}")

#	# Make the core targets depend on this
#
#	# Make ZIP collection depend on this
#	add_dependencies(Scritch${areaNoun}CollectZip
#		Scritch${areaNoun}${targetBase})
#
#	# Include the target into the collection list
#	file(APPEND
#		"${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list"
#		"${targetBase}")
#
#	# What is the library called?
#	get_target_property(libName Scritch${capArea}${targetBase}
#		LIBRARY_OUTPUT_NAME)
#	set(libName
#		"${CMAKE_SHARED_LIBRARY_PREFIX}${libName}${CMAKE_SHARED_LIBRARY_SUFFIX}")
#
#	# Add target library paths
#	get_target_property(targetLibs
#		Scritch${capArea}CollectZip sjmeLibraries)
#	if(targetLibs)
#		list(APPEND targetLibs "${libName}")
#	else()
#		set(targetLibs "${libName}")
#	endif()
#	set_target_properties(Scritch${capArea}CollectZip
#		PROPERTIES sjmeLibraries "${targetLibs}")
endmacro()

# Macro for more easily declaring ScritchUi sub-projects
macro(squirreljme_scritchui_build subDir targetBase)
	squirreljme_scritchany_build(ui ${subDir} ${targetBase})
endmacro()

# Macro for more easily declaring ScritchAudio sub-projects
macro(squirreljme_scritchaudio_build subDir targetBase)
	squirreljme_scritchany_build(audio ${subDir} ${targetBase})
endmacro()

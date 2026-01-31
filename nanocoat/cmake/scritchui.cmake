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
	BRIEF_DOCS "The output path for the ScritchAny binary."
	FULL_DOCS "The output path for the ScritchAny binary.")
define_property(TARGET PROPERTY SCRITCHANY_OUTPUT_LIST
	BRIEF_DOCS "The output path for the ScritchAny list."
	FULL_DOCS "The output path for the ScritchAny list.")
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

	message(FATAL_ERROR "TODO")
endmacro()

# Pseudo ScritchAudio and ScritchUI targets, for "all" and export
squirreljme_scritchany_declare(audio)
squirreljme_scritchany_declare(ui)

# Declare ScritchAny implementation
macro(squirreljme_scritchany_add_library area name)
	# Determine the target name
	squirreljme_scritchany_target_name(target area name)

	message(FATAL_ERROR "TODO")
endmacro()

# Added include directories
macro(squirreljme_scritchany_include_directories area target)
	# Determine the target name
	squirreljme_scritchany_target_name(target area name)

	message(FATAL_ERROR "TODO")
endmacro()

# Added library directories
macro(squirreljme_scritchany_library_directories area target)
	# Determine the target name
	squirreljme_scritchany_target_name(target area name)

	message(FATAL_ERROR "TODO")
endmacro()

# Added link libraries
macro(squirreljme_scritchany_link_libraries area target)
	# Determine the target name
	squirreljme_scritchany_target_name(target area name)

	message(FATAL_ERROR "TODO")
endmacro()

# Binary name and output
macro(squirreljme_scritchany_binary_name_and_output area target)
	# Determine the target name
	squirreljme_scritchany_target_name(target area name)

	message(FATAL_ERROR "TODO")
endmacro()

# Library exports, so that others may use
macro(squirreljme_scritchany_library_exports area target)
	# Determine the target name
	squirreljme_scritchany_target_name(target area name)

	message(FATAL_ERROR "TODO")
endmacro()

# Standard properties for everything
macro(squirreljme_scritchany_standard_properties area target)
	# Standard includes for all ScritchAny
	squirreljme_scritchany_include_directories(${area} ${target})

	# Binary name and output
	squirreljme_scritchany_binary_name_and_output(${area} ${target})

	# Exports of the ScritchAny libraries
	squirreljme_scritchany_library_exports(${area} ${target})
endmacro()

# Enables a Scritch library
macro(squirreljme_scritchany_enable area capArea)
	# Delete the old list
	file(REMOVE "${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list")

	# Make sure a blank file exists at least
	if(${CMAKE_VERSION} VERSION_GREATER_EQUAL "3.12")
		file(TOUCH "${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list")
	else()
		file(WRITE "${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list" "")
	endif()

	# Add collection target
	add_custom_target(Scritch${capArea}CollectZip
		COMMAND "${CMAKE_COMMAND}" -E "tar" "cv"
		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}/libsquirreljme-scritch${area}.zip"
			"--format=zip" "--"
		"$<TARGET_PROPERTY:Scritch${capArea}CollectZip,sjmeLibraries>"
		"${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list"
		WORKING_DIRECTORY "${SQUIRRELJME_DYLIB_OUTPUT_DIR}"
		BYPRODUCTS "${SQUIRRELJME_DYLIB_OUTPUT_DIR}/libsquirreljme-scritch${area}.zip"
		COMMENT "Collects all Scritch${capArea} outputs into one single Zip."
		COMMAND_EXPAND_LISTS)
endmacro()

# Used to build SOs into lists
macro(squirreljme_scritchany_build area capArea subDir targetBase)
	# Notice!
	message(STATUS "Scritch${capArea}: Enabling ${targetBase}!")

	# Include sub-directory for the build
	add_subdirectory(${subDir})

	# Make all ScritchArea depend on this
	add_dependencies(Scritch${capArea}
		Scritch${capArea}${targetBase})

	# Make ZIP collection depend on this
	add_dependencies(Scritch${capArea}CollectZip
		Scritch${capArea}${targetBase})

	# Include the target into the collection list
	file(APPEND
		"${SQUIRRELJME_SCRITCHLIST_DIR}/libsquirreljme-scritch${area}.list"
		"${targetBase}")

	# What is the library called?
	get_target_property(libName Scritch${capArea}${targetBase}
		LIBRARY_OUTPUT_NAME)
	set(libName
		"${CMAKE_SHARED_LIBRARY_PREFIX}${libName}${CMAKE_SHARED_LIBRARY_SUFFIX}")

	# Add target library paths
	get_target_property(targetLibs
		Scritch${capArea}CollectZip sjmeLibraries)
	if(targetLibs)
		list(APPEND targetLibs "${libName}")
	else()
		set(targetLibs "${libName}")
	endif()
	set_target_properties(Scritch${capArea}CollectZip
		PROPERTIES sjmeLibraries "${targetLibs}")
endmacro()

# Macro for more easily declaring ScritchUI sub-projects
macro(squirreljme_scritchui_build subDir targetBase)
	squirreljme_scritchany_build(ui UI
		${subDir} ${targetBase})
endmacro()

# Macro for more easily declaring ScritchAudio sub-projects
macro(squirreljme_scritchaudio_build subDir targetBase)
	squirreljme_scritchany_build(audio Audio
		${subDir} ${targetBase})
endmacro()

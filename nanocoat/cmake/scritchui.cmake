# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: ScritchUI CMake Macros

# Enables a Scritch library
macro(squirreljme_scritchany_enable area capArea)
	# Delete the old list
	file(REMOVE "${CMAKE_BINARY_DIR}/libsquirreljme-scritch${area}.list")

	# Make sure a blank file exists at least
	file(TOUCH "${CMAKE_BINARY_DIR}/libsquirreljme-scritch${area}.list")

	# Add collection target
	add_custom_target(Scritch${capArea}CollectZip
		COMMAND "${CMAKE_COMMAND}" -E "tar" "cv"
		"${SQUIRRELJME_DYLIB_OUTPUT_DIR}/libsquirreljme-scritch${area}.zip"
			"--format=zip" "--"
		"$<TARGET_PROPERTY:Scritch${capArea}CollectZip,sjmeLibraries>"
		"${CMAKE_BINARY_DIR}/libsquirreljme-scritch${area}.list"
		WORKING_DIRECTORY "${SQUIRRELJME_DYLIB_OUTPUT_DIR}"
		BYPRODUCTS "${SQUIRRELJME_DYLIB_OUTPUT_DIR}/libsquirreljme-scritch${area}.zip"
		COMMENT "Collects all Scritch${capArea} outputs into one single Zip."
		COMMAND_EXPAND_LISTS)
endmacro()

# Used to build SOs into lists
macro(squirreljme_scritchany_build area capArea ifVar subDir targetBase)
	if(${ifVar})
		# Notice!
		message(STATUS "Scritch${capArea}: Enabling ${targetBase}!")

		# Include sub-directory for the build
		add_subdirectory(${subDir})

		# Make ZIP collection depend on this
		add_dependencies(Scritch${capArea}CollectZip
			Scritch${capArea}${targetBase})

		# Include the target into the collection list
		file(APPEND "${CMAKE_BINARY_DIR}/libsquirreljme-scritch${area}.list"
			"${targetBase}")

		# What is the library called?
		get_target_property(libraryName Scritch${capArea}${targetBase}
			LIBRARY_OUTPUT_NAME)
		set(libraryName
			"${CMAKE_SHARED_LIBRARY_PREFIX}${libraryName}${CMAKE_SHARED_LIBRARY_SUFFIX}")

		# Add target library paths
		get_target_property(targetLibs
			Scritch${capArea}CollectZip sjmeLibraries)
		if(targetLibs)
			list(APPEND targetLibs "${libraryName}")
		else()
			set(targetLibs "${libraryName}")
		endif()
		set_target_properties(Scritch${capArea}CollectZip
			PROPERTIES sjmeLibraries "${targetLibs}")
	else()
		message("Scritch${capArea}: ${targetBase} not available...")
	endif()
endmacro()

# Macro for more easily declaring ScritchUI sub-projects
macro(squirreljme_scritchui_build ifVar subDir targetBase)
	squirreljme_scritchany_build(ui UI
		${ifVar} ${subDir} ${targetBase})
endmacro()

# Macro for more easily declaring ScritchAudio sub-projects
macro(squirreljme_scritchaudio_build ifVar subDir targetBase)
	squirreljme_scritchany_build(audio Audio
		${ifVar} ${subDir} ${targetBase})
endmacro()

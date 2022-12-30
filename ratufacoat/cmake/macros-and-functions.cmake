# ---------------------------------------------------------------------------
# Multi-Phasic Applications: SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the GNU General Public License v3+, or later.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Helpful functions and macros

# Function to generate object files and sources for cross building
function(squirreljme_object_and_sources coreTargetVar)
	# Make sure these are cleared
	set(CORE_OBJECTS_TEMP)
	set(CORE_SOURCES_TEMP)

	# Go through each target to add the various sources together
	foreach(cmakeTarget ${ARGV})
		# We need to skip the first argument because it is the target variable
		if("${cmakeTarget}" STREQUAL "${coreTargetVar}")
			continue()
		endif()

		# Add the object file targets accordingly, this is for the static
		# library
		list(APPEND CORE_OBJECTS_TEMP
			"$<TARGET_OBJECTS:${cmakeTarget}>")

		# Get relative directory from the source tree root
		get_target_property(cmakeTargetSourceDir ${cmakeTarget} SOURCE_DIR)
		file(RELATIVE_PATH cmakeTargetSourceDir
			"${CMAKE_SOURCE_DIR}" "${cmakeTargetSourceDir}")

		# Go through sources for the target and add them accordingly
		get_target_property(cmakeTargetSources ${cmakeTarget} SOURCES)
		foreach(cmakeTargetSource ${cmakeTargetSources})
			get_filename_component(cmakeTargetSourceExt ${cmakeTargetSource}
				EXT)
			if(NOT "${cmakeTargetSourceExt}" STREQUAL ".c" AND
				NOT "${cmakeTargetSourceExt}" STREQUAL ".cxx")
				continue()
			endif()

			# If this file does not exist, then ignore it!
			if(NOT EXISTS "${CMAKE_SOURCE_DIR}/${cmakeTargetSourceDir}/${cmakeTargetSource}")
				continue()
			endif()

			list(APPEND CORE_SOURCES_TEMP
				${cmakeTargetSourceDir}/${cmakeTargetSource})
		endforeach()
	endforeach()

	# Set global property for the object and source files
	set_property(GLOBAL PROPERTY ${coreTargetVar}Objects
		${CORE_OBJECTS_TEMP})
	set_property(GLOBAL PROPERTY ${coreTargetVar}Sources
		${CORE_SOURCES_TEMP})

	# Debugging
	if(VERBOSE)
		get_property(CORE_OBJECTS_CHECK
			GLOBAL PROPERTY ${coreTargetVar}Objects)
		get_property(CORE_SOURCES_CHECK
			GLOBAL PROPERTY ${coreTargetVar}Sources)
		message("Set ${coreTargetVar}Objects: ${CORE_OBJECTS_CHECK}")
		message("Set ${coreTargetVar}Sources: ${CORE_SOURCES_CHECK}")
	endif()
endfunction()
# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Export CMake Pipeline files only into their own package

# Grab all of the possible files that could be used by the pipeline
file(GLOB pipelineFiles
	LIST_DIRECTORIES NO
	CONFIGURE_DEPENDS YES
	RELATIVE "${CMAKE_SOURCE_DIR}"
	"${CMAKE_SOURCE_DIR}/CMakeLists.txt"
	"${CMAKE_SOURCE_DIR}/squirreljme-version"
	"${CMAKE_SOURCE_DIR}/signing-key.gpg"
	"${CMAKE_SOURCE_DIR}/public-key.gpg"
	"${CMAKE_SOURCE_DIR}/assets/cmake/*.cmake"
	"${CMAKE_SOURCE_DIR}/nanocoat/cmake/*.cmake")

# Where shall the resultant Zip be placed?
if(DEFINED $ENV{SQUIRRELJME_PIPELINE_CMAKE_OUT})
	set(zipOut "$ENV{SQUIRRELJME_PIPELINE_CMAKE_OUT}")
elseif(DEFINED(SQUIRRELJME_PIPELINE_CMAKE_OUT))
	set(zipOut "${SQUIRRELJME_PIPELINE_CMAKE_OUT}")
else()
	set(zipOut "${CMAKE_BINARY_DIR}/pipelineCMake.zip")
endif()

# Add rule to perform the export
add_custom_target(pipelineExportCMake
	COMMAND "${CMAKE_COMMAND}" "-E"
		"tar" "cf" "${zipOut}" "--format=zip" "--" "${pipelineFiles}"
	BYPRODUCTS "${zipOut}"
	SOURCES "${pipelineFiles}"
	WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
	COMMAND_EXPAND_LISTS)

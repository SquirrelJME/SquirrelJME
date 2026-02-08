# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Packaging of just the source code

# Where shall the sources be placed?
set(archiveDir "${CMAKE_BINARY_DIR}")
set(archiveBase "squirreljme-${SQUIRRELJME_VERSION}-src")
set(zipPath "${archiveDir}/${archiveBase}.zip")
set(tgzPath "${archiveDir}/${archiveBase}.tgz")

# Prefer Fossil first
if(Fossil_EXECUTABLE)
	# ZIP
	add_custom_target(sourceZip
		COMMAND "${CMAKE_COMMAND}" "-E"
			"make_directory" "${archiveDir}"
		COMMAND "${Fossil_EXECUTABLE}" "zip"
			"${SQUIRRELJME_VERSION_ID_FOSSIL}" "${zipPath}"
			"--name" "${archiveBase}"
		WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
		BYPRODUCTS "${zipPath}"
		COMMENT "Archiving Zip Source..."
		COMMAND_EXPAND_LISTS)

	# Tarball
	add_custom_target(sourceTgz
		COMMAND "${CMAKE_COMMAND}" "-E"
			"make_directory" "${archiveDir}"
		COMMAND "${Fossil_EXECUTABLE}" "tar"
			"${SQUIRRELJME_VERSION_ID_FOSSIL}" "${tgzPath}"
			"--name" "${archiveBase}"
		WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}"
		BYPRODUCTS "${tgzPath}"
		COMMENT "Archiving Tarball Source..."
		COMMAND_EXPAND_LISTS)

# Otherwise Git
elseif(Git_EXECUTABLE)
	# ZIP
	add_custom_target(sourceZip
		COMMAND "${CMAKE_COMMAND}" "-E"
			"make_directory" "${archiveDir}"
		COMMAND "${Git_EXECUTABLE}" "archive"
			"--format" "zip"
			"--prefix" "${archiveBase}/"
			"-o" "${zipPath}"
			"${SQUIRRELJME_VERSION_ID_GIT}"
		BYPRODUCTS "${zipPath}"
		COMMENT "Archiving Zip Source..."
		COMMAND_EXPAND_LISTS)

	# Tarball
	add_custom_target(sourceTgz
		COMMAND "${CMAKE_COMMAND}" "-E"
			"make_directory" "${archiveDir}"
		COMMAND "${Git_EXECUTABLE}" "archive"
			"--format" "tgz"
			"--prefix" "${archiveBase}/"
			"-o" "${tgzPath}"
			"${SQUIRRELJME_VERSION_ID_GIT}"
		BYPRODUCTS "${tgzPath}"
		COMMENT "Archiving Tarball Source..."
		COMMAND_EXPAND_LISTS)
endif()

# Shared by any output
if(Fossil_EXECUTABLE OR Git_EXECUTABLE)
	# Output where the binaries were placed
	set_target_properties(sourceZip PROPERTIES
		SQUIRRELJME_OUTPUT_PATH "${zipPath}"
		SQUIRRELJME_OUTPUT_TYPE "source")
	set_target_properties(sourceTgz PROPERTIES
		SQUIRRELJME_OUTPUT_PATH "${tgzPath}"
		SQUIRRELJME_OUTPUT_TYPE "source")

	# These get uploaded into Fossil
	squirreljme_fossil_upload_register(sourceZip sourceTgz)
endif()

# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: CMake Helper script which bundles the natives

# Read in all arguments to a list
set(args)
set(gotDashDash)
foreach(i RANGE 0 ${CMAKE_ARGC} 1)
	# Ignore until dash dash hit
	if(gotDashDash)
		list(APPEND args "${CMAKE_ARGV${i}}")
	endif()

	# Hit --?
	if("${CMAKE_ARGV${i}}" STREQUAL "--")
		set(gotDashDash YES)
	endif()
endforeach()

# Recover all arguments
list(GET args 0 SQUIRRELJME_NANOCOAT_SOURCE_DIR)
list(GET args 1 SQUIRRELJME_NANOCOAT_BINARY_DIR)
list(GET args 2 cacheFile)
list(GET args 3 nativesZip)
list(GET args 4 nativesZipSimple)
list(GET args 5 nativesDir)
list(GET args 6 nativesSubDir)

# Determine copy files
set(copyFiles "${args}")
list(REMOVE_AT copyFiles 0)
list(REMOVE_AT copyFiles 0)
list(REMOVE_AT copyFiles 0)
list(REMOVE_AT copyFiles 0)
list(REMOVE_AT copyFiles 0)
list(REMOVE_AT copyFiles 0)
list(REMOVE_AT copyFiles 0)

# Include the cache file
include("${cacheFile}"
	NO_POLICY_SCOPE)

## Get the list file and paths
set(scritchAnyLists)
list(APPEND scritchAnyLists
	"${SQUIRRELJME_NANOCOAT_BINARY_DIR}/bin/ScritchAudio.list"
	"${SQUIRRELJME_NANOCOAT_BINARY_DIR}/bin/ScritchUi.list")
file(STRINGS "${SQUIRRELJME_NANOCOAT_BINARY_DIR}/bin/ScritchAudio.paths"
	scritchAudioBins)
file(STRINGS "${SQUIRRELJME_NANOCOAT_BINARY_DIR}/bin/ScritchUi.paths"
	scritchUiBins)

# Make sure it exists before we bundle
file(MAKE_DIRECTORY "${nativesSubDir}")

# Copy the spread out binaries
foreach(from IN LISTS copyFiles
	scritchAnyLists
	scritchAudioBins
	scritchUiBins)
	message(STATUS "Copying '${from}' to '${nativesSubDir}'...")
	execute_process(
		COMMAND "${CMAKE_COMMAND}" "-E"
			"copy" "${from}" "${nativesSubDir}"
		WORKING_DIRECTORY "${nativesDir}")
endforeach()

# Add everything to the Zip
message(STATUS "Adding everything to ${nativesZip}...")
execute_process(
	COMMAND "${CMAKE_COMMAND}" "-E"
		"tar" "cv" "${nativesZip}" "--format=zip" "--"
		"."
	WORKING_DIRECTORY "${nativesDir}")

# Create a simple name for it to make things easy on the Gradle side
execute_process(
	COMMAND "${CMAKE_COMMAND}" "-E"
		"copy" "${nativesZip}" "${nativesZipSimple}"
	WORKING_DIRECTORY "${nativesDir}")

# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Builds the Standalone Jar

# The name of the target standalone Jar
set(SQUIRRELJME_JAR_BASENAME
	"squirreljme-standalone-${SQUIRRELJME_VERSION}.jar")
set(SQUIRRELJME_OUTPUT_JAR_DIR
	"${CMAKE_BINARY_DIR}/bin")
set(SQUIRRELJME_OUTPUT_JAR_PATH
	"${SQUIRRELJME_OUTPUT_JAR_DIR}/${SQUIRRELJME_JAR_BASENAME}")

# Make sure the output directory exists
file(MAKE_DIRECTORY "${SQUIRRELJME_OUTPUT_JAR_DIR}")

# Add rules and detection steps for the three
squirreljme_include("standalone-jar-compiled.cmake")
squirreljme_include("standalone-jar-download.cmake")
squirreljme_include("standalone-jar-cached.cmake")

# We need a place to put all the temporaries
set(extractedTemp "${CMAKE_BINARY_DIR}/mergeTemp")

# Get the ZIPs which should be merged together
get_property(mergeSet GLOBAL PROPERTY SQUIRRELJME_STANDALONE_MERGE_SET)
set(mergeZips)
foreach(mergeItem IN ITEMS ${mergeSet})
	# Get the output
	get_target_property(targetOutPath ${mergeItem} SQUIRRELJME_OUTPUT_PATH)
	list(APPEND mergeZips "${targetOutPath}")

	# Debug
	message(STATUS "Standalone Jar: Merge from '${targetOutPath}'...")
endforeach()

# Merging of the Base Standalone with All Natives
add_custom_target(standalone.jar ALL
	COMMAND "${CMAKE_COMMAND}" "-E"
		"rm" "-rf" "--" "${extractedTemp}"
	COMMAND "${CMAKE_COMMAND}" "-E"
		"make_directory" "${extractedTemp}"
	COMMAND "${CMAKE_COMMAND}"
		"-P" "${CMAKE_CURRENT_LIST_DIR}/merge-standalone.cmake" "--"
			"${SQUIRRELJME_OUTPUT_JAR_PATH}" "${extractedTemp}"
			"${mergeZips}"
	DEPENDS "${mergeSet}"
	BYPRODUCTS "${SQUIRRELJME_OUTPUT_JAR_PATH}"
	COMMENT "Merging Standalone Jar into '${SQUIRRELJME_OUTPUT_JAR_PATH}'..."
	COMMAND_EXPAND_LISTS)

# Add this to all
set_target_properties(standalone.jar PROPERTIES
	EXCLUDE_FROM_ALL NO
	SQUIRRELJME_OUTPUT_PATH "${SQUIRRELJME_OUTPUT_JAR_PATH}"
	SQUIRRELJME_OUTPUT_TYPE "standalone")

# Add to the order
squirreljme_natives_append_rule(standalone.jar
	"standalone" "standalone" "compiler")

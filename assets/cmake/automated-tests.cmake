# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Running of automated tests

# Add testing for each virtual machine type and clutter level
foreach(jvm IN ITEMS ${SQUIRRELJME_JVM_MAP})
	foreach(clutter IN ITEMS ${SQUIRRELJME_CLUTTER_MAP})
		# Extract proper nouns
		squirreljme_unmap(jvmNoun 0 "${jvm}")
		squirreljme_unmap(clutterNoun 0 "${clutter}")

		# There is no hosted release build
		if("${jvmNoun}" STREQUAL "Hosted" AND
			"${clutterNoun}" STREQUAL "Release")
			continue()
		endif()

		# Skip NanoCoat for now as it is not fully implemented
		if("${jvmNoun}" STREQUAL "NanoCoat")
			message(DEBUG "TODO: Enable NanoCoat once implemented")
			continue()
		endif()

		# What is this target called?
		set(targetName test${jvmNoun}${clutterNoun})

		# This can only be done if Java is available
		if(SQUIRRELJME_HAS_JAVA)
			# Notice
			message(STATUS
				"Tests for ${jvmNoun} (${clutterNoun}) -> "
				"test${jvmNoun}${clutterNoun}")

			# Determine test result directory to use
			set(testResultsDir "${CMAKE_BINARY_DIR}/tests/${targetName}")
			file(MAKE_DIRECTORY "${testResultsDir}")

			# Native is needed for Gradle
			file(TO_NATIVE_PATH "${testResultsDir}"
				testResultsDirNative)

			# Setup testing through Gradle
			squirreljme_add_gradle_target(${targetName}
				"-Dsquirreljme.test.dir=${testResultsDirNative}"
				"-Psquirreljme.test.dir=${testResultsDirNative}"
				"test${jvmNoun}${clutterNoun}")

			# Set some SquirrelJME specific properties
			set_target_properties(${targetName} PROPERTIES
				SQUIRRELJME_TEST_RESULTS_DIR "${testResultsDir}")

			# Need to make pseudo test target?
			if(NOT TARGET testAll)
				add_custom_target(testAll)
			endif()

			# Add to pseudo test target
			add_dependencies(testAll
				${targetName})
		endif()
	endforeach()
endforeach()

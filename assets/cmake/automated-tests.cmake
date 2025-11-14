# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Running of automated tests

# Add testing for each virtual machine type and clutter level
foreach(jvm IN LISTS SQUIRRELJME_JVM_MAP)
	foreach(clutter IN LISTS SQUIRRELJME_CLUTTER_MAP)
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

			# Setup testing through Gradle
			squirreljme_add_gradle_target(${targetName}
				"test${jvmNoun}${clutterNoun}")

			# Register this task with CI/CD
			squirreljme_cicd_register(${targetName})
		endif()
	endforeach()
endforeach()

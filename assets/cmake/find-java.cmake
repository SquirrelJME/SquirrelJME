# ---------------------------------------------------------------------------
# SquirrelJME
#     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
# ---------------------------------------------------------------------------
# SquirrelJME is under the Mozilla Public License Version 2.0.
# See license.mkd for licensing and copyright information.
# ---------------------------------------------------------------------------
# DESCRIPTION: Locates Java

# Locate Java 8
find_package(Java 1.8
	COMPONENTS Development)

# If javac was not found, try alternatives
if(NOT Java_Development_FOUND OR
	NOT Java_JAVAC_EXECUTABLE)
	# Remove these from the cache
	unset(Java_Development_FOUND CACHE)
	unset(Java_JAVAC_EXECUTABLE CACHE)

	# Try finding it through alternatives
	find_program(Java_JAVAC_EXECUTABLE
		NAMES javac ecj gcj)
endif()

# Which Java was found?
message(STATUS "java : ${Java_JAVA_EXECUTABLE}")
message(STATUS "javac: ${Java_JAVAC_EXECUTABLE}")

# Java build available?
if(Java_JAVA_EXECUTABLE AND Java_JAVAC_EXECUTABLE)
	set(SQUIRRELJME_HAS_JAVA_DEFAULT YES)
else()
	set(SQUIRRELJME_HAS_JAVA_DEFAULT NO)
endif()
option(SQUIRRELJME_HAS_JAVA "Java Build Capability"
	${SQUIRRELJME_HAS_JAVA_DEFAULT})

# Used for CI/CD integration
define_property(TARGET PROPERTY SQUIRRELJME_GRADLE_BUILD
	BRIEF_DOCS "Is this a Gradle build?")
define_property(TARGET PROPERTY SQUIRRELJME_OUTPUT_PATH
	BRIEF_DOCS "The resultant output path, used for later upload.")
define_property(TARGET PROPERTY SQUIRRELJME_OUTPUT_TYPE
	BRIEF_DOCS "The resultant output type, used for later upload.")
define_property(TARGET PROPERTY SQUIRRELJME_TEST_RESULTS_DIR
	BRIEF_DOCS "Directory where test results are stored.")

# Only possible when Java is available
if(SQUIRRELJME_HAS_JAVA)
	# Which Gradle is to be used?
	if("${CMAKE_HOST_SYSTEM_NAME}" STREQUAL "Windows")
		file(TO_CMAKE_PATH "${CMAKE_SOURCE_DIR}/gradlew"
			SQUIRRELJME_GRADLE_EXECUTABLE)
	else()
		file(TO_CMAKE_PATH "${CMAKE_SOURCE_DIR}/gradlew"
			SQUIRRELJME_GRADLE_EXECUTABLE)
	endif()

	# Add a target which executes Gradle
	function(squirreljme_add_gradle_target targetName ...)
		# Extract arguments for the call
		list(SUBLIST ARGV 1 -1 gradleArgs)

		# Now declare the target
		add_custom_target(${targetName}
			COMMAND "${SQUIRRELJME_GRADLE_EXECUTABLE}"
				"--console" "plain"
				"--continue"
				"--parallel"
				"--no-daemon"
				"--stacktrace"
				"${gradleArgs}"
			COMMAND_EXPAND_LISTS
			WORKING_DIRECTORY "${CMAKE_SOURCE_DIR}")

			# Set some SquirrelJME specific properties
			set_target_properties(${targetName} PROPERTIES
				SQUIRRELJME_GRADLE_BUILD YES
				SQUIRRELJME_TEST_RESULTS_DIR "${testResultsDir}")
	endfunction()
endif()

